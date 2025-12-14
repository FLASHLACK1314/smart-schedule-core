package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 行政班表实体类
 * <p>
 * 对应数据库表：`sc_administrative_class`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_administrative_class")
@Accessors(chain = true)
public class AdministrativeClassDO {

    /**
     * 行政班主键，采用 UUID 自动生成
     */
    @TableId(value = "administrative_class_uuid", type = IdType.ASSIGN_UUID)
    private String administrativeClassUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 所属院系UUID
     */
    private String departmentUuid;

    /**
     * 所属专业UUID
     */
    private String majorUuid;

    /**
     * 所属年级UUID
     */
    private String gradeUuid;

    /**
     * 班级编码（唯一）
     */
    private String classCode;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 学生人数
     */
    private Integer studentCount;

    /**
     * 辅导员UUID
     */
    private String counselorUuid;

    /**
     * 班长UUID
     */
    private String monitorUuid;

    /**
     * 是否启用
     */
    private Boolean isEnabled;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
