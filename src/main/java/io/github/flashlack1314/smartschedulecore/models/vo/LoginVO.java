package io.github.flashlack1314.smartschedulecore.models.vo;

import io.github.flashlack1314.smartschedulecore.constants.StringConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 登录视图对象
 *
 * @author flash
 */
@Data
public class LoginVO {
    /**
     * 用户邮箱（必填）
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 64, message = "邮箱长度不能超过64个字符")
    private String userEmail;

    /**
     * 密码（必填）
     */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = StringConstant.Regexp.PASSWORD, message = "密码格式不正确")
    private String userPassword;
}