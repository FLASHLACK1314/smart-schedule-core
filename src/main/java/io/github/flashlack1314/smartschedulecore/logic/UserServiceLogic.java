package io.github.flashlack1314.smartschedulecore.logic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.flashlack1314.smartschedulecore.exceptions.BusinessException;
import io.github.flashlack1314.smartschedulecore.exceptions.ErrorCode;
import io.github.flashlack1314.smartschedulecore.models.dto.UserInfoDTO;
import io.github.flashlack1314.smartschedulecore.models.entity.RoleDO;
import io.github.flashlack1314.smartschedulecore.models.entity.UserDO;
import io.github.flashlack1314.smartschedulecore.services.UserService;
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

    private final io.github.flashlack1314.smartschedulecore.daos.UserDAO userDAO;
    private final io.github.flashlack1314.smartschedulecore.daos.RoleDAO roleDAO;

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
}