package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 教学班表实体类
 * <p>
 * 对应数据库表：`sc_teaching_class`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_teaching_class")
@Accessors(chain = true)
public class TeachingClassDO {

    /**
     * 教学班主键，采用 UUID 自动生成
     */
    @TableId(value = "teaching_class_uuid", type = IdType.ASSIGN_UUID)
    private String teachingClassUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 关联学期UUID
     */
    private String semesterUuid;

    /**
     * 关联课程UUID
     */
    private String courseUuid;

    /**
     * 开课院系UUID
     */
    private String departmentUuid;

    /**
     * 教学班编码（唯一）
     */
    private String teachingClassCode;

    /**
     * 教学班名称
     */
    private String teachingClassName;

    /**
     * 包含的行政班UUID数组（JSONB格式）
     */
    private String administrativeClasses;

    /**
     * 是否必修课
     */
    private Boolean isCompulsory;

    /**
     * 班级规模
     */
    private Integer classSize;

    /**
     * 实际学生人数
     */
    private Integer actualStudentCount;

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
