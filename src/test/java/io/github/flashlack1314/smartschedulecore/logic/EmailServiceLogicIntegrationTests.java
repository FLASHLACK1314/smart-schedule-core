package io.github.flashlack1314.smartschedulecore.logic;

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
            String redisKey = "email:verification:" + email;
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
            String redisKey = "email:verification:" + testEmail;
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
    @DisplayName("集成测试 - 验证60秒冷却时间限制")
    void testSendVerificationCodeHtml_CooldownWithRealRedis() {
        // Given: 准备测试邮箱
        String testEmail = "cooldown-test@flashlack.cn";
        testEmails.add(testEmail);

        try {
            // When: 第一次发送验证码
            emailService.sendVerificationCodeHtml(testEmail);
            log.info("第一次发送验证码成功");

            // Then: 立即第二次发送应该抛出异常
            RuntimeException exception = assertThrows(RuntimeException.class, () -> emailService.sendVerificationCodeHtml(testEmail));

            assertTrue(exception.getMessage().contains("验证码发送过于频繁"),
                    "应该提示验证码发送过于频繁");
            log.info("冷却时间限制验证成功: {}", exception.getMessage());

            // 验证 Redis 中仍然存在验证码
            String redisKey = "email:verification:" + testEmail;
            assertTrue(redisTemplate.hasKey(redisKey), "冷却期间 Redis 中应该仍存在验证码");

        } catch (Exception e) {
            log.error("测试失败", e);
            fail("测试失败: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("集成测试 - 验证正确的验证码")
    void testVerifyCode_CorrectCode_RealRedis() {
        // Given: 准备测试数据
        String testEmail = "verify-correct@flashlack.cn";
        String testCode = "123456";
        testEmails.add(testEmail);

        try {
            // 直接保存验证码到 Redis
            VerificationCodeUtils.saveCode(testEmail, testCode);
            log.info("验证正确的验证码 :验证码已保存到 Redis: email={}, code={}", testEmail, testCode);

            // When: 验证正确的验证码
            boolean result = emailService.verifyCode(testEmail, testCode);

            // Then: 应该返回 true
            assertTrue(result, "正确的验证码应该验证成功");

            // 验证成功后，Redis 中的验证码应该被自动删除
            String redisKey = "email:verification:" + testEmail;
            assertFalse(redisTemplate.hasKey(redisKey), "验证成功后 Redis 中的验证码应该被删除");
            log.info("验证码验证成功，Redis 中的验证码已自动删除");

        } catch (Exception e) {
            log.error("测试失败", e);
            fail("测试失败: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("集成测试 - 验证错误的验证码")
    void testVerifyCode_WrongCode_RealRedis() {
        // Given: 准备测试数据
        String testEmail = "verify-wrong@flashlack.cn";
        String correctCode = "123456";
        String wrongCode = "654321";
        testEmails.add(testEmail);

        try {
            // 保存验证码到 Redis
            VerificationCodeUtils.saveCode(testEmail, correctCode);
            log.info("验证错误的验证码 :验证码已保存到 Redis: email={}, code={}", testEmail, correctCode);

            // When: 验证错误的验证码
            boolean result = emailService.verifyCode(testEmail, wrongCode);

            // Then: 应该返回 false
            assertFalse(result, "错误的验证码应该验证失败");

            // 验证失败后，Redis 中的验证码应该仍然存在
            String redisKey = "email:verification:" + testEmail;
            assertTrue(redisTemplate.hasKey(redisKey), "验证失败后 Redis 中的验证码应该仍然存在");
            log.info("验证码验证失败（预期行为），Redis 中的验证码保留");

        } catch (Exception e) {
            log.error("测试失败", e);
            fail("测试失败: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("集成测试 - 验证码过期后无法验证")
    void testVerifyCode_ExpiredCode_RealRedis() {
        // Given: 准备测试数据
        String testEmail = "verify-expired@flashlack.cn";
        String testCode = "123456";
        testEmails.add(testEmail);

        try {
            // 保存验证码到 Redis
            VerificationCodeUtils.saveCode(testEmail, testCode);
            log.info("验证码已保存到 Redis: email={}, code={}", testEmail, testCode);

            // 手动删除 Redis 中的验证码（模拟过期）
            String redisKey = "email:verification:" + testEmail;
            redisTemplate.delete(redisKey);
            log.info("模拟验证码过期，已删除 Redis key: {}", redisKey);

            // When: 验证过期的验证码
            boolean result = emailService.verifyCode(testEmail, testCode);

            // Then: 应该返回 false
            assertFalse(result, "过期的验证码应该验证失败");
            log.info("过期验证码验证失败（预期行为）");

        } catch (Exception e) {
            log.error("测试失败", e);
            fail("测试失败: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("集成测试 - 验证Redis TTL设置")
    void testVerificationCode_CheckRedisTTL() {
        // Given: 准备测试邮箱
        String testEmail = "ttl-test@flashlac.cn";
        testEmails.add(testEmail);

        try {
            // When: 发送验证码
            emailService.sendVerificationCodeHtml(testEmail);

            // Then: 检查 Redis TTL
            String redisKey = "email:verification:" + testEmail;
            Long ttl = redisTemplate.getExpire(redisKey);
            assertNotNull(ttl, "TTL 不应为 null");
            assertTrue(ttl > 0 && ttl <= 300, "TTL 应该在 0-300 秒之间（5分钟），实际: " + ttl + "秒");
            log.info("Redis TTL 验证成功: {}秒", ttl);

        } catch (Exception e) {
            log.error("测试失败", e);
            fail("测试失败: " + e.getMessage());
        }
    }
}
