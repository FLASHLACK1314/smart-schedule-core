package io.github.flashlack1314.smartschedulecore.services;

import io.github.flashlack1314.smartschedulecore.models.dto.UserInfoDTO;
import io.github.flashlack1314.smartschedulecore.models.entity.RoleDO;
import io.github.flashlack1314.smartschedulecore.models.entity.UserDO;

/**
 * 用户服务接口
 * 负责用户相关的查询和数据操作
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
public interface UserService {

    /**
     * 根据邮箱查询用户
     *
     * @param userEmail 用户邮箱
     * @return 用户实体，如果不存在返回 null
     */
    UserDO getUserByEmail(String userEmail);

    /**
     * 根据用户UUID查询用户
     *
     * @param userUuid 用户UUID
     * @return 用户实体，如果不存在返回 null
     */
    UserDO getUserByUuid(String userUuid);

    /**
     * 获取用户角色信息
     *
     * @param userUuid 用户UUID
     * @return 角色实体，如果不存在返回 null
     */
    RoleDO getUserRole(String userUuid);

    /**
     * 检查邮箱是否已被注册
     *
     * @param userEmail 邮箱地址
     * @return true-已注册，false-未注册
     */
    boolean isEmailRegistered(String userEmail);

    /**
     * 构建用户信息DTO
     *
     * @param user 用户实体
     * @param role 角色实体
     * @return 用户信息DTO
     */
    UserInfoDTO buildUserInfoDTO(UserDO user, RoleDO role);
}