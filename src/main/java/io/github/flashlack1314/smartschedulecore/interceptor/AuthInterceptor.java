package io.github.flashlack1314.smartschedulecore.interceptor;

import io.github.flashlack1314.smartschedulecore.annotation.RequireRole;
import io.github.flashlack1314.smartschedulecore.exceptions.BusinessException;
import io.github.flashlack1314.smartschedulecore.exceptions.ErrorCode;
import io.github.flashlack1314.smartschedulecore.models.entity.RoleDO;
import io.github.flashlack1314.smartschedulecore.services.TokenService;
import io.github.flashlack1314.smartschedulecore.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

/**
 * 权限验证拦截器
 * 拦截带有@RequireRole注解的方法，进行权限验证
 *
 * @author flash
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;
    private final UserService userService;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response, @NotNull Object handler) {
        // 只处理方法处理器
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // 获取方法上的注解
        RequireRole methodAnnotation = handlerMethod.getMethodAnnotation(RequireRole.class);

        // 如果方法上没有注解，检查类上的注解
        if (methodAnnotation == null) {
            methodAnnotation = handlerMethod.getBeanType().getAnnotation(RequireRole.class);
        }

        // 没有权限注解，直接通过
        if (methodAnnotation == null) {
            return true;
        }

        // 验证权限
        checkPermission(request, methodAnnotation);

        return true;
    }

    /**
     * 检查权限
     *
     * @param request    HTTP请求
     * @param annotation 权限注解
     */
    private void checkPermission(HttpServletRequest request, RequireRole annotation) {
        try {
            // 从请求头获取token
            String token = getTokenFromRequest(request);
            if (!StringUtils.hasText(token)) {
                log.debug("权限验证失败：请求头中未找到Token");
                throw new BusinessException(ErrorCode.UNAUTHORIZED);
            }

            // 验证token并获取用户信息
            String userUuid = tokenService.validateAndGetUser(token);
            if (!StringUtils.hasText(userUuid)) {
                log.debug("权限验证失败：Token无效或已过期");
                throw new BusinessException(ErrorCode.UNAUTHORIZED);
            }

            // 获取用户角色
            RoleDO roleDO = userService.getUserRole(userUuid);
            if (roleDO == null) {
                log.debug("权限验证失败：用户角色不存在，userUuid: {}", userUuid);
                throw new BusinessException(ErrorCode.USER_ROLE_NOT_FOUND);
            }

            // 检查权限
            String[] requiredRoles = annotation.value();
            if (requiredRoles.length == 0) {
                return; // 没有指定角色要求，通过验证
            }

            String roleNameEn = roleDO.getRoleNameEn();
            boolean hasPermission = false;

            if (annotation.requireAll()) {
                // 需要拥有所有指定角色（AND逻辑）
                hasPermission = Arrays.stream(requiredRoles)
                        .allMatch(role -> role.equals(roleNameEn));
            } else {
                // 只需要拥有其中一个角色（OR逻辑）
                hasPermission = Arrays.asList(requiredRoles).contains(roleNameEn);
            }

            if (!hasPermission) {
                log.debug("权限验证失败：用户角色不满足要求，用户角色: {}, 需要角色: {}, requireAll: {}",
                        roleNameEn, Arrays.toString(requiredRoles), annotation.requireAll());
                throw new BusinessException(ErrorCode.FORBIDDEN);
            }

            log.debug("权限验证通过，userUuid: {}, 用户角色: {}", userUuid, roleNameEn);

        } catch (BusinessException e) {
            // 重新抛出业务异常
            throw e;
        } catch (Exception e) {
            // 其他异常包装为业务异常
            log.error("权限验证过程中发生异常", e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }
    }

    /**
     * 从请求头中获取Token
     *
     * @param request HTTP请求
     * @return Token字符串
     */
    private String getTokenFromRequest(@NotNull HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return bearerToken;
    }
}