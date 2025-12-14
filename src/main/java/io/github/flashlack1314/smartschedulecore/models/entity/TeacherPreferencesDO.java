package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 教师偏好表实体类
 * <p>
 * 对应数据库表：`sc_teacher_preferences`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_teacher_preferences")
@Accessors(chain = true)
public class TeacherPreferencesDO {

    /**
     * 偏好主键，采用 UUID 自动生成
     */
    @TableId(value = "preference_uuid", type = IdType.ASSIGN_UUID)
    private String preferenceUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 关联教师UUID
     */
    private String teacherUuid;

    /**
     * 偏好时间段（JSONB格式）
     */
    private String preferredTime;

    /**
     * 避开时间段（JSONB格式）
     */
    private String avoidedTime;

    /**
     * 偏好校区UUID
     */
    private String preferredCampusUuid;

    /**
     * 偏好教室类型UUID
     */
    private String preferredClassroomTypeUuid;

    /**
     * 每天最多课程数
     */
    private Integer maxCoursesPerDay;

    /**
     * 最多连续课程数
     */
    private Integer maxConsecutiveCourses;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
