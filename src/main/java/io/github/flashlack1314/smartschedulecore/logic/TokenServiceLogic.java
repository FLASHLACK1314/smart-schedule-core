package io.github.flashlack1314.smartschedulecore.logic;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import io.github.flashlack1314.smartschedulecore.constants.StringConstant;
import io.github.flashlack1314.smartschedulecore.exceptions.BusinessException;
import io.github.flashlack1314.smartschedulecore.exceptions.ErrorCode;
import io.github.flashlack1314.smartschedulecore.services.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Token服务实现类
 * 负责用户token的生成、存储、验证和清理的具体实现
 * 包含完整的Redis异常处理和健康检查功能
 *
 * @author flash
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenServiceLogic implements TokenService {

    /**
     * Token过期时间：23小时
     */
    private static final long TOKEN_EXPIRE_HOURS = 23;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public String generateToken(String userUuid) {
        try {
            // 1. 先获取并删除用户的旧token（如果有）
            String oldToken = getOldToken(userUuid);
            if (oldToken != null) {
                removeOldToken(userUuid, oldToken);
            }

            // 2. 生成新token
            String rawToken = IdUtil.fastSimpleUUID() + System.currentTimeMillis();
            String token = SecureUtil.md5(rawToken);

            // 3. 双向存储token
            saveBidirectionalToken(userUuid, token);

            log.info("生成用户token成功: userUuid={}, token={}", userUuid, token);
            return token;
        } catch (RedisConnectionFailureException e) {
            log.error("Redis连接失败，token生成失败: userUuid={}", userUuid, e);
            throw new BusinessException("系统缓存服务异常，请稍后重试", ErrorCode.INTERNAL_ERROR);
        } catch (QueryTimeoutException e) {
            log.error("Redis查询超时，token生成失败: userUuid={}", userUuid, e);
            throw new BusinessException("系统缓存服务超时，请稍后重试", ErrorCode.INTERNAL_ERROR);
        } catch (Exception e) {
            log.error("Token生成发生未知错误: userUuid={}", userUuid, e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }
    }

    @Override
    public String validateAndGetUser(String token) {
        try {
            String redisKey = StringConstant.Redis.TOKEN_USER_PREFIX + token;
            Object userUuid = redisTemplate.opsForValue().get(redisKey);

            if (userUuid != null) {
                log.debug("Token验证成功: token={}, userUuid={}", token, userUuid);
                return userUuid.toString();
            }

            log.debug("Token验证失败: token={}", token);
            return null;
        } catch (RedisConnectionFailureException e) {
            log.error("Redis连接失败，token验证失败: token={}", token, e);
            throw new BusinessException("系统缓存服务异常，请稍后重试", ErrorCode.INTERNAL_ERROR);
        } catch (Exception e) {
            log.error("Token验证发生未知错误: token={}", token, e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }
    }

    @Override
    public String validateTokenCompatible(String token, String userUuid) {
        try {
            // 1. 尝试新模式验证（反向查找）
            String foundUserUuid = validateAndGetUser(token);
            if (foundUserUuid != null) {
                return foundUserUuid;
            }

            // 2. 尝试旧模式验证（需要userUuid）
            if (userUuid != null && verifyToken(userUuid, token)) {
                // 迁移到新模式
                log.info("迁移旧token到新模式: userUuid={}, token={}", userUuid, token);
                removeOldToken(userUuid, null);
                saveBidirectionalToken(userUuid, token);
                return userUuid;
            }

            return null;
        } catch (Exception e) {
            log.error("兼容性验证发生错误: token={}, userUuid={}", token, userUuid, e);
            throw new BusinessException("系统缓存服务异常，请稍后重试", ErrorCode.INTERNAL_ERROR);
        }
    }

    @Override
    public boolean removeAllTokens(String userUuid) {
        try {
            // 1. 获取用户当前的token
            String token = getOldToken(userUuid);

            if (token != null) {
                // 2. 删除反向映射
                String tokenUserKey = StringConstant.Redis.TOKEN_USER_PREFIX + token;
                redisTemplate.delete(tokenUserKey);
            }

            // 3. 删除用户到token的映射
            String userTokenKey = StringConstant.Redis.USER_TOKEN_PREFIX + userUuid;
            boolean tokenDeleted = redisTemplate.delete(userTokenKey);

            // 4. 删除登录状态
            String statusKey = StringConstant.Redis.USER_LOGIN_STATUS_PREFIX + userUuid;
            boolean statusDeleted = redisTemplate.delete(statusKey);

            boolean success = tokenDeleted || statusDeleted;

            log.info("删除用户所有token: userUuid={}, token={}, success={}", userUuid, token, success);
            return success;
        } catch (RedisConnectionFailureException e) {
            log.error("Redis连接失败，删除token失败: userUuid={}", userUuid, e);
            throw new BusinessException("系统缓存服务异常，请稍后重试", ErrorCode.INTERNAL_ERROR);
        } catch (Exception e) {
            log.error("删除token发生未知错误: userUuid={}", userUuid, e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }
    }

    @Override
    public boolean refreshToken(String userUuid) {
        try {
            String redisKey = StringConstant.Redis.USER_TOKEN_PREFIX + userUuid;

            Boolean exists = redisTemplate.hasKey(redisKey);
            if (!exists) {
                log.warn("刷新token失败: key不存在, userUuid={}", userUuid);
                return false;
            }

            boolean success = redisTemplate.expire(redisKey, TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);

            log.debug("刷新token过期时间: userUuid={}, success={}", userUuid, success);
            return success;
        } catch (RedisConnectionFailureException e) {
            log.error("Redis连接失败，刷新token失败: userUuid={}", userUuid, e);
            throw new BusinessException("系统缓存服务异常，请稍后重试", ErrorCode.INTERNAL_ERROR);
        } catch (Exception e) {
            log.error("刷新token发生未知错误: userUuid={}", userUuid, e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }
    }

    @Override
    public boolean forceLogout(String userUuid) {
        try {
            String token = getOldToken(userUuid);
            if (token != null) {
                return removeAllTokens(userUuid);
            }
            return true;
        } catch (Exception e) {
            log.error("强制用户下线发生错误: userUuid={}", userUuid, e);
            throw new BusinessException("系统缓存服务异常，请稍后重试", ErrorCode.INTERNAL_ERROR);
        }
    }

    @Override
    public boolean isUserOnline(String userUuid) {
        try {
            String redisKey = StringConstant.Redis.USER_TOKEN_PREFIX + userUuid;
            return redisTemplate.hasKey(redisKey);
        } catch (RedisConnectionFailureException e) {
            log.error("Redis连接失败，检查用户在线状态失败: userUuid={}", userUuid, e);
            // 连接失败时返回false，不影响主要业务流程
            return false;
        } catch (Exception e) {
            log.error("检查用户在线状态发生未知错误: userUuid={}", userUuid, e);
            return false;
        }
    }

    @Override
    public long getOnlineUserCount() {
        try {
            Set<String> keys = redisTemplate.keys(StringConstant.Redis.USER_TOKEN_PREFIX + "*");
            return keys.size();
        } catch (RedisConnectionFailureException e) {
            log.error("Redis连接失败，获取在线用户数量失败", e);
            return 0;
        } catch (Exception e) {
            log.error("获取在线用户数量发生未知错误", e);
            return 0;
        }
    }

    /**
     * Redis健康检查
     * 检查Redis连接是否正常
     *
     * @return true-连接正常，false-连接异常
     */
    public boolean isRedisHealthy() {
        try {
            redisTemplate.opsForValue().set("health:check", "OK", 5, TimeUnit.SECONDS);
            return "OK".equals(redisTemplate.opsForValue().get("health:check"));
        } catch (Exception e) {
            log.warn("Redis健康检查失败", e);
            return false;
        }
    }

    // === 私有辅助方法 ===

    /**
     * 双向存储token
     */
    private void saveBidirectionalToken(String userUuid, String token) {
        try {
            // 1. 存储 用户到token的映射（单token）
            String userTokenKey = StringConstant.Redis.USER_TOKEN_PREFIX + userUuid;
            redisTemplate.opsForValue().set(userTokenKey, token, TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);

            // 2. 存储token到用户的映射（反向查找）
            String tokenUserKey = StringConstant.Redis.TOKEN_USER_PREFIX + token;
            redisTemplate.opsForValue().set(tokenUserKey, userUuid, TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);

            // 3. 更新登录状态
            updateUserLoginStatus(userUuid);

            log.debug("双向存储token: userUuid={}, token={}", userUuid, token);
        } catch (Exception e) {
            log.error("双向存储token失败: userUuid={}, token={}", userUuid, token, e);
            throw new BusinessException("系统缓存服务异常，请稍后重试", ErrorCode.INTERNAL_ERROR);
        }
    }

    /**
     * 获取用户的旧token
     */
    private String getOldToken(String userUuid) {
        try {
            String userTokenKey = StringConstant.Redis.USER_TOKEN_PREFIX + userUuid;
            Object token = redisTemplate.opsForValue().get(userTokenKey);
            return token != null ? token.toString() : null;
        } catch (Exception e) {
            log.debug("获取旧token失败: userUuid={}", userUuid, e);
            return null;
        }
    }

    /**
     * 清理旧token
     */
    private void removeOldToken(String userUuid, String oldToken) {
        try {
            if (oldToken != null) {
                String tokenUserKey = StringConstant.Redis.TOKEN_USER_PREFIX + oldToken;
                redisTemplate.delete(tokenUserKey);
                log.info("清理旧token: userUuid={}, oldToken={}", userUuid, oldToken);
            }
        } catch (Exception e) {
            log.warn("清理旧token失败: userUuid={}, oldToken={}", userUuid, oldToken, e);
        }
    }

    /**
     * 更新用户登录状态
     */
    private void updateUserLoginStatus(String userUuid) {
        try {
            String statusKey = StringConstant.Redis.USER_LOGIN_STATUS_PREFIX + userUuid;
            String loginInfo = "loginTime:" + System.currentTimeMillis();
            redisTemplate.opsForValue().set(statusKey, loginInfo, TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);
        } catch (Exception e) {
            log.warn("更新用户登录状态失败: userUuid={}", userUuid, e);
        }
    }

    /**
     * 验证token是否有效（旧模式兼容）
     */
    private boolean verifyToken(String userUuid, String token) {
        try {
            String redisKey = StringConstant.Redis.USER_TOKEN_PREFIX + userUuid;
            Object storedToken = redisTemplate.opsForValue().get(redisKey);
            return storedToken != null && storedToken.toString().equals(token);
        } catch (Exception e) {
            log.debug("验证token失败: userUuid={}, token={}", userUuid, token, e);
            return false;
        }
    }
}