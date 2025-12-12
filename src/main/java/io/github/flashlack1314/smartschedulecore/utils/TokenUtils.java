package io.github.flashlack1314.smartschedulecore.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import io.github.flashlack1314.smartschedulecore.constants.StringConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Token管理工具类
 * 负责用户token的生成、存储、验证和清理
 *
 * @author flash
 */
@Slf4j
@Component
public class TokenUtils {

    /**
     * Token过期时间：23小时
     */
    private static final long TOKEN_EXPIRE_HOURS = 23;
    /**
     * 每个用户最多允许的token数量
     */
    private static final int MAX_TOKEN_COUNT = 2;
    private static RedisTemplate<String, Object> REDIS_TEMPLATE;

    /**
     * 生成用户token
     * 使用UUID + 时间戳 + MD5加密生成唯一token
     *
     * @param userUuid 用户UUID
     * @return 生成的token字符串
     */
    public static String generateToken(String userUuid) {
        // 1. 生成原始token：UUID + 时间戳
        String rawToken = IdUtil.fastSimpleUUID() + System.currentTimeMillis();

        // 2. 使用MD5加密，生成32位token
        String token = SecureUtil.md5(rawToken);

        // 3. 存储到Redis
        saveToken(userUuid, token);

        log.info("生成用户token成功: userUuid={}, token={}", userUuid, token);
        return token;
    }

    /**
     * 保存token到Redis
     * Key格式：user:token:{userUuid}
     * Value：使用Set存储token，最多保留2个
     *
     * @param userUuid 用户UUID
     * @param token    token字符串
     */
    private static void saveToken(String userUuid, String token) {
        String redisKey = StringConstant.Redis.USER_TOKEN_PREFIX + userUuid;

        // 1. 获取当前用户的所有token
        Set<Object> tokens = REDIS_TEMPLATE.opsForSet().members(redisKey);

        // 2. 如果已有2个token，删除最早的一个
        if (tokens != null && tokens.size() >= MAX_TOKEN_COUNT) {
            // 删除第一个token（最早的）
            Object oldestToken = tokens.iterator().next();
            REDIS_TEMPLATE.opsForSet().remove(redisKey, oldestToken);
            log.debug("删除最早的token: userUuid={}, oldToken={}", userUuid, oldestToken);
        }

        // 3. 添加新token到Set
        REDIS_TEMPLATE.opsForSet().add(redisKey, token);

        // 4. 设置过期时间为23小时
        REDIS_TEMPLATE.expire(redisKey, TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);

        log.debug("保存token到Redis: userUuid={}, token={}, expire={}小时",
                userUuid, token, TOKEN_EXPIRE_HOURS);
    }

    /**
     * 验证token是否有效
     *
     * @param userUuid 用户UUID
     * @param token    待验证的token
     * @return true-有效，false-无效
     */
    public boolean verifyToken(String userUuid, String token) {
        String redisKey = StringConstant.Redis.USER_TOKEN_PREFIX + userUuid;

        // 检查token是否存在于用户的token集合中
        Boolean isMember = REDIS_TEMPLATE.opsForSet().isMember(redisKey, token);
        boolean valid = Boolean.TRUE.equals(isMember);

        log.debug("验证token: userUuid={}, token={}, valid={}", userUuid, token, valid);
        return valid;
    }

    /**
     * 刷新token过期时间
     * 将token的过期时间重置为23小时
     *
     * @param userUuid 用户UUID
     * @return true-刷新成功，false-刷新失败
     */
    public boolean refreshToken(String userUuid) {
        String redisKey = StringConstant.Redis.USER_TOKEN_PREFIX + userUuid;

        // 检查key是否存在
        Boolean exists = REDIS_TEMPLATE.hasKey(redisKey);
        if (!exists) {
            log.warn("刷新token失败: key不存在, userUuid={}", userUuid);
            return false;
        }

        // 重置过期时间
        boolean success = REDIS_TEMPLATE.expire(redisKey, TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);

        log.debug("刷新token过期时间: userUuid={}, success={}", userUuid, success);
        return success;
    }

    /**
     * 删除指定token
     *
     * @param userUuid 用户UUID
     * @param token    待删除的token
     * @return true-删除成功，false-删除失败
     */
    public boolean removeToken(String userUuid, String token) {
        String redisKey = StringConstant.Redis.USER_TOKEN_PREFIX + userUuid;

        Long removed = REDIS_TEMPLATE.opsForSet().remove(redisKey, token);
        boolean success = removed != null && removed > 0;

        log.debug("删除token: userUuid={}, token={}, success={}", userUuid, token, success);
        return success;
    }

    /**
     * 删除用户的所有token（用于退出登录）
     *
     * @param userUuid 用户UUID
     * @return true-删除成功，false-删除失败
     */
    public boolean removeAllTokens(String userUuid) {
        String redisKey = StringConstant.Redis.USER_TOKEN_PREFIX + userUuid;

        boolean success = REDIS_TEMPLATE.delete(redisKey);

        log.info("删除用户所有token: userUuid={}, success={}", userUuid, success);
        return success;
    }

    /**
     * 获取用户当前的所有token
     *
     * @param userUuid 用户UUID
     * @return token集合
     */
    public Set<Object> getUserTokens(String userUuid) {
        String redisKey = StringConstant.Redis.USER_TOKEN_PREFIX + userUuid;
        Set<Object> tokens = REDIS_TEMPLATE.opsForSet().members(redisKey);

        log.debug("获取用户所有token: userUuid={}, count={}",
                userUuid, tokens != null ? tokens.size() : 0);
        return tokens;
    }

    /**
     * 获取用户当前token数量
     *
     * @param userUuid 用户UUID
     * @return token数量
     */
    public long getTokenCount(String userUuid) {
        String redisKey = StringConstant.Redis.USER_TOKEN_PREFIX + userUuid;
        Long count = REDIS_TEMPLATE.opsForSet().size(redisKey);

        long tokenCount = count != null ? count : 0;
        log.debug("获取用户token数量: userUuid={}, count={}", userUuid, tokenCount);
        return tokenCount;
    }

    /**
     * 检查用户是否有有效token（是否已登录）
     *
     * @param userUuid 用户UUID
     * @return true-已登录，false-未登录
     */
    public boolean isUserLoggedIn(String userUuid) {
        return getTokenCount(userUuid) > 0;
    }

    /**
     * 通过setter注入RedisTemplate并赋值给静态字段
     * 这是让静态工具类能够使用Spring Bean的标准模式
     *
     * @param redisTemplate Spring配置的RedisTemplate
     */
    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        REDIS_TEMPLATE = redisTemplate;
    }
}
