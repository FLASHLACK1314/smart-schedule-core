package io.github.flashlack1314.smartschedulecore.logic;

import io.github.flashlack1314.smartschedulecore.constants.StringConstant;
import io.github.flashlack1314.smartschedulecore.exceptions.BusinessException;
import io.github.flashlack1314.smartschedulecore.exceptions.ErrorCode;
import io.github.flashlack1314.smartschedulecore.services.EmailService;
import io.github.flashlack1314.smartschedulecore.utils.VerificationCodeUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 邮件服务实现类
 * 负责邮件发送和验证码验证的具体实现
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailServiceLogic implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 发件人邮箱地址（从配置文件读取）
     */
    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 发送验证码邮件（HTML格式）
     * 使用Thymeleaf模板引擎渲染邮件内容
     *
     * @param toEmail 收件人邮箱
     */
    @Override
    public void sendVerificationCodeHtml(String toEmail) {
        try {
            // 1. 检查是否可以发送验证码（防止1分钟内重复发送）
            if (!VerificationCodeUtils.canSendCode(toEmail)) {
                long remainingSeconds = VerificationCodeUtils.getRemainingCooldown(toEmail);
                log.warn("验证码发送失败: 距离上次发送不足1分钟, toEmail: {}, 剩余冷却时间: {}秒",
                        toEmail, remainingSeconds);
                throw new BusinessException(
                        String.format("验证码发送过于频繁，请%d秒后再试", remainingSeconds),
                        ErrorCode.EMAIL_SEND_TOO_FREQUENT
                );
            }

            // 2. 生成验证码
            String code = VerificationCodeUtils.generateCode();

            // 3. 保存到Redis
            VerificationCodeUtils.saveCode(toEmail, code);

            // 4. 使用Thymeleaf渲染HTML模板
            Context context = new Context();
            context.setVariable("code", code);
            String htmlContent = templateEngine.process("email-verification-code", context);

            // 5. 创建邮件消息
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("【智能排程系统】邮箱验证码");
            helper.setText(htmlContent, true);

            // 6. 发送邮件
            mailSender.send(message);
            log.info("验证码邮件发送成功, toEmail: {}", toEmail);

        } catch (MessagingException e) {
            log.error("验证码邮件发送失败, toEmail: {}, error: {}", toEmail, e.getMessage(), e);
            throw new BusinessException(
                    "邮件发送失败: " + e.getMessage(),
                    ErrorCode.EMAIL_SEND_FAILED
            );
        }
    }

    /**
     * 验证验证码
     *
     * @param email 邮箱地址
     * @param code  用户输入的验证码
     * @return true-验证成功，false-验证失败
     */
    @Override
    public boolean verifyCode(String email, String code) {
        return VerificationCodeUtils.verifyCode(email, code);
    }

    // ========== 密码重置相关邮件方法实现 ==========

    @Override
    public void sendPasswordResetCode(String toEmail) {
        try {
            // 1. 检查是否可以发送验证码（防止1分钟内重复发送）
            String passwordResetKey = StringConstant.Redis.PASSWORD_RESET_PREFIX + toEmail;
            String lastSentKey = passwordResetKey + ":last_sent";

            if (redisTemplate.hasKey(lastSentKey)) {
                long remainingSeconds = redisTemplate.getExpire(lastSentKey);
                log.warn("密码重置验证码发送失败: 距离上次发送不足1分钟, toEmail: {}, 剩余冷却时间: {}秒",
                        toEmail, remainingSeconds);
                throw new BusinessException(
                        String.format("验证码发送过于频繁，请%d秒后再试", remainingSeconds),
                        ErrorCode.EMAIL_SEND_TOO_FREQUENT
                );
            }

            // 2. 生成验证码
            String code = VerificationCodeUtils.generateCode();

            // 3. 保存到Redis（密码重置验证码使用单独的key前缀）
            redisTemplate.opsForValue().set(passwordResetKey, code, java.time.Duration.ofMinutes(5));

            // 4. 设置发送冷却标记
            redisTemplate.opsForValue().set(lastSentKey, "sent", java.time.Duration.ofMinutes(1));

            // 5. 使用Thymeleaf渲染HTML模板
            Context context = new Context();
            context.setVariable("code", code);
            String htmlContent = templateEngine.process("email-password-reset-code", context);

            // 6. 创建邮件消息
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("【智能排程系统】密码重置验证码");
            helper.setText(htmlContent, true);

            // 7. 发送邮件
            mailSender.send(message);
            log.info("密码重置验证码邮件发送成功, toEmail: {}", toEmail);

        } catch (MessagingException e) {
            log.error("密码重置验证码邮件发送失败, toEmail: {}, error: {}", toEmail, e.getMessage(), e);
            throw new BusinessException(
                    "邮件发送失败: " + e.getMessage(),
                    ErrorCode.EMAIL_SEND_FAILED
            );
        }
    }

    @Override
    public void sendPasswordResetSuccessNotification(String toEmail, String userName) {
        try {
            // 使用Thymeleaf渲染HTML模板
            Context context = new Context();
            context.setVariable("userName", userName != null ? userName : "用户");
            context.setVariable("resetTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            String htmlContent = templateEngine.process("email-password-reset-success", context);

            // 创建邮件消息
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("【智能排程系统】密码重置成功通知");
            helper.setText(htmlContent, true);

            // 发送邮件
            mailSender.send(message);
            log.info("密码重置成功通知邮件发送成功, toEmail: {}", toEmail);

        } catch (MessagingException e) {
            log.error("密码重置成功通知邮件发送失败, toEmail: {}, error: {}", toEmail, e.getMessage(), e);
            throw new BusinessException(
                    "邮件发送失败: " + e.getMessage(),
                    ErrorCode.EMAIL_SEND_FAILED
            );
        }
    }

    @Override
    public boolean verifyPasswordResetCode(String email, String code) {
        String passwordResetKey = StringConstant.Redis.PASSWORD_RESET_PREFIX + email;

        try {
            String savedCode = (String) redisTemplate.opsForValue().get(passwordResetKey);
            if (savedCode == null) {
                log.warn("密码重置验证码不存在或已过期: email={}", email);
                return false;
            }

            boolean isValid = savedCode.equals(code);
            if (isValid) {
                // 验证成功后删除验证码
                redisTemplate.delete(passwordResetKey);
                String lastSentKey = passwordResetKey + ":last_sent";
                redisTemplate.delete(lastSentKey);
                log.info("密码重置验证码验证成功并已删除: email={}", email);
            } else {
                log.warn("密码重置验证码不匹配: email={}, inputCode={}, savedCode={}", email, code, savedCode);
            }

            return isValid;
        } catch (Exception e) {
            log.error("验证密码重置验证码时发生错误: email={}, error={}", email, e.getMessage(), e);
            return false;
        }
    }
}
