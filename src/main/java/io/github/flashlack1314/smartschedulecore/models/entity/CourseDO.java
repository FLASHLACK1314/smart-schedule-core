package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
     * 课程类别UUID
     */
    private String courseCategoryUuid;

    /**
     * 课程属性UUID
     */
    private String coursePropertyUuid;

    /**
     * 课程类型UUID
     */
    private String courseTypeUuid;

    /**
     * 课程性质UUID
     */
    private String courseNatureUuid;

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
     * 理论课教室类型UUID
     */
    private String theoryClassroomTypeUuid;

    /**
     * 实验课教室类型UUID
     */
    private String experimentClassroomTypeUuid;

    /**
     * 实践课教室类型UUID
     */
    private String practiceClassroomTypeUuid;

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
