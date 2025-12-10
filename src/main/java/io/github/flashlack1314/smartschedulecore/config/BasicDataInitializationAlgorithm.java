package io.github.flashlack1314.smartschedulecore.config;

import io.github.flashlack1314.smartschedulecore.daos.RoleDAO;
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


    /**
     * 执行角色初始化
     */
    public void executeRoleInitialization() {
        // 执行管理员角色创建
        roleDAO.initAllBasicRoles();
    }


}