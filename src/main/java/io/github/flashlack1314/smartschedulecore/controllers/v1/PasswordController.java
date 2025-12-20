package io.github.flashlack1314.smartschedulecore.controllers.v1;

import io.github.flashlack1314.smartschedulecore.annotation.RequireRole;
import io.github.flashlack1314.smartschedulecore.models.vo.AdminResetPasswordVO;
import io.github.flashlack1314.smartschedulecore.models.vo.ChangePasswordVO;
import io.github.flashlack1314.smartschedulecore.models.vo.ResetPasswordByCodeVO;
import io.github.flashlack1314.smartschedulecore.models.vo.ResultVO;
import io.github.flashlack1314.smartschedulecore.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 密码管理控制器
 * 负责密码修改、重置等密码管理相关的API接口
 *
 * @author flash
 */
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/password")
public class PasswordController {

    private final UserService userService;

    /**
     * 用户修改密码
     * 需要登录，验证当前密码后修改为新密码
     *
     * @param authorization    用户Token
     * @param changePasswordVO 修改密码请求对象
     * @return 操作结果
     */
    @PostMapping("/change")
    @RequireRole
    public ResultVO<Void> changePassword(
            @RequestHeader(value = "Authorization") String authorization,
            @Valid @RequestBody ChangePasswordVO changePasswordVO
    ) {
        userService.changePasswordByToken(authorization,
                changePasswordVO.getCurrentPassword(),
                changePasswordVO.getNewPassword(),
                changePasswordVO.getConfirmPassword());
        return ResultVO.success("密码修改成功");
    }

    /**
     * 管理员重置用户密码
     * 需要管理员或教务处权限
     *
     * @param authorization        管理员Token
     * @param adminResetPasswordVO 管理员重置密码请求对象
     * @return 操作结果
     */
    @PostMapping("/reset-password")
    @RequireRole("admin")
    public ResultVO<Void> resetUserPassword(
            @RequestHeader(value = "Authorization") String authorization,
            @Valid @RequestBody AdminResetPasswordVO adminResetPasswordVO
    ) {
        userService.resetUserPasswordByToken(authorization,
                adminResetPasswordVO.getUserEmail(),
                adminResetPasswordVO.getNewPassword());
        return ResultVO.success("密码重置成功");
    }



    /**
     * 通过验证码重置密码
     * 无需登录，用于忘记密码场景
     *
     * @param resetPasswordByCodeVO 验证码重置密码请求对象
     * @return 操作结果
     */
    @PostMapping("/reset-by-code")
    public ResultVO<Void> resetPasswordByCode(
            @Valid @RequestBody ResetPasswordByCodeVO resetPasswordByCodeVO) {
        userService.resetPasswordByCodeWithValidation(
                resetPasswordByCodeVO.getUserEmail(),
                resetPasswordByCodeVO.getVerificationCode(),
                resetPasswordByCodeVO.getNewPassword(),
                resetPasswordByCodeVO.getConfirmPassword());
        return ResultVO.success("密码重置成功");
    }
}