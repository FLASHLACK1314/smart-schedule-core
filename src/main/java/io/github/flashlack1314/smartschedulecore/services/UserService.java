package io.github.flashlack1314.smartschedulecore.services;

import io.github.flashlack1314.smartschedulecore.models.entity.UserDO;

import java.util.List;

/**
 * 用户服务接口
 * 定义用户相关的业务操作
 *
 * @author flash
 */
public interface UserService {

    /**
     * 检查邮箱是否已注册
     *
     * @param email 邮箱地址
     * @return true-已注册，false-未注册
     */
    boolean isEmailRegistered(String email);

    /**
     * 检查手机号是否已注册
     *
     * @param phoneNum 手机号
     * @return true-已注册，false-未注册
     */
    boolean isPhoneRegistered(String phoneNum);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱地址
     * @return 用户实体，不存在返回null
     */
    UserDO getUserByEmail(String email);

    /**
     * 根据手机号查询用户
     *
     * @param phoneNum 手机号
     * @return 用户实体，不存在返回null
     */
    UserDO getUserByPhone(String phoneNum);

    /**
     * 根据UUID查询用户
     *
     * @param userUuid 用户UUID
     * @return 用户实体，不存在返回null
     */
    UserDO getUserByUuid(String userUuid);

    /**
     * 验证用户登录（邮箱+密码）
     *
     * @param email    邮箱地址
     * @param password 密码（明文）
     * @return true-验证成功，false-验证失败
     */
    boolean verifyLogin(String email, String password);

    /**
     * 修改用户密码
     *
     * @param userUuid    用户UUID
     * @param oldPassword 旧密码（明文）
     * @param newPassword 新密码（明文）
     * @return true-修改成功，false-修改失败
     */
    boolean changePassword(String userUuid, String oldPassword, String newPassword);

    /**
     * 重置用户密码（忘记密码）
     *
     * @param email       邮箱地址
     * @param newPassword 新密码（明文）
     * @return true-重置成功，false-重置失败
     */
    boolean resetPassword(String email, String newPassword);

    /**
     * 修改用户角色
     *
     * @param userUuid 用户UUID
     * @param roleUuid 新角色UUID
     * @return true-修改成功，false-修改失败
     */
    boolean changeUserRole(String userUuid, String roleUuid);

    /**
     * 根据角色查询用户列表
     *
     * @param roleUuid 角色UUID
     * @return 用户列表
     */
    List<UserDO> getUsersByRole(String roleUuid);

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    List<UserDO> getAllUsers();

    /**
     * 删除用户
     *
     * @param userUuid 用户UUID
     * @return true-删除成功，false-删除失败
     */
    boolean deleteUser(String userUuid);
}
