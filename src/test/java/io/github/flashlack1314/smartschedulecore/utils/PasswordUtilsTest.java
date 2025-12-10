package io.github.flashlack1314.smartschedulecore.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PasswordUtils工具类单元测试
 * 验证密码加密和验证的核心功能
 *
 * @author flash
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordUtils工具类测试")
class PasswordUtilsTest {

    private String plainPassword;
    private String hashedPassword;

    @BeforeEach
    void setUp() {
        plainPassword = "123456";
        hashedPassword = PasswordUtils.encrypt(plainPassword);
    }

    @Test
    @DisplayName("密码加密功能测试")
    void testEncrypt() {
        // 测试正常密码加密
        String result = PasswordUtils.encrypt(plainPassword);

        assertNotNull(result);
        assertNotEquals(plainPassword, result);
        assertTrue(result.length() >= 60); // BCrypt哈希通常60个字符
    }

    @Test
    @DisplayName("密码加密异常测试 - 空密码")
    void testEncryptWithEmptyPassword() {
        // 测试null密码
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordUtils.encrypt(null);
        });

        // 测试空字符串密码
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordUtils.encrypt("");
        });

        // 测试只包含空格的密码
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordUtils.encrypt("   ");
        });
    }

    @Test
    @DisplayName("密码验证功能测试")
    void testVerify() {
        // 测试正确密码验证
        assertTrue(PasswordUtils.verify(plainPassword, hashedPassword));

        // 测试错误密码验证
        assertFalse(PasswordUtils.verify("wrongpassword", hashedPassword));

        // 测试null参数
        assertFalse(PasswordUtils.verify(null, hashedPassword));
        assertFalse(PasswordUtils.verify(plainPassword, null));
        assertFalse(PasswordUtils.verify(null, null));

        // 测试空字符串参数
        assertFalse(PasswordUtils.verify("", hashedPassword));
        assertFalse(PasswordUtils.verify(plainPassword, ""));
    }

    @Test
    @DisplayName("密码加密一致性测试")
    void testEncryptConsistency() {
        // 同一个密码多次加密应该产生不同的哈希值（因为BCrypt使用随机盐）
        String hash1 = PasswordUtils.encrypt(plainPassword);
        String hash2 = PasswordUtils.encrypt(plainPassword);
        String hash3 = PasswordUtils.encrypt(plainPassword);

        assertNotEquals(hash1, hash2);
        assertNotEquals(hash2, hash3);
        assertNotEquals(hash1, hash3);

        // 但所有哈希值都应该能验证原密码
        assertTrue(PasswordUtils.verify(plainPassword, hash1));
        assertTrue(PasswordUtils.verify(plainPassword, hash2));
        assertTrue(PasswordUtils.verify(plainPassword, hash3));
    }

    @Test
    @DisplayName("密码处理空白字符测试")
    void testPasswordWhitespaceHandling() {
        String passwordWithSpaces = "  123456  ";

        // 加密时应该去除空格
        String encrypted = PasswordUtils.encrypt(passwordWithSpaces);
        assertTrue(PasswordUtils.verify("123456", encrypted));

        // 验证时也处理空格
        assertTrue(PasswordUtils.verify("  123456  ", encrypted));
    }
}