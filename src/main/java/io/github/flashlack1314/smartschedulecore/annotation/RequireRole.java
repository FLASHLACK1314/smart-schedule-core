package io.github.flashlack1314.smartschedulecore.annotation;

import java.lang.annotation.*;

/**
 * 权限验证注解
 * 用于标记需要特定角色才能访问的方法或类
 *
 * @author flash
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {
    /**
     * 需要的角色
     * 支持多个角色，默认为OR逻辑（拥有其中一个角色即可）
     */
    String[] value() default {};

    /**
     * 是否需要所有角色（AND逻辑）
     * true: 需要拥有所有指定角色
     * false: 只需要拥有其中一个角色（默认）
     */
    boolean requireAll() default false;
}