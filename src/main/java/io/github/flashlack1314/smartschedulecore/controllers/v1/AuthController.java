package io.github.flashlack1314.smartschedulecore.controllers.v1;

import io.github.flashlack1314.smartschedulecore.models.dto.LoginBackDTO;
import io.github.flashlack1314.smartschedulecore.models.vo.LoginVO;
import io.github.flashlack1314.smartschedulecore.models.vo.ResultVO;
import io.github.flashlack1314.smartschedulecore.services.AuthService;
import io.github.flashlack1314.smartschedulecore.services.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author flash
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;

    /**
     * 用户登录
     *
     * @param loginVO 登录信息
     * @return 操作结果及登录返回数据
     */
    @PostMapping("/login")
    public ResultVO<LoginBackDTO> login(
            @Valid @RequestBody LoginVO loginVO
    ) {
        LoginBackDTO loginBackDTO = authService.login(loginVO);
        return ResultVO.success("登录成功", loginBackDTO);
    }

    /**
     * 用户登出
     *
     * @param authorization Token值（可以是Bearer格式或直接token）
     * @return 操作结果
     */
    @PostMapping("/logout")
    public ResultVO<Void> logout(
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        // 直接使用authorization作为token，不需要Bearer前缀
        if (!StringUtils.hasText(authorization)) {
            return ResultVO.error("Token不能为空");
        }

        // 验证token并获取用户UUID
        String userUuid = tokenService.validateAndGetUser(authorization);
        if (!StringUtils.hasText(userUuid)) {
            return ResultVO.error("Token无效或已过期");
        }

        // 调用登出服务
        authService.logout(userUuid, authorization);
        return ResultVO.success("登出成功");
    }
}
