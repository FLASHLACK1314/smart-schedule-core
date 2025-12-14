package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 院系/部门表实体类
 * <p>
 * 对应数据库表：`sc_department`
 * 支持树形结构（通过 parentDepartmentUuid 自引用）
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_department")
@Accessors(chain = true)
public class DepartmentDO {

    /**
     * 部门主键，采用 UUID 自动生成
     */
    @TableId(value = "department_uuid", type = IdType.ASSIGN_UUID)
    private String departmentUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 部门编码（唯一）
     */
    private String departmentCode;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 部门英文名称
     */
    private String departmentEnglishName;

    /**
     * 部门简称
     */
    private String departmentShortName;

    /**
     * 上级部门UUID（支持树形结构）
     */
    private String parentDepartmentUuid;

    /**
     * 是否开课院系
     */
    private Boolean isTeachingDepartment;

    /**
     * 是否启用
     */
    private Boolean isEnabled;

    /**
     * 排序
     */
    private Integer departmentOrder;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
