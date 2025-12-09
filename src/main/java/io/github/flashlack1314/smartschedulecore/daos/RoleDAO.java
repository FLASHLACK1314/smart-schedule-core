package io.github.flashlack1314.smartschedulecore.daos;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.RoleMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.RoleDO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色数据访问对象 (DAO)
 * <p>
 * 封装 RoleMapper，提供角色表的数据访问操作
 * 对应数据库表：`sc_role`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
@RequiredArgsConstructor
public class RoleDAO extends ServiceImpl<RoleMapper, RoleDO> {

    /**
     * 初始化默认管理员角色
     * 如果管理员角色已存在则不会重复创建
     *
     * @return 是否成功创建（已存在返回false）
     */
    public boolean initAdminRole() {
        // 创建管理员角色
        RoleDO adminRole = new RoleDO();
        adminRole.setRoleUuid(IdUtil.simpleUUID());
        adminRole.setRoleName("管理员");
        adminRole.setRoleNameEn("admin");
        adminRole.setRolePermissions("{\"permissions\": [\"super_admin\"], \"description\": \"超级管理员权限\"}");

        return this.save(adminRole);
    }

    /**
     * 根据角色名称查询角色
     *
     * @param roleName 角色名称
     * @return 角色实体，如果不存在返回 null
     */
    public RoleDO selectByRoleName(String roleName) {
        LambdaQueryWrapper<RoleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleDO::getRoleName, roleName);
        return this.getOne(wrapper);
    }

    /**
     * 根据角色英文名称查询角色
     *
     * @param roleNameEn 角色英文名称
     * @return 角色实体，如果不存在返回 null
     */
    public RoleDO selectByRoleNameEn(String roleNameEn) {
        LambdaQueryWrapper<RoleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleDO::getRoleNameEn, roleNameEn);
        return this.getOne(wrapper);
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    public List<RoleDO> selectAllRoles() {
        LambdaQueryWrapper<RoleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(RoleDO::getRoleName);
        return this.list(wrapper);
    }

    /**
     * 根据角色UUID更新角色权限
     *
     * @param roleUuid        角色UUID
     * @param rolePermissions 角色权限（JSON格式）
     * @return 是否更新成功
     */
    public boolean updateRolePermissions(String roleUuid, String rolePermissions) {
        LambdaUpdateWrapper<RoleDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RoleDO::getRoleUuid, roleUuid)
                .set(RoleDO::getRolePermissions, rolePermissions);
        return this.update(wrapper);
    }

    /**
     * 检查角色名称是否存在
     *
     * @param roleName 角色名称
     * @return 是否存在
     */
    public boolean existsByRoleName(String roleName) {
        LambdaQueryWrapper<RoleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleDO::getRoleName, roleName);
        return this.count(wrapper) > 0;
    }
}