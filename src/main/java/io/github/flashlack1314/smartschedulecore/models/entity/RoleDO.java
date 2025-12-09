package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 角色表实体类
 * <p>
 * 对应数据库表：`sc_role`
 * 主键采用 UUID 自动生成。
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_role")
@Accessors(chain = true)
public class RoleDO {

    /**
     * 角色主键，采用 UUID 自动生成
     */
    @TableId(value = "role_uuid", type = IdType.ASSIGN_UUID)
    private String roleUuid;

    /**
     * 角色名称（中文）
     */
    private String roleName;

    /**
     * 角色名称（英文）
     */
    private String roleNameEn;

    /**
     * 角色权限，文本格式
     */
    private String rolePermissions;
}
