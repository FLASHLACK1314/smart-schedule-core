package io.github.flashlack1314.smartschedulecore.utils;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class VerificationCodeUtils {

    /**
     * 验证码重发间隔（秒）
     */
    private static final long RESEND_INTERVAL_SECONDS = 60;

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
    private static RedisTemplate<String, Object> staticRedisTemplate;

    /**
     * 生成6位数字验证码
     * 使用Hutool的RandomUtil工具生成
     *
     * @return 6位数字验证码字符串
     */
    public static String generateCode() {
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
    public static void saveCode(String email, String code) {
        String key = REDIS_KEY_PREFIX + email;
        staticRedisTemplate.opsForValue().set(key, code, Duration.ofMinutes(CODE_EXPIRE_MINUTES));
        log.info("验证码已保存到Redis, email: {}, 有效期: {}分钟", email, CODE_EXPIRE_MINUTES);
    }

    /**
     * 检查是否可以发送验证码
     * 规则：距离上次发送必须超过1分钟
     *
     * @param email 邮箱地址
     * @return true-可以发送，false-不能发送（1分钟内已发送过）
     */
    public static boolean canSendCode(String email) {
        String key = REDIS_KEY_PREFIX + email;
        long expireTime = staticRedisTemplate.getExpire(key);

        // 如果key不存在（expireTime为-2）或已过期，可以发送
        if (expireTime < 0) {
            log.debug("验证码不存在或已过期，可以发送: email={}", email);
            return true;
        }

        // 计算距离上次发送的时间
        long timeSinceLastSend = (CODE_EXPIRE_MINUTES * 60) - expireTime;

        // 如果距离上次发送超过60秒，可以发送
        boolean canSend = timeSinceLastSend >= RESEND_INTERVAL_SECONDS;
        log.debug("检查是否可以发送验证码: email={}, 距上次发送={}秒, 可发送={}",
                email, timeSinceLastSend, canSend);

        return canSend;
    }

    /**
     * 获取验证码重发剩余冷却时间（秒）
     *
     * @param email 邮箱地址
     * @return 剩余冷却时间（秒），0表示可以立即发送
     */
    public static long getRemainingCooldown(String email) {
        String key = REDIS_KEY_PREFIX + email;
        long expireTime = staticRedisTemplate.getExpire(key);

        // 如果key不存在或已过期，可以立即发送
        if (expireTime < 0) {
            return 0;
        }

        // 计算距离上次发送的时间
        long timeSinceLastSend = (CODE_EXPIRE_MINUTES * 60) - expireTime;

        // 计算剩余冷却时间
        long remainingCooldown = RESEND_INTERVAL_SECONDS - timeSinceLastSend;

        return Math.max(0, remainingCooldown);
    }

    /**
     * 验证验证码是否正确
     *
     * @param email     邮箱地址
     * @param inputCode 用户输入的验证码
     * @return true-验证成功，false-验证失败
     */
    public static boolean verifyCode(String email, String inputCode) {
        if (email == null || inputCode == null) {
            log.warn("验证码验证失败: 邮箱或验证码为空");
            return false;
        }

        String key = REDIS_KEY_PREFIX + email;
        Object storedCode = staticRedisTemplate.opsForValue().get(key);

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
    public static void removeCode(String email) {
        String key = REDIS_KEY_PREFIX + email;
        staticRedisTemplate.delete(key);
        log.debug("验证码已删除, email: {}", email);
    }

    /**
     * 检查验证码是否存在（未过期）
     *
     * @param email 邮箱地址
     * @return true-存在，false-不存在
     */
    public static boolean isCodeExists(String email) {
        String key = REDIS_KEY_PREFIX + email;
        return staticRedisTemplate.hasKey(key);
    }

    /**
     * 获取验证码剩余有效时间（秒）
     *
     * @param email 邮箱地址
     * @return 剩余有效时间（秒），如果不存在则返回-1
     */
    public static long getCodeExpireTime(String email) {
        String key = REDIS_KEY_PREFIX + email;
        return staticRedisTemplate.getExpire(key);
    }

    /**
     * 通过setter注入RedisTemplate并赋值给静态字段
     */
    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        staticRedisTemplate = redisTemplate;
    }
}
