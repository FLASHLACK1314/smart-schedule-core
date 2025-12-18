package io.github.flashlack1314.smartschedulecore.services;

/**
 * Token服务接口
 * 负责用户token的生成、验证、存储和清理
 *
 * @author flash
 */
public interface TokenService {

    // === 核心Token管理方法 ===

    /**
     * 生成用户token（单token模式）
     * 新登录会自动使旧token失效
     *
     * @param userUuid 用户UUID
     * @return 生成的token字符串
     */
    String generateToken(String userUuid);

    /**
     * 通过token验证并获取用户信息
     * 支持反向查找，只需要token即可获取userUuid
     *
     * @param token 待验证的token
     * @return 用户UUID，如果token无效返回null
     */
    String validateAndGetUser(String token);

    /**
     * 兼容性验证方法
     * 支持新旧两种token模式，用于平滑迁移
     *
     * @param token    待验证的token
     * @param userUuid 用户UUID（可为null）
     * @return 用户UUID，验证失败返回null
     */
    String validateTokenCompatible(String token, String userUuid);

    /**
     * 删除用户的所有token（用于退出登录）
     *
     * @param userUuid 用户UUID
     * @return true-删除成功，false-删除失败
     */
    boolean removeAllTokens(String userUuid);

    // === 扩展管理方法 ===

    /**
     * 刷新token过期时间
     *
     * @param userUuid 用户UUID
     * @return true-刷新成功，false-刷新失败
     */
    boolean refreshToken(String userUuid);

    /**
     * 强制用户下线
     *
     * @param userUuid 用户UUID
     * @return true-成功，false-失败
     */
    boolean forceLogout(String userUuid);

    /**
     * 检查用户是否在线
     *
     * @param userUuid 用户UUID
     * @return true-在线，false-离线
     */
    boolean isUserOnline(String userUuid);

    /**
     * 获取在线用户数量
     *
     * @return 在线用户总数
     */
    long getOnlineUserCount();
}