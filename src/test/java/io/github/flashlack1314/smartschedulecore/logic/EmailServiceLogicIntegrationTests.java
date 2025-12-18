package io.github.flashlack1314.smartschedulecore.logic;

import io.github.flashlack1314.smartschedulecore.constants.StringConstant;
import io.github.flashlack1314.smartschedulecore.services.EmailService;
import io.github.flashlack1314.smartschedulecore.utils.VerificationCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EmailService 集成测试类
 * 使用真实的 Redis 和邮件服务进行测试
 * 面向接口测试，而非具体实现类
 *
 * @author flash
 */
@Slf4j
@SpringBootTest
@DisplayName("EmailService 集成测试")
class EmailServiceLogicIntegrationTests {

    /**
     * 记录测试过程中使用的邮箱，用于清理
     */
    private final List<String> testEmails = new ArrayList<>();
    @Autowired
    private EmailService emailService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 每个测试执行后清理 Redis 中的测试数据
     */
    @AfterEach
    void cleanUp() {
        log.info("开始清理测试数据，共 {} 个邮箱", testEmails.size());
        for (String email : testEmails) {
            String redisKey = StringConstant.Redis.EMAIL_VERIFICATION_PREFIX + email;
            Boolean deleted = redisTemplate.delete(redisKey);
            log.info("清理 Redis key: {}, 结果: {}", redisKey, deleted);
        }
        testEmails.clear();
        log.info("测试数据清理完成");
    }

    @Test
    @DisplayName("集成测试 - 发送验证码到真实邮箱并验证Redis")
    void testSendVerificationCodeHtml_RealRedis() {
        // Given: 准备测试邮箱
        String testEmail = "integration-test@flashlack.cn";
        testEmails.add(testEmail);

        try {
            // When: 发送验证码邮件
            emailService.sendVerificationCodeHtml(testEmail);

            // Then: 验证 Redis 中存在验证码
            String redisKey = StringConstant.Redis.EMAIL_VERIFICATION_PREFIX + testEmail;
            assertTrue(redisTemplate.hasKey(redisKey), "Redis 中应该存在验证码");

            // 验证验证码格式（6位数字）
            Object storedCode = redisTemplate.opsForValue().get(redisKey);
            assertNotNull(storedCode, "Redis 中的验证码不应为空");
            String codeStr = storedCode.toString();
            assertTrue(codeStr.matches("\\d{6}"), "验证码应该是6位数字，实际: " + codeStr);

            log.info("验证码已成功保存到 Redis: email={}, code={}", testEmail, codeStr);
        } catch (Exception e) {
            log.error("发送验证码失败", e);
            fail("发送验证码失败: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("集成测试 - 验证正确的验证码")
    void testVerifyCode_CorrectCode_RealRedis() {
        // Given: 准备测试邮箱并保存验证码到 Redis
        String testEmail = "verify-correct@flashlack.cn";
        testEmails.add(testEmail);
        String code = "123456";

        VerificationCodeUtils.saveCode(testEmail, code);
        log.info("验证码已保存到 Redis: email={}, code={}", testEmail, code);

        try {
            // When: 验证正确的验证码
            boolean result = emailService.verifyCode(testEmail, code);

            // Then: 验证应该成功
            assertTrue(result, "正确验证码应该验证成功");
            log.info("验证码验证成功: email={}, code={}", testEmail, code);

            // 验证 Redis 中的验证码已被删除
            String redisKey = StringConstant.Redis.EMAIL_VERIFICATION_PREFIX + testEmail;
            assertFalse(redisTemplate.hasKey(redisKey), "验证成功后 Redis 中的验证码应该被删除");

        } catch (Exception e) {
            log.error("验证验证码失败", e);
            fail("验证验证码失败: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("集成测试 - 验证错误的验证码")
    void testVerifyCode_WrongCode_RealRedis() {
        // Given: 准备测试邮箱并保存验证码到 Redis
        String testEmail = "verify-wrong@flashlack.cn";
        testEmails.add(testEmail);
        String correctCode = "123456";

        VerificationCodeUtils.saveCode(testEmail, correctCode);
        log.info("验证码已保存到 Redis: email={}, code={}", testEmail, correctCode);

        try {
            // When: 验证错误的验证码
            boolean result = emailService.verifyCode(testEmail, "654321");

            // Then: 验证应该失败
            assertFalse(result, "错误验证码应该验证失败");
            log.info("验证码验证失败（预期行为）: email={}", testEmail);

            // 验证 Redis 中的验证码仍然存在
            String redisKey = StringConstant.Redis.EMAIL_VERIFICATION_PREFIX + testEmail;
            assertTrue(redisTemplate.hasKey(redisKey), "验证失败后 Redis 中的验证码应该仍然存在");

        } catch (Exception e) {
            log.error("验证验证码失败", e);
            fail("验证验证码失败: " + e.getMessage());
        }
    }
}