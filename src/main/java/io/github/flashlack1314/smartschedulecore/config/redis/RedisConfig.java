package io.github.flashlack1314.smartschedulecore.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis基础配置类
 * 提供Redis连接池配置、序列化配置和超时设置
 *
 * @author flash
 */
@Configuration
public class RedisConfig {

    /**
     * Redis连接工厂配置
     * 配置连接超时、连接池等关键参数
     *
     * @return LettuceConnectionFactory
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        // Redis基础配置
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName("localhost");
        redisConfig.setPort(6379);
        redisConfig.setDatabase(0);

        // 客户端配置：超时设置
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(5))  // 命令执行超时
                .shutdownTimeout(Duration.ZERO)          // 关闭超时
                .build();

        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }

    /**
     * Redis模板配置
     * 设置Key和Value的序列化方式
     * Key使用String序列化，Value使用JSON序列化（保持灵活性）
     *
     * @param connectionFactory Redis连接工厂
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key使用String序列化（确保Key的可读性和一致性）
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // Value使用GenericJackson2JsonRedisSerializer
        // 保持现有序列化策略，不同业务需求可以自行定义具体的序列化策略
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer(jsonRedisSerializer);
        template.setHashValueSerializer(jsonRedisSerializer);

        // 设置默认序列化器
        template.setDefaultSerializer(jsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }
}