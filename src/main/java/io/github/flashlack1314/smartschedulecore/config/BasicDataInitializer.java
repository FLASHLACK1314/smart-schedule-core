package io.github.flashlack1314.smartschedulecore.config;

import io.github.flashlack1314.smartschedulecore.exceptions.DatabaseInitializationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 基础数据初始化器
 * 负责系统基础数据的初始化，包括角色、用户等基础数据
 *
 * @author flash
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BasicDataInitializer {

    private final BasicDataInitializationAlgorithm algorithm;

    /**
     * 初始化基础数据
     * 只有在数据库重建时才应该调用此方法
     */
    public void initializeBasicData() {
        log.debug("开始执行基础数据初始化...");

        try {
            // 执行管理员角色初始化
            algorithm.executeAdminRoleInitialization();

            // 未来可以在这里添加其他初始化步骤
            // 例如：
            // - 初始化默认用户
            // - 初始化系统配置
            // - 初始化权限数据等

            log.debug("基础数据初始化逻辑执行完成");
        } catch (Exception e) {
            log.error("初始化基础数据失败", e);
            throw new DatabaseInitializationException("初始化基础数据失败: " + e.getMessage(), e);
        }
    }
}