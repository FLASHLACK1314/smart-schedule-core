package io.github.flashlack1314.smartschedulecore.config;

import io.github.flashlack1314.smartschedulecore.daos.RoleDAO;
import io.github.flashlack1314.smartschedulecore.exceptions.DatabaseInitializationException;
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
public class BasicDataInitializationAlgorithm {

    private final RoleDAO roleDAO;

    public BasicDataInitializationAlgorithm(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    /**
     * 执行管理员角色初始化算法
     *
     * @return boolean true表示成功创建，false表示已存在
     * @throws DatabaseInitializationException 初始化异常
     */
    public boolean executeAdminRoleInitialization() throws DatabaseInitializationException {
        log.debug("开始执行管理员角色初始化算法...");

        try {
            // 算法步骤1：检查管理员角色是否已存在
            if (isAdminRoleExists()) {
                log.debug("管理员角色已存在，跳过初始化");
                return false;
            }

            // 算法步骤2：执行管理员角色创建
            boolean created = roleDAO.initAdminRole();

            // 算法步骤3：验证创建结果
            if (created && isAdminRoleExists()) {
                log.info("管理员角色初始化算法执行成功");
                return true;
            } else if (!created) {
                log.warn("管理员角色创建方法返回false，可能角色已存在");
                return false;
            } else {
                throw new DatabaseInitializationException("管理员角色创建成功但验证失败");
            }

        } catch (Exception e) {
            log.error("管理员角色初始化算法执行失败", e);
            throw new DatabaseInitializationException("管理员角色初始化算法执行失败: " + e.getMessage(), e);
        }
    }

    /**
     * 检查管理员角色是否已存在
     *
     * @return true 如果管理员角色已存在
     */
    public boolean isAdminRoleExists() {
        try {
            return roleDAO.selectByRoleName("管理员") != null;
        } catch (Exception e) {
            log.warn("检查管理员角色是否存在时发生异常: {}", e.getMessage());
            // 发生异常时保守处理，假设角色存在以避免重复创建
            return true;
        }
    }

    /**
     * 验证数据初始化的完整性
     *
     * @return true 如果数据初始化完整
     */
    public boolean validateDataIntegrity() {
        try {
            // 验证管理员角色是否存在
            boolean adminExists = isAdminRoleExists();

            if (!adminExists) {
                log.warn("数据完整性验证失败：管理员角色不存在");
                return false;
            }

            log.debug("数据完整性验证通过");
            return true;

        } catch (Exception e) {
            log.error("数据完整性验证时发生异常: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取数据初始化状态信息
     *
     * @return String 状态信息
     */
    public String getInitializationStatus() {
        try {
            boolean adminExists = isAdminRoleExists();

            if (adminExists) {
                return "数据已初始化（管理员角色存在）";
            } else {
                return "数据未初始化（管理员角色不存在）";
            }

        } catch (Exception e) {
            return "数据初始化状态未知：" + e.getMessage();
        }
    }
}