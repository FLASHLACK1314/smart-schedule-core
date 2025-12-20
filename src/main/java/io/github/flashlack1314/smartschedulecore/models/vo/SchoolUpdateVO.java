package io.github.flashlack1314.smartschedulecore.models.vo;

import io.github.flashlack1314.smartschedulecore.constants.StringConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新学校请求对象
 *
 * @author flash
 */
@Data
public class SchoolUpdateVO {

    /**
     * 学校UUID
     */
    @NotBlank(message = "学校UUID不能为空")
    @Pattern(regexp = "^[a-f0-9]{32}$", message = "学校UUID格式不正确")
    private String schoolUuid;

    /**
     * 学校名称
     */
    @Size(max = 128, message = "学校名称不能超过128个字符")
    private String schoolName;

    /**
     * 英文名称
     */
    @Size(max = 128, message = "英文名称不能超过128个字符")
    private String schoolNameEn;

    /**
     * 学校类型
     */
    @Size(max = 32, message = "学校类型不能超过32个字符")
    private String schoolType;

    /**
     * 学校地址
     */
    @Size(max = 256, message = "学校地址不能超过256个字符")
    private String schoolAddress;

    /**
     * 联系电话
     */
    @Pattern(regexp = StringConstant.Regexp.PHONE, message = "联系电话格式不正确")
    private String schoolPhone;

    /**
     * 邮箱地址
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 64, message = "邮箱不能超过64个字符")
    private String schoolEmail;
}