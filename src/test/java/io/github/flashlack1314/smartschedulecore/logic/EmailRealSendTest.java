package io.github.flashlack1314.smartschedulecore.logic;

import io.github.flashlack1314.smartschedulecore.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 邮件发送真实测试
 * 发送真实邮件到指定邮箱
 *
 * @author flash
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
@DisplayName("邮件真实发送测试")
class EmailRealSendTest {

    @Autowired
    private EmailService emailService;

    @Test
    @DisplayName("发送验证码邮件到1550909467@qq.com")
    void testSendVerificationCodeToQQEmail() {
        String targetEmail = "1550909467@qq.com";

        log.info("=".repeat(80));
        log.info("开始真实邮件发送测试");
        log.info("目标邮箱: {}", targetEmail);
        log.info("=".repeat(80));

        // 发送验证码邮件
        String verificationCode = emailService.sendVerificationCodeHtml(targetEmail);

        // 验证返回的验证码
        assertNotNull(verificationCode, "验证码不应为null");
        assertEquals(6, verificationCode.length(), "验证码应为6位");
        assertTrue(verificationCode.matches("\\d{6}"), "验证码应为6位数字");

        log.info("✅ 邮件发送成功！");
        log.info("收件人: {}", targetEmail);
        log.info("验证码: {}", verificationCode);
        log.info("有效期: 5分钟");
        log.info("=".repeat(80));
        log.info("请检查邮箱收件箱或垃圾箱");
        log.info("=".repeat(80));
    }
}