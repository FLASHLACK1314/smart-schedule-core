package io.github.flashlack1314.smartschedulecore.services;

import io.github.flashlack1314.smartschedulecore.models.dto.RegisterBackDTO;
import io.github.flashlack1314.smartschedulecore.models.vo.RegisterVO;
import jakarta.validation.Valid;

/**
 * 认证服务接口
 *
 * @author flash
 */
public interface AuthService {
    /**
     * 注册用户
     *
     * @param registerVO 注册信息
     * @return 注册返回数据
     */
    RegisterBackDTO registerUser(
            @Valid RegisterVO registerVO);
}
