package io.github.flashlack1314.smartschedulecore.models.vo;

import io.github.flashlack1314.smartschedulecore.constants.StringConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 注册视图对象
 *
 * @author flash
 */
@Data
public class RegisterVO {
    /**
     * 用户姓名（必填）
     */
    @NotBlank(message = "用户姓名不能为空")
    @Pattern(regexp = StringConstant.Regexp.USERNAME, message = "用户姓名格式不正确")
    private String userName;

    /**
     * 用户邮箱（必填）
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 64, message = "邮箱长度不能超过64个字符")
    private String userEmail;

    /**
     * 邮箱验证码（必填，6位数字）
     */
    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = StringConstant.Regexp.VERIFICATION_CODE, message = "验证码必须是6位数字")
    private String verificationCode;

    /**
     * 密码（必填）
     */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = StringConstant.Regexp.PASSWORD, message = "密码格式不正确")
    private String userPassword;
    /**
     * 角色名称英文（必填）
     */
    @NotBlank(message = "角色不能为空")
    private String roleNameEn;

    /**
     * 手机号（可选）
     */
    @Pattern(regexp = StringConstant.Regexp.PHONE, message = "手机 号格式不正确")
    private String userPhoneNum;
}
