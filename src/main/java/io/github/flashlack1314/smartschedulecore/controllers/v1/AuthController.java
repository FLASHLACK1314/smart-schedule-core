package io.github.flashlack1314.smartschedulecore.controllers.v1;

import io.github.flashlack1314.smartschedulecore.models.dto.LoginBackDTO;
import io.github.flashlack1314.smartschedulecore.models.vo.LoginVO;
import io.github.flashlack1314.smartschedulecore.models.vo.ResultVO;
import io.github.flashlack1314.smartschedulecore.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
