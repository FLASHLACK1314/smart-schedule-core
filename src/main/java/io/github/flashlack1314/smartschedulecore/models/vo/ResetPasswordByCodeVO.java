package io.github.flashlack1314.smartschedulecore.models.vo;

import io.github.flashlack1314.smartschedulecore.constants.StringConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 验证码重置密码请求对象
 *
 * @author flash
 */
@Data
public class ResetPasswordByCodeVO {

    /**
     * 用户邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String userEmail;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = StringConstant.Regexp.VERIFICATION_CODE, message = "验证码必须是6位数字")
    private String verificationCode;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = StringConstant.Regexp.PASSWORD, message = "密码格式不正确")
    private String newPassword;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}