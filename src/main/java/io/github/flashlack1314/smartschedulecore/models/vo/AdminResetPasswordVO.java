package io.github.flashlack1314.smartschedulecore.models.vo;

import io.github.flashlack1314.smartschedulecore.constants.StringConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 管理员重置用户密码请求对象
 *
 * @author flash
 */
@Data
public class AdminResetPasswordVO {

    /**
     * 目标用户邮箱
     */
    @NotBlank(message = "用户邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String userEmail;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = StringConstant.Regexp.PASSWORD, message = "密码格式不正确")
    private String newPassword;
}