package io.github.flashlack1314.smartschedulecore.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.flashlack1314.smartschedulecore.constants.SystemConstant;
import io.github.flashlack1314.smartschedulecore.daos.RoleDAO;
import io.github.flashlack1314.smartschedulecore.daos.UserDAO;
import io.github.flashlack1314.smartschedulecore.exceptions.BusinessException;
import io.github.flashlack1314.smartschedulecore.exceptions.ErrorCode;
import io.github.flashlack1314.smartschedulecore.models.dto.UserInfoDTO;
import io.github.flashlack1314.smartschedulecore.models.entity.RoleDO;
import io.github.flashlack1314.smartschedulecore.models.entity.UserDO;
import io.github.flashlack1314.smartschedulecore.services.EmailService;
import io.github.flashlack1314.smartschedulecore.services.TokenService;
import io.github.flashlack1314.smartschedulecore.services.UserService;
import io.github.flashlack1314.smartschedulecore.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户服务实现类
 * 负责用户相关的查询和数据操作的具体实现
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceLogic implements UserService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final EmailService emailService;
    private final TokenService tokenService;

    @Override
    public UserDO getUserByEmail(String userEmail) {
        log.debug("根据邮箱查询用户: userEmail={}", userEmail);
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDO::getUserEmail, userEmail);
        return userDAO.getOne(wrapper);
    }

    @Override
    public UserDO getUserByUuid(String userUuid) {
        log.debug("根据UUID查询用户: userUuid={}", userUuid);
        return userDAO.getById(userUuid);
    }

    @Override
    public RoleDO getUserRole(String userUuid) {
        log.debug("获取用户角色: userUuid={}", userUuid);
        UserDO user = userDAO.getById(userUuid);
        if (user == null) {
            log.warn("用户不存在: userUuid={}", userUuid);
            return null;
        }
        return roleDAO.getById(user.getUserRoleUuid());
    }

    @Override
    public boolean isEmailRegistered(String userEmail) {
        log.debug("检查邮箱是否已注册: userEmail={}", userEmail);
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDO::getUserEmail, userEmail);
        return userDAO.count(wrapper) > 0;
    }

    @Override
    public UserInfoDTO buildUserInfoDTO(UserDO user, RoleDO role) {
        log.debug("构建用户信息DTO: userUuid={}, roleNameEn={}",
                user != null ? user.getUserUuid() : null,
                role != null ? role.getRoleNameEn() : null);

        // 1. 验证用户数据
        if (user == null) {
            throw new BusinessException("用户数据不存在", ErrorCode.USER_NOT_FOUND);
        }

        // 2. 验证角色数据
        if (role == null) {
            throw new BusinessException("用户角色数据不存在", ErrorCode.USER_ROLE_NOT_FOUND);
        }

        // 3. 验证用户关键字段
        if (!StringUtils.hasText(user.getUserUuid())) {
            throw new BusinessException("用户UUID缺失", ErrorCode.DATA_INCOMPLETE);
        }
        if (!StringUtils.hasText(user.getUserName())) {
            throw new BusinessException("用户姓名缺失", ErrorCode.DATA_INCOMPLETE);
        }
        if (!StringUtils.hasText(user.getUserEmail())) {
            throw new BusinessException("用户邮箱缺失", ErrorCode.DATA_INCOMPLETE);
        }

        // 4. 验证角色关键字段
        if (!StringUtils.hasText(role.getRoleName())) {
            throw new BusinessException("角色名称缺失", ErrorCode.DATA_INCOMPLETE);
        }
        if (!StringUtils.hasText(role.getRoleNameEn())) {
            throw new BusinessException("角色英文名称缺失", ErrorCode.DATA_INCOMPLETE);
        }

        // 5. 构建返回数据
        return UserInfoDTO.builder()
                .userUuid(user.getUserUuid())
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .userPhoneNum(user.getUserPhoneNum())
                .roleName(role.getRoleName())
                .roleNameEn(role.getRoleNameEn())
                .build();
    }

    // ========== 密码管理相关方法实现 ==========

    @Override
    public void changePassword(String userUuid, String currentPassword, String newPassword) {
        log.info("开始修改用户密码: userUuid={}", userUuid);

        // 1. 参数验证
        if (!StringUtils.hasText(userUuid) || !StringUtils.hasText(currentPassword) || !StringUtils.hasText(newPassword)) {
            throw new BusinessException("参数不能为空", ErrorCode.PARAM_ERROR);
        }

        // 2. 获取用户信息
        UserDO user = userDAO.getById(userUuid);
        if (user == null) {
            log.warn("用户不存在: userUuid={}", userUuid);
            throw new BusinessException("用户不存在", ErrorCode.USER_NOT_FOUND);
        }

        // 3. 验证当前密码
        if (!PasswordUtils.verify(currentPassword, user.getUserPassword())) {
            log.warn("当前密码不正确: userUuid={}", userUuid);
            throw new BusinessException("当前密码不正确", ErrorCode.CURRENT_PASSWORD_INCORRECT);
        }

        // 4. 加密新密码
        String encryptedNewPassword = PasswordUtils.encrypt(newPassword);

        // 5. 更新密码
        boolean updated = userDAO.updateUserPassword(userUuid, encryptedNewPassword);
        if (!updated) {
            log.error("密码更新失败: userUuid={}", userUuid);
            throw new BusinessException("密码更新失败", ErrorCode.RESET_OPERATION_FAILED);
        }

        // 6. 清理用户所有Token，强制重新登录
        tokenService.removeAllTokens(userUuid);

        log.info("用户密码修改成功: userUuid={}", userUuid);
    }

    @Override
    public void resetUserPassword(String userEmail, String newPassword) {
        log.info("管理员重置用户密码: userEmail={}", userEmail);

        // 1. 参数验证
        if (!StringUtils.hasText(userEmail) || !StringUtils.hasText(newPassword)) {
            throw new BusinessException("参数不能为空", ErrorCode.PARAM_ERROR);
        }

        // 2. 获取目标用户
        UserDO targetUser = getUserByEmail(userEmail);
        if (targetUser == null) {
            log.warn("目标用户不存在: userEmail={}", userEmail);
            throw new BusinessException("用户不存在", ErrorCode.USER_NOT_FOUND);
        }

        // 3. 获取目标用户角色
        RoleDO targetRole = roleDAO.getById(targetUser.getUserRoleUuid());
        if (targetRole != null && SystemConstant.Role.getAdminUuid().equals(targetRole.getRoleNameEn())) {
            log.warn("不能重置管理员密码: userEmail={}", userEmail);
            throw new BusinessException("不能重置管理员密码", ErrorCode.CANNOT_RESET_ADMIN_PASSWORD);
        }

        // 4. 加密新密码
        String encryptedNewPassword = PasswordUtils.encrypt(newPassword);

        // 5. 更新密码
        boolean updated = userDAO.updateUserPassword(targetUser.getUserUuid(), encryptedNewPassword);
        if (!updated) {
            log.error("管理员重置密码失败: userEmail={}", userEmail);
            throw new BusinessException("密码重置失败", ErrorCode.RESET_OPERATION_FAILED);
        }

        // 6. 清理目标用户所有Token
        tokenService.removeAllTokens(targetUser.getUserUuid());

        // 7. 发送密码重置成功通知邮件
        try {
            emailService.sendPasswordResetSuccessNotification(userEmail, targetUser.getUserName());
        } catch (Exception e) {
            log.error("发送密码重置成功通知邮件失败: userEmail={}", userEmail, e);
            // 邮件发送失败不影响密码重置操作
        }

        log.info("管理员重置用户密码成功: userEmail={}", userEmail);
    }

    @Override
    public void resetPasswordByCode(String userEmail, String verificationCode, String newPassword) {
        log.info("通过验证码重置密码: userEmail={}", userEmail);

        // 1. 参数验证
        if (!StringUtils.hasText(userEmail) || !StringUtils.hasText(verificationCode) || !StringUtils.hasText(newPassword)) {
            throw new BusinessException("参数不能为空", ErrorCode.PARAM_ERROR);
        }

        // 2. 验证验证码
        boolean codeValid = emailService.verifyPasswordResetCode(userEmail, verificationCode);
        if (!codeValid) {
            log.warn("密码重置验证码无效或已过期: userEmail={}", userEmail);
            throw new BusinessException("验证码无效或已过期", ErrorCode.RESET_CODE_INVALID);
        }

        // 3. 获取用户信息
        UserDO user = getUserByEmail(userEmail);
        if (user == null) {
            log.warn("用户不存在: userEmail={}", userEmail);
            throw new BusinessException("用户不存在", ErrorCode.USER_NOT_FOUND);
        }

        // 4. 加密新密码
        String encryptedNewPassword = PasswordUtils.encrypt(newPassword);

        // 5. 更新密码
        boolean updated = userDAO.updateUserPassword(user.getUserUuid(), encryptedNewPassword);
        if (!updated) {
            log.error("密码重置失败: userEmail={}", userEmail);
            throw new BusinessException("密码重置失败", ErrorCode.RESET_OPERATION_FAILED);
        }

        // 6. 清理用户所有Token
        tokenService.removeAllTokens(user.getUserUuid());

        // 7. 发送密码重置成功通知邮件
        try {
            emailService.sendPasswordResetSuccessNotification(userEmail, user.getUserName());
        } catch (Exception e) {
            log.error("发送密码重置成功通知邮件失败: userEmail={}", userEmail, e);
            // 邮件发送失败不影响密码重置操作
        }

        log.info("通过验证码重置密码成功: userEmail={}", userEmail);
    }

    @Override
    public void sendPasswordResetCode(String userEmail) {
        log.info("发送密码重置验证码: userEmail={}", userEmail);

        // 1. 参数验证
        if (!StringUtils.hasText(userEmail)) {
            throw new BusinessException("邮箱不能为空", ErrorCode.PARAM_ERROR);
        }

        // 2. 检查用户是否存在
        UserDO user = getUserByEmail(userEmail);
        if (user == null) {
            log.warn("用户不存在，不发送验证码: userEmail={}", userEmail);
            // 为了安全，即使用户不存在也返回成功，避免信息泄露
            return;
        }

        // 3. 发送密码重置验证码邮件
        emailService.sendPasswordResetCode(userEmail);

        log.info("密码重置验证码发送成功: userEmail={}", userEmail);
    }

    // ========== 基于Token的密码管理方法实现 ==========

    @Override
    public void changePasswordByToken(String token, String currentPassword, String newPassword, String confirmPassword) {
        log.info("开始基于Token修改用户密码");

        String userUuid = tokenService.validateAndGetUser(token);
        if (!StringUtils.hasText(userUuid)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED.getErrorMessage(), ErrorCode.UNAUTHORIZED);
        }

        // 2. 密码一致性验证
        if (!newPassword.equals(confirmPassword)) {
            throw new BusinessException(ErrorCode.PASSWORD_MISMATCH.getErrorMessage(), ErrorCode.PASSWORD_MISMATCH);
        }

        // 3. 调用现有方法
        changePassword(userUuid, currentPassword, newPassword);
    }

    @Override
    public void resetUserPasswordByToken(String token, String userEmail, String newPassword) {
        log.info("开始基于Token重置用户密码: userEmail={}", userEmail);
        String adminUuid = tokenService.validateAndGetUser(token);
        if (!StringUtils.hasText(adminUuid)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED.getErrorMessage(), ErrorCode.UNAUTHORIZED);
        }

        // 3. 调用现有方法
        resetUserPassword(userEmail, newPassword);
    }

    @Override
    public void resetPasswordByCodeWithValidation(String userEmail, String verificationCode, String newPassword, String confirmPassword) {
        log.info("开始通过验证码重置密码: userEmail={}", userEmail);
        // 1. 密码一致性验证
        if (!newPassword.equals(confirmPassword)) {
            throw new BusinessException(ErrorCode.PASSWORD_MISMATCH.getErrorMessage(), ErrorCode.PASSWORD_MISMATCH);
        }
        // 2. 调用现有方法
        resetPasswordByCode(userEmail, verificationCode, newPassword);
    }
}