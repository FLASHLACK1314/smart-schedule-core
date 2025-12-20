package io.github.flashlack1314.smartschedulecore.controllers.v1;

import io.github.flashlack1314.smartschedulecore.models.vo.ResultVO;
import io.github.flashlack1314.smartschedulecore.services.EmailService;
import io.github.flashlack1314.smartschedulecore.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 电子邮件控制器
 *
 * @author flash
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
public class EmailController {
    private final EmailService emailService;
    private final UserService userService;

    /**
     * 发送验证码到指定邮箱
     *
     * @param email 收件人邮箱
     * @return 操作结果
     */
    @PostMapping("/sendEmail")
    public ResultVO<Void> sendVerificationCode(
            @Valid @RequestParam @Email String email
    ) {
        emailService.sendVerificationCodeHtml(email);
        return ResultVO.success("验证码发送成功");
    }

    /**
     * 发送密码重置验证码
     * 无需登录，用于忘记密码场景
     *
     * @param email 用户邮箱
     * @return 操作结果
     */
    @PostMapping("/send-reset-code")
    public ResultVO<Void> sendPasswordResetCode(
            @Valid @RequestParam @Email String email) {
        userService.sendPasswordResetCode(email);
        return ResultVO.success("验证码发送成功，请查收邮件");
    }
}
