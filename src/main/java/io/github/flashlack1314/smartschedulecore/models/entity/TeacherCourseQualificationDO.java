package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 教师课程资格表实体类
 * <p>
 * 对应数据库表：`sc_teacher_course_qualification`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_teacher_course_qualification")
@Accessors(chain = true)
public class TeacherCourseQualificationDO {

    /**
     * 资格主键，采用 UUID 自动生成
     */
    @TableId(value = "qualification_uuid", type = IdType.ASSIGN_UUID)
    private String qualificationUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 关联教师UUID
     */
    private String teacherUuid;

    /**
     * 关联课程UUID
     */
    private String courseUuid;

    /**
     * 资格等级（1-5级）
     */
    private Integer qualificationLevel;

    /**
     * 是否主讲
     */
    private Boolean isPrimary;

    /**
     * 审批状态（0待审批/1已通过/2已拒绝）
     */
    private Integer approvalStatus;

    /**
     * 审批人UUID
     */
    private String approvedBy;

    /**
     * 审批时间
     */
    private LocalDateTime approvedAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
