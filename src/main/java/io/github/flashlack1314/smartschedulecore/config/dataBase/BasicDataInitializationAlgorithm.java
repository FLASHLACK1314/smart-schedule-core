package io.github.flashlack1314.smartschedulecore.config.dataBase;

import cn.hutool.core.util.IdUtil;
import io.github.flashlack1314.smartschedulecore.constants.SystemConstant;
import io.github.flashlack1314.smartschedulecore.daos.RoleDAO;
import io.github.flashlack1314.smartschedulecore.daos.SchoolDAO;
import io.github.flashlack1314.smartschedulecore.daos.UserDAO;
import io.github.flashlack1314.smartschedulecore.models.entity.RoleDO;
import io.github.flashlack1314.smartschedulecore.models.entity.SchoolDO;
import io.github.flashlack1314.smartschedulecore.models.entity.UserDO;
import io.github.flashlack1314.smartschedulecore.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    private final SchoolDAO schoolDAO;
    private final RoleDAO roleDAO;
    private final UserDAO userDAO;


    /**
     * 执行学校初始化
     */
    public void executeSchoolInitialization() {
        log.info("开始执行学校数据初始化");

        List<SchoolDO> schools = new ArrayList<>();

        // 创建默认学校
        SchoolDO defaultSchool = new SchoolDO()
                .setSchoolUuid(IdUtil.simpleUUID())
                .setSchoolName("智能排程系统演示学校")
                .setSchoolNameEn("Smart Schedule Demo School")
                .setSchoolCode("DEMO001")
                .setSchoolType("大学")
                .setSchoolAddress("演示地址")
                .setSchoolPhone("000-00000000")
                .setSchoolEmail("demo@smart-schedule.com");

        schools.add(defaultSchool);

        // 批量插入学校
        schoolDAO.saveBatch(schools);
        log.info("学校数据初始化完成，共插入 {} 条记录", schools.size());

        // 将默认学校信息保存到系统常量
        SystemConstant.School.setDefaultUuid(defaultSchool.getSchoolUuid());
        SystemConstant.School.setDefaultName(defaultSchool.getSchoolName());
        log.info("默认学校信息已保存到系统常量: UUID={}, 名称={}",
                defaultSchool.getSchoolUuid(), defaultSchool.getSchoolName());
    }

    /**
     * 执行角色初始化
     */
    public void executeRoleInitialization() {
        log.info("开始执行角色数据初始化");
        // 执行管理员角色创建
        roleDAO.initAllBasicRoles();
        log.info("角色数据初始化完成");
    }

    /**
     * 执行用户初始化
     */
    public void executeUserInitialization() {
        log.info("开始执行用户数据初始化");

        // 获取角色信息
        RoleDO adminRole = roleDAO.selectByRoleNameEn("admin");
        RoleDO teacherRole = roleDAO.selectByRoleNameEn("teacher");
        RoleDO studentRole = roleDAO.selectByRoleNameEn("student");
        RoleDO academicRole = roleDAO.selectByRoleNameEn("academic");

        // 获取默认学校UUID
        String defaultSchoolUuid = SystemConstant.School.getDefaultUuid();
        if (defaultSchoolUuid == null) {
            log.error("默认学校UUID未找到，无法初始化用户数据");
            return;
        }

        // 创建默认用户列表
        List<UserDO> users = new ArrayList<>();

        // 管理员用户
        if (adminRole != null) {
            users.add(createUser(
                    "admin@flashlack.cn",
                    "系统管理员",
                    adminRole.getRoleUuid(),
                    defaultSchoolUuid
            ));
        }

        // 教师用户
        if (teacherRole != null) {
            users.add(createUser(
                    "teacher@flashlack.cn",
                    "演示教师",
                    teacherRole.getRoleUuid(),
                    defaultSchoolUuid
            ));
        }

        // 学生用户
        if (studentRole != null) {
            users.add(createUser(
                    "student@flashlack.cn",
                    "演示学生",
                    studentRole.getRoleUuid(),
                    defaultSchoolUuid
            ));
        }

        // 教务处老师用户
        if (academicRole != null) {
            users.add(createUser(
                    "academic@flashlack.cn",
                    "演示教务",
                    academicRole.getRoleUuid(),
                    defaultSchoolUuid
            ));
        }

        // 批量插入用户
        if (!users.isEmpty()) {
            userDAO.saveBatch(users);
            log.info("用户数据初始化完成，共插入 {} 条记录", users.size());
        } else {
            log.warn("未创建任何用户数据，请检查角色初始化是否成功");
        }
    }

    /**
     * 创建用户对象
     *
     * @param userEmail  用户邮箱
     * @param userName   用户姓名
     * @param roleUuid   角色UUID
     * @param schoolUuid 学校UUID
     * @return 用户对象
     */
    private UserDO createUser(String userEmail, String userName,
                              String roleUuid, String schoolUuid) {
        String studentId = generateStudentId(userEmail);

        return new UserDO()
                .setUserUuid(IdUtil.simpleUUID())
                .setUserEmail(userEmail)
                .setUserName(userName)
                .setUserPassword(PasswordUtils.encrypt("qwer1234"))
                .setUserRoleUuid(roleUuid)
                .setUserSchoolUuid(schoolUuid)
                .setUserStudentId(studentId)
                .setUserPhoneNum("13800138000");
    }

    /**
     * 根据邮箱生成学号/工号
     *
     * @param email 用户邮箱
     * @return 生成的学号/工号
     */
    private String generateStudentId(String email) {
        // 简单的学号生成规则：邮箱前缀 + 随机数字
        String prefix = email.split("@")[0];
        return prefix + String.format("%04d", (int) (Math.random() * 10000));
    }
}