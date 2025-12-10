package io.github.flashlack1314.smartschedulecore.daos;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.RoleMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.RoleDO;
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
public class RoleDAO extends ServiceImpl<RoleMapper, RoleDO> {

    /**
     * 初始化默认管理员角色
     */
    public void initAdminRole() {
        RoleDO adminRole = new RoleDO();
        adminRole.setRoleUuid(IdUtil.simpleUUID())
                .setRoleName("管理员")
                .setRoleNameEn("admin")
                .setRolePermissions("超级管理员权限");
        this.save(adminRole);
    }

    /**
     * 初始化默认学生角色
     */
    public void initStudentRole() {
        RoleDO studentRole = new RoleDO();
        studentRole.setRoleUuid(IdUtil.simpleUUID())
                .setRoleName("学生")
                .setRoleNameEn("student")
                .setRolePermissions("学生权限");
        this.save(studentRole);
    }

    /**
     * 初始化默认教师角色
     */
    public void initTeacherRole() {
        RoleDO teacherRole = new RoleDO();
        teacherRole.setRoleUuid(IdUtil.simpleUUID())
                .setRoleName("教师")
                .setRoleNameEn("teacher")
                .setRolePermissions("教师权限");
        this.save(teacherRole);
    }

    /**
     * 初始化默认教务处老师角色
     */
    public void initAcademicOfficeRole() {
        RoleDO academicAffairsRole = new RoleDO();
        academicAffairsRole.setRoleUuid(IdUtil.simpleUUID())
                .setRoleName("教务处老师")
                .setRoleNameEn("academic")
                .setRolePermissions("教务处权限");
        this.save(academicAffairsRole);
    }

    /**
     * 初始化所有基础角色
     * 依次创建管理员、教师、学生、教务处老师角色
     */
    public void initAllBasicRoles() {
        initAdminRole();
        initTeacherRole();
        initStudentRole();
        initAcademicOfficeRole();
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