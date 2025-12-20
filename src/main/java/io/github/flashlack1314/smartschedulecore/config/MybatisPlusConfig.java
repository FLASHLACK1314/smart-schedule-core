package io.github.flashlack1314.smartschedulecore.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus配置类
 * 配置分页插件和其他MyBatis Plus功能
 *
 * @author flash
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * MyBatis Plus拦截器配置
     *
     * @return MyBatis Plus拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // TODO: 确认MyBatis Plus 3.5.15的正确分页插件类名
        // 暂时不添加分页插件，需要找到正确的类名
        // interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));

        return interceptor;
    }
}