package io.github.flashlack1314.smartschedulecore.utils;

import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 验证码工具类
 * 用于生成、存储、验证邮箱验证码
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class VerificationCodeUtils {

    /**
     * 验证码长度
     */
    private static final int CODE_LENGTH = 6;
    /**
     * 验证码有效期（分钟）
     */
    private static final long CODE_EXPIRE_MINUTES = 5;
    /**
     * Redis key前缀
     */
    private static final String REDIS_KEY_PREFIX = "email:verification:";
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 生成6位数字验证码
     * 使用Hutool的RandomUtil工具生成
     *
     * @return 6位数字验证码字符串
     */
    public String generateCode() {
        String verificationCode = RandomUtil.randomNumbers(CODE_LENGTH);
        log.debug("生成验证码: {}", verificationCode);
        return verificationCode;
    }

    /**
     * 保存验证码到Redis
     *
     * @param email 邮箱地址
     * @param code  验证码
     */
    public void saveCode(String email, String code) {
        String key = REDIS_KEY_PREFIX + email;
        redisTemplate.opsForValue().set(key, code, Duration.ofMinutes(CODE_EXPIRE_MINUTES));
        log.info("验证码已保存到Redis, email: {}, 有效期: {}分钟", email, CODE_EXPIRE_MINUTES);
    }

    /**
     * 验证验证码是否正确
     *
     * @param email     邮箱地址
     * @param inputCode 用户输入的验证码
     * @return true-验证成功，false-验证失败
     */
    public boolean verifyCode(String email, String inputCode) {
        if (email == null || inputCode == null) {
            log.warn("验证码验证失败: 邮箱或验证码为空");
            return false;
        }

        String key = REDIS_KEY_PREFIX + email;
        Object storedCode = redisTemplate.opsForValue().get(key);

        if (storedCode == null) {
            log.warn("验证码验证失败: 验证码不存在或已过期, email: {}", email);
            return false;
        }

        boolean isValid = storedCode.toString().equals(inputCode);
        if (isValid) {
            log.info("验证码验证成功, email: {}", email);
            // 验证成功后删除验证码
            removeCode(email);
        } else {
            log.warn("验证码验证失败: 验证码不匹配, email: {}", email);
        }

        return isValid;
    }

    /**
     * 删除验证码
     *
     * @param email 邮箱地址
     */
    public void removeCode(String email) {
        String key = REDIS_KEY_PREFIX + email;
        redisTemplate.delete(key);
        log.debug("验证码已删除, email: {}", email);
    }

    /**
     * 检查验证码是否存在（未过期）
     *
     * @param email 邮箱地址
     * @return true-存在，false-不存在
     */
    public boolean isCodeExists(String email) {
        String key = REDIS_KEY_PREFIX + email;
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取验证码剩余有效时间（秒）
     *
     * @param email 邮箱地址
     * @return 剩余有效时间（秒），如果不存在则返回-1
     */
    public long getCodeExpireTime(String email) {
        String key = REDIS_KEY_PREFIX + email;
        return redisTemplate.getExpire(key);
    }
}
