package io.github.flashlack1314.smartschedulecore.controllers.v1;

import io.github.flashlack1314.smartschedulecore.models.vo.ResultVO;
import io.github.flashlack1314.smartschedulecore.services.EmailService;
import jakarta.validation.Valid;
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

    @PostMapping("/sendEmail")
    public ResultVO<Void> sendVerificationCode(
            @Valid @RequestParam String email
    ) {
        emailService.sendVerificationCodeHtml(email);
        return ResultVO.success("验证码发送成功");
    }
}
