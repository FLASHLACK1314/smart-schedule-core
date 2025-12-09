package io.github.flashlack1314.smartschedulecore.config;

import io.github.flashlack1314.smartschedulecore.exceptions.DatabaseInitializationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.FileCopyUtils;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库初始化配置类
 * 系统启动时检查表结构是否完整，按顺序检查：sc_role -> sc_user
 *
 * @author flash
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(DatabaseInitializationConfig.DatabaseInitProperties.class)
public class DatabaseInitializationConfig {

    private final JdbcTemplate jdbcTemplate;
    private final DatabaseInitProperties properties;
    private final BasicDataInitializer basicDataInitializer;

    /**
     * 数据库初始化配置属性
     */
    @Getter
    @Setter
    @ConfigurationProperties(prefix = "app.database.init")
    public static class DatabaseInitProperties {
        /**
         * 是否启用数据库初始化
         */
        private boolean enabled = false;

        /**
         * 是否强制重新创建表（生产环境请勿开启）
         */
        private boolean dropAndCreate = false;

        /**
         * 是否在发现缺失表时删除所有表重建（确保表结构一致）
         */
        private boolean dropAllOnMissing = true;

        /**
         * 需要检查的表（按顺序，从配置文件读取）
         */
        private List<String> tables = new ArrayList<>();

        /**
         * SQL文件路径（与tables顺序一一对应）
         */
        private List<String> sqlFiles = new ArrayList<>();
    }

    @Bean
    @Order(1) // 唯一的数据库初始化Bean
    public ApplicationRunner databaseInitializer() {
        return args -> {
            // 检查是否启用数据库初始化
            if (!properties.isEnabled()) {
                log.info("数据库初始化功能已禁用");
                return;
            }

            log.info("开始检查数据库表结构...");

            try {
                // 检查表是否完整
                List<String> missingTables = this.checkTables();

                // 场景1: 配置强制重建 - 删除所有表，重新创建，初始化数据
                if (properties.isDropAndCreate()) {
                    log.warn("强制重新创建模式：将删除所有表后重新创建并初始化数据");
                    dropAllTables();
                    createAllTables();
                    basicDataInitializer.initializeBasicData();
                    log.info("强制重新创建模式完成");
                    return;
                }

                // 场景2: 表都存在 - 不做任何操作
                if (missingTables.isEmpty()) {
                    log.info("数据库表结构检查通过，所有必需的表都存在，跳过初始化");
                    return;
                }

                // 场景3: 表不完整 - 创建缺失的表并初始化数据
                log.warn("发现缺失的表: {}，将创建并初始化数据", missingTables);
                if (properties.isDropAllOnMissing()) {
                    log.warn("drop-all-on-missing模式：删除所有表后重新创建");
                    dropAllTables();
                    createAllTables();
                } else {
                    createMissingTables(missingTables);
                }
                basicDataInitializer.initializeBasicData();
                log.info("表结构修复和数据初始化完成");

            } catch (DatabaseInitializationException e) {
                // 数据库初始化异常直接抛出
                throw e;
            } catch (Exception e) {
                log.error("数据库初始化失败", e);
                throw new DatabaseInitializationException("数据库初始化失败", e);
            }
        };
    }

    /**
     * 检查必需的表是否存在
     */
    private @NotNull List<String> checkTables() throws Exception {
        List<String> missingTables = new ArrayList<>();

        if (jdbcTemplate.getDataSource() != null) {
            try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
                DatabaseMetaData metaData = connection.getMetaData();

                for (String tableName : properties.getTables()) {
                    try (ResultSet tables = metaData.getTables(null, "public", tableName, new String[]{"TABLE"})) {
                        if (!tables.next()) {
                            missingTables.add(tableName);
                            log.warn("表 {} 不存在", tableName);
                        } else {
                            log.debug("表 {} 已存在", tableName);
                        }
                    }
                }
            }
        }

        return missingTables;
    }

    /**
     * 删除所有配置的表（按反向顺序删除以避免外键约束）
     */
    private void dropAllTables() {
        log.info("开始删除所有表...");

        List<String> failedTables = new ArrayList<>();

        // 按反向顺序删除表，避免外键约束问题
        for (int i = properties.getTables().size() - 1; i >= 0; i--) {
            String tableName = properties.getTables().get(i);

            try {
                //noinspection SqlSourceToSinkFlow
                jdbcTemplate.execute("DROP TABLE IF EXISTS " + tableName + " CASCADE");
                log.debug("已删除表: {}", tableName);
            } catch (Exception e) {
                String errorMsg = String.format("删除表 %s 失败: %s", tableName, e.getMessage());
                log.error(errorMsg);
                failedTables.add(tableName);
            }
        }

        if (!failedTables.isEmpty()) {
            String message = String.format("删除表时发生错误，失败的表: %s", String.join(", ", failedTables));
            throw new DatabaseInitializationException(message);
        }

        log.info("所有表删除完成");
    }

    /**
     * 创建所有表（用于dropAndCreate模式）
     */
    private void createAllTables() throws Exception {
        log.info("开始创建所有表（共{}个）", properties.getTables().size());
        for (int i = 0; i < properties.getTables().size(); i++) {
            String tableName = properties.getTables().get(i);
            String sqlFile = properties.getSqlFiles().get(i);

            log.debug("正在创建表: {} (步骤 {}/{})", tableName, i + 1, properties.getTables().size());
            executeSqlFile(sqlFile);
        }
        log.info("所有表创建完成");
    }

    /**
     * 创建缺失的表
     */
    private void createMissingTables(List<String> missingTables) throws Exception {
        log.info("开始创建缺失的表: {}", missingTables);
        for (int i = 0; i < properties.getTables().size(); i++) {
            String tableName = properties.getTables().get(i);
            String sqlFile = properties.getSqlFiles().get(i);

            if (missingTables.contains(tableName)) {
                executeSqlFile(sqlFile);
            }
        }
        log.info("缺失表创建完成");
    }

    /**
     * 执行SQL文件
     */
    private void executeSqlFile(String sqlFilePath) throws Exception {
        log.debug("执行SQL文件: {}", sqlFilePath);
        ClassPathResource resource = new ClassPathResource(sqlFilePath);

        if (!resource.exists()) {
            throw new RuntimeException("SQL文件不存在: " + sqlFilePath);
        }

        // 读取SQL文件内容
        String sqlContent = FileCopyUtils.copyToString(
            new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
        );

        // 分割SQL语句（以分号结尾）
        String[] sqlStatements = sqlContent.split(";");

        int statementCount = 0;
        for (String sql : sqlStatements) {
            sql = sql.trim();

            // 跳过空行和注释
            if (sql.isEmpty() || sql.startsWith("--")) {
                continue;
            }

            // 确保以分号结尾
            if (!sql.endsWith(";")) {
                sql += ";";
            }

            executeSql(sql);
            statementCount++;
        }

        if (statementCount > 0) {
            log.debug("成功执行 {} 条SQL语句", statementCount);
        }
    }

    /**
     * 执行单条SQL语句
     */
    private void executeSql(String sql) {
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            log.error("执行SQL失败: {}\nSQL: {}", e.getMessage(), sql);
            throw e;
        }
    }
}