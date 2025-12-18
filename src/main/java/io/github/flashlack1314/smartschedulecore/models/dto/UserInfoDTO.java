package io.github.flashlack1314.smartschedulecore.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户信息DTO
 *
 * @author flash
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserInfoDTO {
    /**
     * 用户UUID
     */
    private String userUuid;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户手机号
     */
    private String userPhoneNum;

    /**
     * 角色中文名称
     */
    private String roleName;

    /**
     * 角色英文名称
     */
    private String roleNameEn;
}