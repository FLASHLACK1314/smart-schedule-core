package io.github.flashlack1314.smartschedulecore.config;

import io.github.flashlack1314.smartschedulecore.daos.RoleDAO;
import io.github.flashlack1314.smartschedulecore.daos.UserDAO;
import io.github.flashlack1314.smartschedulecore.models.entity.RoleDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 基础数据初始化算法类
 * 负责具体的基础数据初始化算法实现
 *
 * @author flash
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BasicDataInitializationAlgorithm {

    private final RoleDAO roleDAO;
    private final UserDAO userDAO;


    /**
     * 执行角色初始化
     */
    public void executeRoleInitialization() {
        // 执行管理员角色创建
        roleDAO.initAllBasicRoles();
    }

    /**
     * 执行用户初始化
     */
    public void executeUserInitialization() {
        // 获取角色信息
        RoleDO adminRole = roleDAO.selectByRoleNameEn("admin");
        RoleDO teacherRole = roleDAO.selectByRoleNameEn("teacher");
        RoleDO studentRole = roleDAO.selectByRoleNameEn("student");
        RoleDO academicRole = roleDAO.selectByRoleNameEn("academic");

        // 执行默认用户创建
        userDAO.initAllDefaultUsers(
                adminRole != null ? adminRole.getRoleUuid() : null,
                teacherRole != null ? teacherRole.getRoleUuid() : null,
                studentRole != null ? studentRole.getRoleUuid() : null,
                academicRole != null ? academicRole.getRoleUuid() : null
        );
    }


}