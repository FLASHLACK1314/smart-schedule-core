package io.github.flashlack1314.smartschedulecore.logic;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import io.github.flashlack1314.smartschedulecore.daos.RoleDAO;
import io.github.flashlack1314.smartschedulecore.daos.UserDAO;
import io.github.flashlack1314.smartschedulecore.exceptions.BusinessException;
import io.github.flashlack1314.smartschedulecore.exceptions.ErrorCode;
import io.github.flashlack1314.smartschedulecore.models.dto.RegisterBackDTO;
import io.github.flashlack1314.smartschedulecore.models.entity.RoleDO;
import io.github.flashlack1314.smartschedulecore.models.entity.UserDO;
import io.github.flashlack1314.smartschedulecore.models.vo.RegisterVO;
import io.github.flashlack1314.smartschedulecore.services.AuthService;
import io.github.flashlack1314.smartschedulecore.services.EmailService;
import io.github.flashlack1314.smartschedulecore.services.UserService;
import io.github.flashlack1314.smartschedulecore.utils.PasswordUtils;
import io.github.flashlack1314.smartschedulecore.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 认证逻辑类
 *
 * @author flash
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceLogic implements AuthService {

    private final UserService userService;
    private final EmailService emailService;
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

    @Override
    public RegisterBackDTO registerUser(RegisterVO registerVO) {
        // 1. 验证邮箱验证码
        boolean codeValid = emailService.verifyCode(
                registerVO.getUserEmail(),
                registerVO.getVerificationCode()
        );
        if (!codeValid) {
            log.warn("注册失败: 验证码错误或已过期, email={}", registerVO.getUserEmail());
            throw new BusinessException("验证码错误或已过期", ErrorCode.PARAM_ERROR);
        }
        // 2. 检查邮箱是否已注册
        if (userService.isEmailRegistered(registerVO.getUserEmail())) {
            log.warn("注册失败: 邮箱已被注册, email={}", registerVO.getUserEmail());
            throw new BusinessException("邮箱已被注册", ErrorCode.DATA_EXISTS);
        }
        // 3. 检查手机号是否已注册（如果提供了手机号）
        if (StrUtil.isNotBlank(registerVO.getUserPhoneNum()) &&
                userService.isPhoneRegistered(registerVO.getUserPhoneNum())) {
            log.warn("注册失败: 手机号已被注册, phoneNum={}", registerVO.getUserPhoneNum());
            throw new BusinessException("手机号已被注册", ErrorCode.DATA_EXISTS);
        }
        // 4. 根据角色英文名称查询角色UUID
        RoleDO role = roleDAO.selectByRoleNameEn(registerVO.getRoleNameEn());
        if (role == null) {
            log.warn("注册失败: 角色不存在, roleNameEn={}", registerVO.getRoleNameEn());
            throw new BusinessException("角色不存在", ErrorCode.PARAM_ERROR);
        }
        // 5. 创建用户对象
        UserDO user = new UserDO();
        user.setUserUuid(IdUtil.simpleUUID())
                .setUserName(registerVO.getUserName())
                .setUserEmail(registerVO.getUserEmail())
                .setUserPassword(PasswordUtils.encrypt(registerVO.getUserPassword()))
                .setUserRoleUuid(role.getRoleUuid())
                .setUserPhoneNum(registerVO.getUserPhoneNum());
        // 6. 保存到数据库
        userDAO.save(user);
        log.info("用户注册成功: uuid={}, email={}, role={}",
                user.getUserUuid(), registerVO.getUserEmail(), registerVO.getRoleNameEn());
        // 7. 构建返回对象
        RegisterBackDTO result = new RegisterBackDTO();
        result.setToken(TokenUtils.generateToken(user.getUserUuid()));
        return result;
    }
}
