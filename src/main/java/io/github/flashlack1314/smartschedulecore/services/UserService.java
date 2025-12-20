package io.github.flashlack1314.smartschedulecore.services;

import io.github.flashlack1314.smartschedulecore.exceptions.BusinessException;
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

    // ========== 密码管理相关方法 ==========

    /**
     * 修改用户密码
     *
     * @param userUuid        用户UUID
     * @param currentPassword 当前密码
     * @param newPassword     新密码
     * @throws BusinessException 当前密码错误或新密码格式错误时抛出异常
     */
    void changePassword(String userUuid, String currentPassword, String newPassword);

    /**
     * 重置用户密码（管理员功能）
     *
     * @param userEmail   目标用户邮箱
     * @param newPassword 新密码
     * @throws BusinessException 用户不存在时抛出异常
     */
    void resetUserPassword(String userEmail, String newPassword);

    /**
     * 通过验证码重置密码
     *
     * @param userEmail        用户邮箱
     * @param verificationCode 验证码
     * @param newPassword      新密码
     * @throws BusinessException 验证码错误或用户不存在时抛出异常
     */
    void resetPasswordByCode(String userEmail, String verificationCode, String newPassword);

    /**
     * 发送密码重置验证码
     *
     * @param userEmail 用户邮箱
     * @throws BusinessException 用户不存在或邮件发送失败时抛出异常
     */
    void sendPasswordResetCode(String userEmail);

    // ========== 基于Token的密码管理方法 ==========

    /**
     * 基于Token修改用户密码
     * 包含Token验证和密码一致性验证
     *
     * @param token           用户Token
     * @param currentPassword 当前密码
     * @param newPassword     新密码
     * @param confirmPassword 确认密码
     * @throws BusinessException Token无效、密码错误或格式错误时抛出异常
     */
    void changePasswordByToken(String token, String currentPassword, String newPassword, String confirmPassword);

    /**
     * 基于Token重置用户密码（管理员功能）
     * 包含Token验证和权限验证
     *
     * @param token       管理员Token
     * @param userEmail   目标用户邮箱
     * @param newPassword 新密码
     * @throws BusinessException Token无效、权限不足或用户不存在时抛出异常
     */
    void resetUserPasswordByToken(String token, String userEmail, String newPassword);

    /**
     * 通过验证码重置密码（包含密码一致性验证）
     *
     * @param userEmail        用户邮箱
     * @param verificationCode 验证码
     * @param newPassword      新密码
     * @param confirmPassword  确认密码
     * @throws BusinessException 验证码错误、密码不一致或用户不存在时抛出异常
     */
    void resetPasswordByCodeWithValidation(String userEmail, String verificationCode, String newPassword, String confirmPassword);
}