package io.github.flashlack1314.smartschedulecore.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 注册返回数据传输对象
 *
 * @author flash
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RegisterBackDTO {
    /**
     * 用户令牌
     */
    private String token;

    /**
     * 用户信息
     */
    private UserInfo userInfo;

    /**
     * 用户信息嵌套类
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class UserInfo {
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
}
