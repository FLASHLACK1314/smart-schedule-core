package io.github.flashlack1314.smartschedulecore.controllers.v1;

import io.github.flashlack1314.smartschedulecore.models.dto.RegisterBackDTO;
import io.github.flashlack1314.smartschedulecore.models.vo.RegisterVO;
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

    @PostMapping("/register")
    public ResultVO<RegisterBackDTO> register(
            @Valid @RequestBody RegisterVO registerVO
    ) {
        // 注册逻辑待实现
        RegisterBackDTO registerBackDTO = authService.registerUser(registerVO);
        return ResultVO.success("注册成功", registerBackDTO);
    }
}
