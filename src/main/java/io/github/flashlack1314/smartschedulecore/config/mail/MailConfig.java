package io.github.flashlack1314.smartschedulecore.config.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * 邮件服务配置类
 * 显式配置JavaMailSender Bean，确保IDE能正确识别依赖注入
 * 使用Spring Boot的MailProperties自动读取application.yaml中的配置
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MailProperties.class)
@ConditionalOnProperty(prefix = "spring.mail", name = "host")
public class MailConfig {

    private final MailProperties mailProperties;

    /**
     * 配置JavaMailSender Bean
     * 自动读取application.yaml中spring.mail.*的所有配置
     *
     * @return JavaMailSender实例
     */
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // 应用Spring Boot的邮件配置
        mailSender.setHost(mailProperties.getHost());
        if (mailProperties.getPort() != null) {
            mailSender.setPort(mailProperties.getPort());
        }
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());
        mailSender.setProtocol(mailProperties.getProtocol());
        mailSender.setDefaultEncoding(mailProperties.getDefaultEncoding().name());

        // 应用YAML中配置的所有properties属性
        Properties props = new Properties();
        props.putAll(mailProperties.getProperties());
        mailSender.setJavaMailProperties(props);

        return mailSender;
    }
}