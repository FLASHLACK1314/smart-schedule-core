package io.github.flashlack1314.smartschedulecore.services;

import io.github.flashlack1314.smartschedulecore.models.dto.LoginBackDTO;
import io.github.flashlack1314.smartschedulecore.models.vo.LoginVO;
import jakarta.validation.Valid;

/**
 * 认证服务接口
 * 负责登录、登出等核心认证功能
 *
 * @author flash
 */
public interface AuthService {
    /**
     * 用户登录
     *
     * @param loginVO 登录信息
     * @return 登录返回数据
     */
    LoginBackDTO login(
            @Valid LoginVO loginVO);

    /**
     * 用户登出
     *
     * @param userUuid 用户UUID
     * @param token    登录Token
     */
    void logout(String userUuid, String token);
}
