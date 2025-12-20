package io.github.flashlack1314.smartschedulecore.services;

import io.github.flashlack1314.smartschedulecore.exceptions.BusinessException;

/**
 * 邮件服务接口
 * 负责邮件发送和验证码验证
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
public interface EmailService {

    /**
     * 发送验证码邮件（HTML格式）
     *
     * @param toEmail 收件人邮箱
     */
    void sendVerificationCodeHtml(String toEmail);

    /**
     * 验证验证码
     *
     * @param email 邮箱地址
     * @param code  用户输入的验证码
     * @return true-验证成功，false-验证失败
     */
    boolean verifyCode(String email, String code);

    // ========== 密码重置相关邮件方法 ==========

    /**
     * 发送密码重置验证码邮件
     *
     * @param toEmail 收件人邮箱
     * @throws BusinessException 邮件发送失败时抛出异常
     */
    void sendPasswordResetCode(String toEmail);

    /**
     * 发送密码重置成功通知邮件
     *
     * @param toEmail  收件人邮箱
     * @param userName 用户名（可选）
     * @throws BusinessException 邮件发送失败时抛出异常
     */
    void sendPasswordResetSuccessNotification(String toEmail, String userName);

    /**
     * 验证密码重置验证码
     *
     * @param email 邮箱地址
     * @param code  用户输入的验证码
     * @return true-验证成功，false-验证失败
     */
    boolean verifyPasswordResetCode(String email, String code);
}
