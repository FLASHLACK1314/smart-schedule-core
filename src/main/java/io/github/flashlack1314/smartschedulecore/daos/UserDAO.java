package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.UserMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.UserDO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户数据访问对象 (DAO)
 * <p>
 * 封装 UserMapper，提供用户表的数据访问操作
 * 对应数据库表：`sc_user`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
@RequiredArgsConstructor
public class UserDAO extends ServiceImpl<UserMapper, UserDO> {

    /**
     * 根据用户邮箱查询用户
     *
     * @param userEmail 用户邮箱
     * @return 用户实体，如果不存在返回 null
     */
    public UserDO selectByUserEmail(String userEmail) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDO::getUserEmail, userEmail);
        return this.getOne(wrapper);
    }

    /**
     * 根据用户手机号查询用户
     *
     * @param userPhoneNum 用户手机号
     * @return 用户实体，如果不存在返回 null
     */
    public UserDO selectByUserPhoneNum(String userPhoneNum) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDO::getUserPhoneNum, userPhoneNum);
        return this.getOne(wrapper);
    }

    /**
     * 根据角色UUID查询用户列表
     *
     * @param userRoleUuid 角色UUID
     * @return 用户列表
     */
    public List<UserDO> selectUsersByRoleUuid(String userRoleUuid) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDO::getUserRoleUuid, userRoleUuid)
                .orderByAsc(UserDO::getUserName);
        return this.list(wrapper);
    }

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    public List<UserDO> selectAllUsers() {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(UserDO::getUserName);
        return this.list(wrapper);
    }

    /**
     * 更新用户密码
     *
     * @param userUuid    用户UUID
     * @param newPassword 新密码（已加密）
     * @return 是否更新成功
     */
    public boolean updateUserPassword(String userUuid, String newPassword) {
        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDO::getUserUuid, userUuid)
                .set(UserDO::getUserPassword, newPassword);
        return this.update(wrapper);
    }

    /**
     * 更新用户角色
     *
     * @param userUuid    用户UUID
     * @param newRoleUuid 新角色UUID
     * @return 是否更新成功
     */
    public boolean updateUserRole(String userUuid, String newRoleUuid) {
        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDO::getUserUuid, userUuid)
                .set(UserDO::getUserRoleUuid, newRoleUuid);
        return this.update(wrapper);
    }

    /**
     * 检查用户邮箱是否存在
     *
     * @param userEmail 用户邮箱
     * @return 是否存在
     */
    public boolean existsByUserEmail(String userEmail) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDO::getUserEmail, userEmail);
        return this.count(wrapper) > 0;
    }

    /**
     * 检查用户手机号是否存在
     *
     * @param userPhoneNum 用户手机号
     * @return 是否存在
     */
    public boolean existsByUserPhoneNum(String userPhoneNum) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDO::getUserPhoneNum, userPhoneNum);
        return this.count(wrapper) > 0;
    }
}