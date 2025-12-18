package io.github.flashlack1314.smartschedulecore.Service;

import cn.hutool.core.util.IdUtil;
import io.github.flashlack1314.smartschedulecore.daos.RoleDAO;
import io.github.flashlack1314.smartschedulecore.daos.UserDAO;
import io.github.flashlack1314.smartschedulecore.exceptions.BusinessException;
import io.github.flashlack1314.smartschedulecore.exceptions.ErrorCode;
import io.github.flashlack1314.smartschedulecore.models.dto.LoginBackDTO;
import io.github.flashlack1314.smartschedulecore.models.entity.RoleDO;
import io.github.flashlack1314.smartschedulecore.models.entity.UserDO;
import io.github.flashlack1314.smartschedulecore.models.vo.LoginVO;
import io.github.flashlack1314.smartschedulecore.services.AuthService;
import io.github.flashlack1314.smartschedulecore.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AuthService集成测试
 * 测试AuthService方法的所有业务逻辑分支
 *
 * @author flash
 */
@Slf4j
@SpringBootTest
@Transactional
@Rollback
class AuthServiceTests {

    private static final String TEST_USER_EMAIL = "test.integration@example.com";
    private static final String TEST_USER_PASSWORD = "Test123456!";
    private static final String WRONG_PASSWORD = "WrongPassword123!";
    private static final String TEST_USER_NAME = "集成测试用户";

    @Autowired
    private AuthService authService;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    // 测试数据
    private String testUserUuid;
    private String testRoleUuid;

    @BeforeEach
    void setUp() {
        // 创建测试数据
        createTestRole();
        createTestUser();

        log.info("测试数据创建完成: userUuid={}, roleUuid={}", testUserUuid, testRoleUuid);
    }

    @AfterEach
    void tearDown() {
        // 事务回滚会自动清理测试数据
        log.info("测试结束，事务自动回滚");
    }

    @Test
    @DisplayName("正常登录成功测试")
    void testSuccessfulLogin() {
        // Given - 准备登录请求数据
        LoginVO loginVO = new LoginVO();
        loginVO.setUserEmail(TEST_USER_EMAIL);
        loginVO.setUserPassword(TEST_USER_PASSWORD);

        // When - 执行登录
        LoginBackDTO result = authService.login(loginVO);

        // Then - 验证返回数据
        assertNotNull(result, "登录结果不应为空");
        assertNotNull(result.getToken(), "Token不应为空");
        assertNotNull(result.getUserInfo(), "用户信息不应为空");

        // 验证Token格式（MD5哈希，32位）
        String token = result.getToken();
        assertEquals(32, token.length(), "Token长度应为32位");
        assertTrue(token.matches("[a-f0-9]{32}"), "Token应为MD5哈希格式");

        // 验证用户信息
        assertEquals(TEST_USER_EMAIL, result.getUserInfo().getUserEmail());
        assertEquals(TEST_USER_NAME, result.getUserInfo().getUserName());
        assertEquals(testUserUuid, result.getUserInfo().getUserUuid());
        assertEquals("test", result.getUserInfo().getRoleNameEn());

        log.info("正常登录成功测试通过: email={}, token={}, role={}",
                result.getUserInfo().getUserEmail(),
                token.substring(0, 8) + "...",
                result.getUserInfo().getRoleNameEn());
    }

    @Test
    @DisplayName("用户不存在测试")
    void testUserNotFound() {
        // Given - 使用不存在的邮箱
        LoginVO loginVO = new LoginVO();
        loginVO.setUserEmail("nonexistent.user@example.com");
        loginVO.setUserPassword(TEST_USER_PASSWORD);

        // When & Then - 验证抛出BusinessException
        BusinessException exception = assertThrows(BusinessException.class, () -> authService.login(loginVO));

        assertEquals(ErrorCode.USER_NOT_FOUND.getCode(), exception.getCode(), "错误码应为1001");
        log.info("用户不存在测试通过: {}", exception.getMessage());
    }

    @Test
    @DisplayName("密码错误测试")
    void testWrongPassword() {
        // Given - 使用正确的邮箱但错误的密码
        LoginVO loginVO = new LoginVO();
        loginVO.setUserEmail(TEST_USER_EMAIL);
        loginVO.setUserPassword(WRONG_PASSWORD);

        // When & Then - 验证抛出BusinessException
        BusinessException exception = assertThrows(BusinessException.class, () -> authService.login(loginVO));

        assertEquals(ErrorCode.PASSWORD_ERROR.getCode(), exception.getCode(), "错误码应为1003");
        log.info("密码错误测试通过: {}", exception.getMessage());
    }

    @Test
    @DisplayName("用户被封禁测试")
    void testUserBanned() {
        // Given - 创建被封禁的测试用户
        UserDO bannedUser = new UserDO();
        bannedUser.setUserUuid(IdUtil.simpleUUID());
        bannedUser.setUserRoleUuid(testRoleUuid);
        bannedUser.setUserSchoolUuid("demo-school-uuid");
        bannedUser.setUserName("被封禁用户");
        bannedUser.setUserEmail("banned.user@example.com");
        bannedUser.setUserPhoneNum("13800138001");
        bannedUser.setUserPassword(PasswordUtils.encrypt(TEST_USER_PASSWORD));
        bannedUser.setBan(true); // 设置封禁状态
        userDAO.save(bannedUser);

        // When - 尝试使用被封禁用户登录
        LoginVO loginVO = new LoginVO();
        loginVO.setUserEmail("banned.user@example.com");
        loginVO.setUserPassword(TEST_USER_PASSWORD);

        // Then - 验证抛出BusinessException
        BusinessException exception = assertThrows(BusinessException.class, () -> authService.login(loginVO));

        assertEquals(ErrorCode.USER_DISABLED.getCode(), exception.getCode(), "错误码应为1002");
        log.info("用户被封禁测试通过: {}", exception.getMessage());
    }

    @Test
    @DisplayName("用户角色不存在测试")
    void testUserRoleNotFound() {
        // Given - 创建没有有效角色的用户
        UserDO userWithoutRole = new UserDO();
        userWithoutRole.setUserUuid(IdUtil.simpleUUID());
        userWithoutRole.setUserRoleUuid("non-existent-role-uuid"); // 不存在的角色UUID
        userWithoutRole.setUserSchoolUuid("demo-school-uuid");
        userWithoutRole.setUserName("无角色用户");
        userWithoutRole.setUserEmail("norole.user@example.com");
        userWithoutRole.setUserPhoneNum("13800138002");
        userWithoutRole.setUserPassword(PasswordUtils.encrypt(TEST_USER_PASSWORD));
        userWithoutRole.setBan(false);
        userDAO.save(userWithoutRole);

        // When - 尝试使用无角色用户登录
        LoginVO loginVO = new LoginVO();
        loginVO.setUserEmail("norole.user@example.com");
        loginVO.setUserPassword(TEST_USER_PASSWORD);

        // Then - 验证抛出BusinessException
        BusinessException exception = assertThrows(BusinessException.class, () -> authService.login(loginVO));

        assertEquals(ErrorCode.USER_NOT_FOUND.getCode(), exception.getCode(), "错误码应为1001");
        log.info("用户角色不存在测试通过: {}", exception.getMessage());
    }

    @Test
    @DisplayName("参数验证测试 - 邮箱为空")
    void testParameterValidation_EmailEmpty() {
        // Given - 邮箱为空的登录请求
        LoginVO loginVO = new LoginVO();
        loginVO.setUserEmail("");
        loginVO.setUserPassword(TEST_USER_PASSWORD);

        // When & Then - 验证参数检查失败
        BusinessException exception = assertThrows(BusinessException.class, () -> authService.login(loginVO));

        // 空邮箱会走到用户不存在的逻辑
        assertEquals(ErrorCode.USER_NOT_FOUND.getCode(), exception.getCode(), "错误码应为1001");
        log.info("邮箱为空参数验证测试通过");
    }

    @Test
    @DisplayName("参数验证测试 - 密码为空")
    void testParameterValidation_PasswordEmpty() {
        // Given - 密码为空的登录请求
        LoginVO loginVO = new LoginVO();
        loginVO.setUserEmail(TEST_USER_EMAIL);
        loginVO.setUserPassword("");

        // When & Then - 验证参数检查失败
        BusinessException exception = assertThrows(BusinessException.class, () -> authService.login(loginVO));

        // 空密码会走到密码错误的逻辑
        assertEquals(ErrorCode.PASSWORD_ERROR.getCode(), exception.getCode(), "错误码应为1003");
        log.info("密码为空参数验证测试通过");
    }

    // === 私有辅助方法 ===

    /**
     * 创建测试角色
     */
    private void createTestRole() {
        RoleDO testRole = new RoleDO();
        testRole.setRoleUuid(IdUtil.simpleUUID());
        testRole.setRoleName("测试角色");
        testRole.setRoleNameEn("test");
        testRole.setRolePermissions("测试权限");
        roleDAO.save(testRole);

        this.testRoleUuid = testRole.getRoleUuid();
        log.debug("创建测试角色: {}", testRoleUuid);
    }

    /**
     * 创建测试用户
     */
    private void createTestUser() {
        UserDO testUser = new UserDO();
        testUser.setUserUuid(IdUtil.simpleUUID());
        testUser.setUserRoleUuid(testRoleUuid);
        testUser.setUserSchoolUuid("demo-school-uuid");
        testUser.setUserName(TEST_USER_NAME);
        testUser.setUserEmail(TEST_USER_EMAIL);
        testUser.setUserPhoneNum("13800138000"); // 添加必需的手机号
        testUser.setUserPassword(PasswordUtils.encrypt(TEST_USER_PASSWORD));
        testUser.setBan(false);
        userDAO.save(testUser);

        this.testUserUuid = testUser.getUserUuid();
        log.debug("创建测试用户: {}", testUserUuid);
    }
}