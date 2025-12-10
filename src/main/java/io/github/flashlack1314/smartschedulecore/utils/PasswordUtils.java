package io.github.flashlack1314.smartschedulecore.utils;

import cn.hutool.crypto.digest.BCrypt;
import lombok.extern.slf4j.Slf4j;

/**
 * 密码工具类
 * 提供密码加密和验证功能，基于Hutool的BCrypt算法
 *
 * @author flash
 */
@Slf4j
public class PasswordUtils {

    /**
     * 私有构造函数，防止实例化
     */
    private PasswordUtils() {
        throw new UnsupportedOperationException("工具类不允许实例化");
    }

    /**
     * 加密密码
     * 使用BCrypt算法对明文密码进行加密
     *
     * @param plainPassword 明文密码
     * @return 加密后的密码哈希值
     * @throws IllegalArgumentException 当明文密码为空或null时抛出异常
     */
    public static String encrypt(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }

        try {
            String hashedPassword = BCrypt.hashpw(plainPassword.trim());
            log.debug("密码加密成功");
            return hashedPassword;
        } catch (Exception e) {
            log.error("密码加密失败", e);
            throw new RuntimeException("密码加密失败: " + e.getMessage(), e);
        }
    }

    /**
     * 验证密码
     * 验证明文密码是否与加密的密码哈希值匹配
     *
     * @param plainPassword  明文密码
     * @param hashedPassword 加密的密码哈希值
     * @return 如果密码匹配返回true，否则返回false
     */
    public static boolean verify(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }

        try {
            boolean result = BCrypt.checkpw(plainPassword.trim(), hashedPassword);
            log.debug("密码验证结果: {}", result);
            return result;
        } catch (Exception e) {
            log.error("密码验证失败", e);
            return false;
        }
    }
}