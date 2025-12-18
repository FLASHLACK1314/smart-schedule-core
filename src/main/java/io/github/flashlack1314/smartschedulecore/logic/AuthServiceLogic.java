package io.github.flashlack1314.smartschedulecore.logic;

import io.github.flashlack1314.smartschedulecore.daos.RoleDAO;
import io.github.flashlack1314.smartschedulecore.daos.UserDAO;
import io.github.flashlack1314.smartschedulecore.exceptions.BusinessException;
import io.github.flashlack1314.smartschedulecore.exceptions.ErrorCode;
import io.github.flashlack1314.smartschedulecore.models.dto.LoginBackDTO;
import io.github.flashlack1314.smartschedulecore.models.entity.RoleDO;
import io.github.flashlack1314.smartschedulecore.models.entity.UserDO;
import io.github.flashlack1314.smartschedulecore.models.vo.LoginVO;
import io.github.flashlack1314.smartschedulecore.services.AuthService;
import io.github.flashlack1314.smartschedulecore.services.TokenService;
import io.github.flashlack1314.smartschedulecore.services.UserService;
import io.github.flashlack1314.smartschedulecore.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 认证逻辑类
 * 负责登录、登出等核心认证功能
 *
 * @author flash
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceLogic implements AuthService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final UserService userService;
    private final TokenService tokenService;

    @Override
    public LoginBackDTO login(LoginVO loginVO) {
        // 1. 参数验证（通过@Valid自动完成）
        log.info("开始用户登录: userEmail={}", loginVO.getUserEmail());

        // 2. 查询用户
        UserDO user = userDAO.selectByUserEmail(loginVO.getUserEmail());
        if (user == null) {
            log.warn("登录失败: 用户不存在, userEmail={}", loginVO.getUserEmail());
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 3. 验证密码
        if (!PasswordUtils.verify(loginVO.getUserPassword(), user.getUserPassword())) {
            log.warn("登录失败: 密码错误, userEmail={}", loginVO.getUserEmail());
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }

        // 4. 检查用户封禁状态
        if (Boolean.TRUE.equals(user.getBan())) {
            log.warn("登录失败: 用户已被封禁, userEmail={}", loginVO.getUserEmail());
            throw new BusinessException(ErrorCode.USER_DISABLED);
        }

        // 5. 获取用户角色
        RoleDO role = roleDAO.getById(user.getUserRoleUuid());
        if (role == null) {
            log.warn("登录失败: 用户角色不存在, userUuid={}, userEmail={}",
                    user.getUserUuid(), loginVO.getUserEmail());
            throw new BusinessException("用户角色不存在", ErrorCode.USER_NOT_FOUND);
        }

        // 6. 生成Token
        String token = tokenService.generateToken(user.getUserUuid());
        if (token == null) {
            log.error("登录失败: Token生成失败, userUuid={}, userEmail={}",
                    user.getUserUuid(), loginVO.getUserEmail());
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }

        // 7. 构建返回数据
        LoginBackDTO loginBackDTO = new LoginBackDTO();
        loginBackDTO.setToken(token);
        loginBackDTO.setUserInfo(userService.buildUserInfoDTO(user, role));

        log.info("用户登录成功: userEmail={}, userUuid={}, role={}, tokenGenerated={}",
                user.getUserEmail(), user.getUserUuid(), role.getRoleNameEn(), true);

        return loginBackDTO;
    }

    @Override
    public void logout(String userUuid, String token) {
        log.info("开始用户登出: userUuid={}", userUuid);

        // 使用兼容性验证方法，确保token有效
        String validatedUserUuid = tokenService.validateTokenCompatible(token, userUuid);
        if (validatedUserUuid != null && validatedUserUuid.equals(userUuid)) {
            boolean removed = tokenService.removeAllTokens(userUuid);
            if (removed) {
                log.info("用户登出成功: userUuid={}", userUuid);
            } else {
                log.warn("用户登出失败: 无需删除的token, userUuid={}", userUuid);
            }
        } else {
            log.warn("用户登出失败: Token无效或用户不匹配, userUuid={}, token={}", userUuid, token);
        }
    }
}