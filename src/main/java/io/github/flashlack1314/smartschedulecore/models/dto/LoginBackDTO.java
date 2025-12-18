package io.github.flashlack1314.smartschedulecore.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 登录返回数据传输对象
 *
 * @author flash
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class LoginBackDTO {
    /**
     * 用户令牌
     */
    private String token;

    /**
     * 用户信息
     */
    private UserInfoDTO userInfo;
}
