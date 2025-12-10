package io.github.flashlack1314.smartschedulecore.config;

import io.github.flashlack1314.smartschedulecore.constants.SystemConstant;
import io.github.flashlack1314.smartschedulecore.daos.RoleDAO;
import io.github.flashlack1314.smartschedulecore.models.entity.RoleDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 系统常量初始化配置单元测试
 * 验证系统常量从数据库正确初始化
 *
 * @author flash
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("系统常量初始化测试")
class SystemConstantInitializationConfigTest {

    @Mock
    private RoleDAO roleDAO;

    @InjectMocks
    private SystemConstantInitializationConfig config;

    private ApplicationRunner applicationRunner;

    @BeforeEach
    void setUp() {
        // 在每个测试前重置系统常量
        SystemConstant.setRoleAdmin(null);
        SystemConstant.setRoleTeacher(null);
        SystemConstant.setRoleStudent(null);
        SystemConstant.setRoleAcademic(null);

        // 获取ApplicationRunner实例
        applicationRunner = config.systemConstantInitializer();
    }

    @Test
    @DisplayName("正常流程 - 所有角色都存在")
    void testSystemConstantInitialization_AllRolesExist() throws Exception {
        // 准备测试数据
        RoleDO adminRole = createRole("admin-uuid-001", "管理员", "admin");
        RoleDO teacherRole = createRole("teacher-uuid-002", "教师", "teacher");
        RoleDO studentRole = createRole("student-uuid-003", "学生", "student");
        RoleDO academicRole = createRole("academic-uuid-004", "教务处老师", "academic");

        // 模拟DAO调用
        when(roleDAO.selectByRoleNameEn("admin")).thenReturn(adminRole);
        when(roleDAO.selectByRoleNameEn("teacher")).thenReturn(teacherRole);
        when(roleDAO.selectByRoleNameEn("student")).thenReturn(studentRole);
        when(roleDAO.selectByRoleNameEn("academic")).thenReturn(academicRole);

        // 执行初始化
        applicationRunner.run(mock(ApplicationArguments.class));

        // 验证系统常量被正确设置
        assertEquals("admin-uuid-001", SystemConstant.getRoleAdmin());
        assertEquals("teacher-uuid-002", SystemConstant.getRoleTeacher());
        assertEquals("student-uuid-003", SystemConstant.getRoleStudent());
        assertEquals("academic-uuid-004", SystemConstant.getRoleAcademic());

        // 验证DAO方法被正确调用
        verify(roleDAO, times(1)).selectByRoleNameEn("admin");
        verify(roleDAO, times(1)).selectByRoleNameEn("teacher");
        verify(roleDAO, times(1)).selectByRoleNameEn("student");
        verify(roleDAO, times(1)).selectByRoleNameEn("academic");
    }

    @Test
    @DisplayName("部分初始化 - 只有部分角色存在")
    void testSystemConstantInitialization_PartialRolesExist() throws Exception {
        // 准备测试数据 - 只有管理员和学生角色
        RoleDO adminRole = createRole("admin-uuid-001", "管理员", "admin");
        RoleDO studentRole = createRole("student-uuid-003", "学生", "student");

        // 模拟DAO调用 - 教师和教务处角色返回null
        when(roleDAO.selectByRoleNameEn("admin")).thenReturn(adminRole);
        when(roleDAO.selectByRoleNameEn("teacher")).thenReturn(null);
        when(roleDAO.selectByRoleNameEn("student")).thenReturn(studentRole);
        when(roleDAO.selectByRoleNameEn("academic")).thenReturn(null);

        // 执行初始化
        applicationRunner.run(mock(ApplicationArguments.class));

        // 验证存在的角色被正确设置，不存在的保持null
        assertEquals("admin-uuid-001", SystemConstant.getRoleAdmin());
        assertNull(SystemConstant.getRoleTeacher());
        assertEquals("student-uuid-003", SystemConstant.getRoleStudent());
        assertNull(SystemConstant.getRoleAcademic());

        // 验证所有DAO方法都被调用了
        verify(roleDAO, times(1)).selectByRoleNameEn("admin");
        verify(roleDAO, times(1)).selectByRoleNameEn("teacher");
        verify(roleDAO, times(1)).selectByRoleNameEn("student");
        verify(roleDAO, times(1)).selectByRoleNameEn("academic");
    }

    @Test
    @DisplayName("无角色存在 - 全部为空")
    void testSystemConstantInitialization_NoRolesExist() throws Exception {
        // 模拟所有角色查询都返回null
        when(roleDAO.selectByRoleNameEn(anyString())).thenReturn(null);

        // 执行初始化
        applicationRunner.run(mock(ApplicationArguments.class));

        // 验证所有系统常量都保持null
        assertNull(SystemConstant.getRoleAdmin());
        assertNull(SystemConstant.getRoleTeacher());
        assertNull(SystemConstant.getRoleStudent());
        assertNull(SystemConstant.getRoleAcademic());

        // 验证所有角色查询都被调用
        verify(roleDAO, times(1)).selectByRoleNameEn("admin");
        verify(roleDAO, times(1)).selectByRoleNameEn("teacher");
        verify(roleDAO, times(1)).selectByRoleNameEn("student");
        verify(roleDAO, times(1)).selectByRoleNameEn("academic");
    }

    @Test
    @DisplayName("数据库异常处理")
    void testSystemConstantInitialization_DatabaseException() {
        // 模拟数据库查询抛出异常
        when(roleDAO.selectByRoleNameEn("admin"))
                .thenThrow(new RuntimeException("数据库连接失败"));

        // 执行初始化并验证异常不会向上传播
        assertDoesNotThrow(() -> applicationRunner.run(mock(ApplicationArguments.class)));

        // 验证系统常量保持null状态
        assertNull(SystemConstant.getRoleAdmin());
        assertNull(SystemConstant.getRoleTeacher());
        assertNull(SystemConstant.getRoleStudent());
        assertNull(SystemConstant.getRoleAcademic());
    }

    @Test
    @DisplayName("验证ApplicationRunner Bean创建")
    void testApplicationRunnerBeanCreation() {
        // 验证systemConstantInitializer方法返回ApplicationRunner实例
        assertNotNull(applicationRunner);
        assertInstanceOf(ApplicationRunner.class, applicationRunner);
    }

    @Test
    @DisplayName("重复初始化测试")
    void testSystemConstantInitialization_MultipleInitialization() throws Exception {
        // 准备测试数据
        RoleDO adminRole = createRole("admin-uuid-001", "管理员", "admin");

        when(roleDAO.selectByRoleNameEn("admin")).thenReturn(adminRole);
        when(roleDAO.selectByRoleNameEn("teacher")).thenReturn(null);
        when(roleDAO.selectByRoleNameEn("student")).thenReturn(null);
        when(roleDAO.selectByRoleNameEn("academic")).thenReturn(null);

        // 第一次初始化
        applicationRunner.run(mock(ApplicationArguments.class));
        String firstResult = SystemConstant.getRoleAdmin();

        // 重置常量并第二次初始化
        SystemConstant.setRoleAdmin(null);
        applicationRunner.run(mock(ApplicationArguments.class));
        String secondResult = SystemConstant.getRoleAdmin();

        // 验证两次初始化结果一致
        assertEquals(firstResult, secondResult);
        assertEquals("admin-uuid-001", firstResult);

        // 验证DAO被调用了两次（每次初始化都调用一次）
        verify(roleDAO, times(2)).selectByRoleNameEn("admin");
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