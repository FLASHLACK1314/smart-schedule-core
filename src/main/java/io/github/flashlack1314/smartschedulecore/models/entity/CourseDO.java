package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.flashlack1314.smartschedulecore.models.enums.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程表实体类
 * <p>
 * 对应数据库表：`sc_course`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_course")
@Accessors(chain = true)
public class CourseDO {

    /**
     * 课程主键，采用 UUID 自动生成
     */
    @TableId(value = "course_uuid", type = IdType.ASSIGN_UUID)
    private String courseUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 课程编码（唯一）
     */
    private String courseCode;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程英文名称
     */
    private String courseEnglishName;

    /**
     * 开课院系UUID
     */
    private String departmentUuid;

    /**
     * 课程类别（枚举，替代 course_category_uuid 外键）
     */
    private CourseCategory category;

    /**
     * 课程属性（枚举，替代 course_property_uuid 外键）
     */
    private CourseProperty property;

    /**
     * 课程类型（枚举，替代 course_type_uuid 外键）
     */
    private CourseType type;

    /**
     * 课程性质（枚举，替代 course_nature_uuid 外键）
     */
    private CourseNature nature;

    /**
     * 总学时
     */
    private Integer totalHours;

    /**
     * 周学时
     */
    private Integer weekHours;

    /**
     * 理论学时
     */
    private Integer theoryHours;

    /**
     * 实验学时
     */
    private Integer experimentHours;

    /**
     * 实践学时
     */
    private Integer practiceHours;

    /**
     * 上机学时
     */
    private Integer computerHours;

    /**
     * 学分
     */
    private BigDecimal credit;

    /**
     * 理论课教室类型（枚举，替代 theory_classroom_type_uuid 外键）
     */
    private ClassroomType theoryClassroomType;

    /**
     * 实验课教室类型（枚举，替代 experiment_classroom_type_uuid 外键）
     */
    private ClassroomType experimentClassroomType;

    /**
     * 实践课教室类型（枚举，替代 practice_classroom_type_uuid 外键）
     */
    private ClassroomType practiceClassroomType;

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
