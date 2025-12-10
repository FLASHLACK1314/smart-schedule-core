package io.github.flashlack1314.smartschedulecore.config;

import io.github.flashlack1314.smartschedulecore.daos.RoleDAO;
import io.github.flashlack1314.smartschedulecore.daos.UserDAO;
import io.github.flashlack1314.smartschedulecore.models.entity.RoleDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 基础数据初始化算法单元测试
 * 验证用户初始化功能正常工作
 *
 * @author flash
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("基础数据初始化算法测试")
class BasicDataInitializationAlgorithmTest {

    @Mock
    private RoleDAO roleDAO;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private BasicDataInitializationAlgorithm algorithm;

    private RoleDO adminRole;
    private RoleDO teacherRole;
    private RoleDO studentRole;
    private RoleDO academicRole;

    @BeforeEach
    void setUp() {
        // 准备测试角色数据
        adminRole = createRole("admin-uuid-001", "管理员", "admin");
        teacherRole = createRole("teacher-uuid-002", "教师", "teacher");
        studentRole = createRole("student-uuid-003", "学生", "student");
        academicRole = createRole("academic-uuid-004", "教务处老师", "academic");
    }

    @Test
    @DisplayName("正常流程 - 所有角色都存在时创建用户")
    void testExecuteUserInitialization_AllRolesExist() {
        // 模拟所有角色都存在
        when(roleDAO.selectByRoleNameEn("admin")).thenReturn(adminRole);
        when(roleDAO.selectByRoleNameEn("teacher")).thenReturn(teacherRole);
        when(roleDAO.selectByRoleNameEn("student")).thenReturn(studentRole);
        when(roleDAO.selectByRoleNameEn("academic")).thenReturn(academicRole);

        // 执行用户初始化
        algorithm.executeUserInitialization();

        // 验证所有角色查询都被调用
        verify(roleDAO, times(1)).selectByRoleNameEn("admin");
        verify(roleDAO, times(1)).selectByRoleNameEn("teacher");
        verify(roleDAO, times(1)).selectByRoleNameEn("student");
        verify(roleDAO, times(1)).selectByRoleNameEn("academic");

        // 验证用户初始化方法被调用，并传入正确的角色UUID
        verify(userDAO, times(1)).initAllDefaultUsers(
                eq("admin-uuid-001"),
                eq("teacher-uuid-002"),
                eq("student-uuid-003"),
                eq("academic-uuid-004")
        );
    }

    @Test
    @DisplayName("部分角色存在 - 只有管理员和学生角色")
    void testExecuteUserInitialization_PartialRolesExist() {
        // 模拟只有部分角色存在
        when(roleDAO.selectByRoleNameEn("admin")).thenReturn(adminRole);
        when(roleDAO.selectByRoleNameEn("teacher")).thenReturn(null);
        when(roleDAO.selectByRoleNameEn("student")).thenReturn(studentRole);
        when(roleDAO.selectByRoleNameEn("academic")).thenReturn(null);

        // 执行用户初始化
        algorithm.executeUserInitialization();

        // 验证用户初始化方法被调用，并传入正确的角色UUID（部分为null）
        verify(userDAO, times(1)).initAllDefaultUsers(
                eq("admin-uuid-001"),
                eq(null),
                eq("student-uuid-003"),
                eq(null)
        );
    }

    @Test
    @DisplayName("无角色存在 - 所有角色都为null")
    void testExecuteUserInitialization_NoRolesExist() {
        // 模拟所有角色都不存在
        when(roleDAO.selectByRoleNameEn(anyString())).thenReturn(null);

        // 执行用户初始化
        algorithm.executeUserInitialization();

        // 验证用户初始化方法被调用，所有参数都为null
        verify(userDAO, times(1)).initAllDefaultUsers(
                eq(null),
                eq(null),
                eq(null),
                eq(null)
        );
    }

    @Test
    @DisplayName("角色初始化测试")
    void testExecuteRoleInitialization() {
        // 执行角色初始化
        algorithm.executeRoleInitialization();

        // 验证角色初始化方法被调用
        verify(roleDAO, times(1)).initAllBasicRoles();
    }

    @Test
    @DisplayName("数据库异常处理 - 角色查询异常")
    void testExecuteUserInitialization_RoleQueryException() {
        // 模拟角色查询抛出异常
        when(roleDAO.selectByRoleNameEn("admin"))
                .thenThrow(new RuntimeException("数据库连接失败"));

        // 执行用户初始化并验证异常向上传播
        assertThrows(RuntimeException.class, () -> {
            algorithm.executeUserInitialization();
        });

        // 验证用户初始化方法不会被调用（因为异常导致提前终止）
        verify(userDAO, never()).initAllDefaultUsers(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("数据库异常处理 - 用户创建异常")
    void testExecuteUserInitialization_UserCreationException() {
        // 模拟所有角色都存在
        when(roleDAO.selectByRoleNameEn("admin")).thenReturn(adminRole);
        when(roleDAO.selectByRoleNameEn("teacher")).thenReturn(teacherRole);
        when(roleDAO.selectByRoleNameEn("student")).thenReturn(studentRole);
        when(roleDAO.selectByRoleNameEn("academic")).thenReturn(academicRole);

        // 模拟用户创建抛出异常
        doThrow(new RuntimeException("用户创建失败"))
                .when(userDAO).initAllDefaultUsers(anyString(), anyString(), anyString(), anyString());

        // 执行用户初始化并验证异常向上传播
        assertThrows(RuntimeException.class, () -> {
            algorithm.executeUserInitialization();
        });
    }

    /**
     * 创建测试用的角色实体
     */
    private RoleDO createRole(String uuid, String name, String nameEn) {
        RoleDO role = new RoleDO();
        role.setRoleUuid(uuid);
        role.setRoleName(name);
        role.setRoleNameEn(nameEn);
        role.setRolePermissions("测试权限");
        return role;
    }
}