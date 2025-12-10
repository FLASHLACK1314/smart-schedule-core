package io.github.flashlack1314.smartschedulecore.config;

import io.github.flashlack1314.smartschedulecore.constants.SystemConstant;
import io.github.flashlack1314.smartschedulecore.daos.RoleDAO;
import io.github.flashlack1314.smartschedulecore.models.entity.RoleDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 系统常量初始化配置类
 * 在数据库初始化完成后，通过查询数据库为系统常量赋值
 *
 * @author flash
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SystemConstantInitializationConfig {

    private final RoleDAO roleDAO;

    /**
     * 系统常量初始化
     * 优先级设置为2，在数据库初始化完成后执行
     */
    @Bean
    @Order(2)
    public ApplicationRunner systemConstantInitializer() {
        return args -> {
            log.info("开始初始化系统常量...");

            try {
                initializeSystemConstants();
                log.info("系统常量初始化完成");
            } catch (Exception e) {
                log.error("系统常量初始化失败", e);
                // 常量初始化失败不影响应用启动
            }
        };
    }

    /**
     * 初始化系统常量
     */
    private void initializeSystemConstants() {
        try {
            // 通过英文名称查询管理员角色
            RoleDO adminRole = roleDAO.selectByRoleNameEn("admin");
            if (adminRole != null) {
                SystemConstant.setRoleAdmin(adminRole.getRoleUuid());
                log.debug("管理员角色常量已初始化: {}", adminRole.getRoleUuid());
            }

            // 通过英文名称查询教师角色
            RoleDO teacherRole = roleDAO.selectByRoleNameEn("teacher");
            if (teacherRole != null) {
                SystemConstant.setRoleTeacher(teacherRole.getRoleUuid());
                log.debug("教师角色常量已初始化: {}", teacherRole.getRoleUuid());
            }

            // 通过英文名称查询学生角色
            RoleDO studentRole = roleDAO.selectByRoleNameEn("student");
            if (studentRole != null) {
                SystemConstant.setRoleStudent(studentRole.getRoleUuid());
                log.debug("学生角色常量已初始化: {}", studentRole.getRoleUuid());
            }

            // 通过英文名称查询教务处角色
            RoleDO academicRole = roleDAO.selectByRoleNameEn("academic");
            if (academicRole != null) {
                SystemConstant.setRoleAcademic(academicRole.getRoleUuid());
                log.debug("教务处角色常量已初始化: {}", academicRole.getRoleUuid());
            }

            log.info("系统常量初始化完成 - 管理员: {}, 教师: {}, 学生: {}, 教务处: {}",
                    SystemConstant.getRoleAdmin(),
                    SystemConstant.getRoleTeacher(),
                    SystemConstant.getRoleStudent(),
                    SystemConstant.getRoleAcademic());

        } catch (Exception e) {
            log.error("系统常量初始化过程中发生异常", e);
            throw e;
        }
    }
}