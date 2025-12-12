package io.github.flashlack1314.smartschedulecore.logic;

import cn.hutool.core.util.StrUtil;
import io.github.flashlack1314.smartschedulecore.daos.UserDAO;
import io.github.flashlack1314.smartschedulecore.models.entity.UserDO;
import io.github.flashlack1314.smartschedulecore.services.UserService;
import io.github.flashlack1314.smartschedulecore.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户服务实现类
 * 实现用户相关的业务逻辑
 *
 * @author flash
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserServiceLogic implements UserService {

    private final UserDAO userDAO;

    /**
     * 检查邮箱是否已注册
     *
     * @param email 邮箱地址
     * @return true-已注册，false-未注册
     */
    @Override
    public boolean isEmailRegistered(String email) {
        if (StrUtil.isBlank(email)) {
            return false;
        }
        boolean exists = userDAO.existsByUserEmail(email);
        log.debug("检查邮箱是否已注册: email={}, exists={}", email, exists);
        return exists;
    }

    /**
     * 检查手机号是否已注册
     *
     * @param phoneNum 手机号
     * @return true-已注册，false-未注册
     */
    @Override
    public boolean isPhoneRegistered(String phoneNum) {
        if (StrUtil.isBlank(phoneNum)) {
            return false;
        }
        boolean exists = userDAO.existsByUserPhoneNum(phoneNum);
        log.debug("检查手机号是否已注册: phoneNum={}, exists={}", phoneNum, exists);
        return exists;
    }

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱地址
     * @return 用户实体，不存在返回null
     */
    @Override
    public UserDO getUserByEmail(String email) {
        if (StrUtil.isBlank(email)) {
            return null;
        }
        UserDO user = userDAO.selectByUserEmail(email);
        log.debug("根据邮箱查询用户: email={}, found={}", email, user != null);
        return user;
    }

    /**
     * 根据手机号查询用户
     *
     * @param phoneNum 手机号
     * @return 用户实体，不存在返回null
     */
    @Override
    public UserDO getUserByPhone(String phoneNum) {
        if (StrUtil.isBlank(phoneNum)) {
            return null;
        }
        UserDO user = userDAO.selectByUserPhoneNum(phoneNum);
        log.debug("根据手机号查询用户: phoneNum={}, found={}", phoneNum, user != null);
        return user;
    }

    /**
     * 根据UUID查询用户
     *
     * @param userUuid 用户UUID
     * @return 用户实体，不存在返回null
     */
    @Override
    public UserDO getUserByUuid(String userUuid) {
        if (StrUtil.isBlank(userUuid)) {
            return null;
        }
        UserDO user = userDAO.getById(userUuid);
        log.debug("根据UUID查询用户: uuid={}, found={}", userUuid, user != null);
        return user;
    }

    /**
     * 验证用户登录（邮箱+密码）
     *
     * @param email    邮箱地址
     * @param password 密码（明文）
     * @return true-验证成功，false-验证失败
     */
    @Override
    public boolean verifyLogin(String email, String password) {
        if (StrUtil.hasBlank(email, password)) {
            log.debug("登录验证失败: 邮箱或密码为空");
            return false;
        }

        // 1. 查询用户
        UserDO user = getUserByEmail(email);
        if (user == null) {
            log.debug("登录验证失败: 用户不存在, email={}", email);
            return false;
        }

        // 2. 验证密码
        boolean verified = PasswordUtils.verify(password, user.getUserPassword());
        log.debug("登录验证结果: email={}, success={}", email, verified);
        return verified;
    }

    /**
     * 修改用户密码
     *
     * @param userUuid    用户UUID
     * @param oldPassword 旧密码（明文）
     * @param newPassword 新密码（明文）
     * @return true-修改成功，false-修改失败
     */
    @Override
    public boolean changePassword(String userUuid, String oldPassword, String newPassword) {
        if (StrUtil.hasBlank(userUuid, oldPassword, newPassword)) {
            log.warn("修改密码失败: 参数不能为空");
            return false;
        }

        // 1. 查询用户
        UserDO user = getUserByUuid(userUuid);
        if (user == null) {
            log.warn("修改密码失败: 用户不存在, uuid={}", userUuid);
            return false;
        }

        // 2. 验证旧密码
        if (!PasswordUtils.verify(oldPassword, user.getUserPassword())) {
            log.warn("修改密码失败: 旧密码错误, uuid={}", userUuid);
            return false;
        }

        // 3. 更新密码
        String encryptedPassword = PasswordUtils.encrypt(newPassword);
        boolean updated = userDAO.updateUserPassword(userUuid, encryptedPassword);

        if (updated) {
            log.info("密码修改成功: uuid={}", userUuid);
        } else {
            log.error("密码修改失败: 数据库更新失败, uuid={}", userUuid);
        }

        return updated;
    }

    /**
     * 重置用户密码（忘记密码）
     *
     * @param email       邮箱地址
     * @param newPassword 新密码（明文）
     * @return true-重置成功，false-重置失败
     */
    @Override
    public boolean resetPassword(String email, String newPassword) {
        if (StrUtil.hasBlank(email, newPassword)) {
            log.warn("重置密码失败: 参数不能为空");
            return false;
        }

        // 1. 查询用户
        UserDO user = getUserByEmail(email);
        if (user == null) {
            log.warn("重置密码失败: 用户不存在, email={}", email);
            return false;
        }

        // 2. 更新密码
        String encryptedPassword = PasswordUtils.encrypt(newPassword);
        boolean updated = userDAO.updateUserPassword(user.getUserUuid(), encryptedPassword);

        if (updated) {
            log.info("密码重置成功: email={}, uuid={}", email, user.getUserUuid());
        } else {
            log.error("密码重置失败: 数据库更新失败, email={}", email);
        }

        return updated;
    }

    /**
     * 修改用户角色
     *
     * @param userUuid 用户UUID
     * @param roleUuid 新角色UUID
     * @return true-修改成功，false-修改失败
     */
    @Override
    public boolean changeUserRole(String userUuid, String roleUuid) {
        if (StrUtil.hasBlank(userUuid, roleUuid)) {
            log.warn("修改用户角色失败: 参数不能为空");
            return false;
        }

        // 1. 检查用户是否存在
        UserDO user = getUserByUuid(userUuid);
        if (user == null) {
            log.warn("修改用户角色失败: 用户不存在, uuid={}", userUuid);
            return false;
        }

        // 2. 更新角色
        boolean updated = userDAO.updateUserRole(userUuid, roleUuid);

        if (updated) {
            log.info("用户角色修改成功: uuid={}, newRoleUuid={}", userUuid, roleUuid);
        } else {
            log.error("用户角色修改失败: 数据库更新失败, uuid={}", userUuid);
        }

        return updated;
    }

    /**
     * 根据角色查询用户列表
     *
     * @param roleUuid 角色UUID
     * @return 用户列表
     */
    @Override
    public List<UserDO> getUsersByRole(String roleUuid) {
        if (StrUtil.isBlank(roleUuid)) {
            log.warn("查询失败: 角色UUID不能为空");
            return List.of();
        }

        List<UserDO> users = userDAO.selectUsersByRoleUuid(roleUuid);
        log.debug("根据角色查询用户: roleUuid={}, count={}", roleUuid, users.size());
        return users;
    }

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    @Override
    public List<UserDO> getAllUsers() {
        List<UserDO> users = userDAO.selectAllUsers();
        log.debug("查询所有用户: count={}", users.size());
        return users;
    }

    /**
     * 删除用户
     *
     * @param userUuid 用户UUID
     * @return true-删除成功，false-删除失败
     */
    @Override
    public boolean deleteUser(String userUuid) {
        if (StrUtil.isBlank(userUuid)) {
            log.warn("删除用户失败: UUID不能为空");
            return false;
        }

        // 1. 检查用户是否存在
        UserDO user = getUserByUuid(userUuid);
        if (user == null) {
            log.warn("删除用户失败: 用户不存在, uuid={}", userUuid);
            return false;
        }

        // 2. 删除用户
        boolean deleted = userDAO.removeById(userUuid);

        if (deleted) {
            log.info("用户删除成功: uuid={}, email={}", userUuid, user.getUserEmail());
        } else {
            log.error("用户删除失败: 数据库删除失败, uuid={}", userUuid);
        }

        return deleted;
    }
}
