package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 排课表实体类
 * <p>
 * 对应数据库表：`sc_class_assignment`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_class_assignment")
@Accessors(chain = true)
public class ClassAssignmentDO {

    /**
     * 排课主键，采用 UUID 自动生成
     */
    @TableId(value = "class_assignment_uuid", type = IdType.ASSIGN_UUID)
    private String classAssignmentUuid;

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
     * 关联教师UUID
     */
    private String teacherUuid;

    /**
     * 关联教学班UUID
     */
    private String teachingClassUuid;

    /**
     * 关联校区UUID
     */
    private String campusUuid;

    /**
     * 关联教学楼UUID
     */
    private String buildingUuid;

    /**
     * 关联教室UUID
     */
    private String classroomUuid;

    /**
     * 关联教室类型UUID
     */
    private String classroomTypeUuid;

    /**
     * 学时类型UUID
     */
    private String creditHourTypeUuid;

    /**
     * 教师实际授课学时
     */
    private Integer teachingHours;

    /**
     * 已排课学时
     */
    private Integer scheduledHours;

    /**
     * 总需学时
     */
    private Integer totalHours;

    /**
     * 上课时间（JSONB格式：周次、星期、节次）
     */
    private String classTime;

    /**
     * 指定固定时间（JSONB格式）
     */
    private String specifiedTime;

    /**
     * 连堂节数（默认2）
     */
    private Integer consecutiveSessions;

    /**
     * 排课优先级（默认100）
     */
    private Integer schedulingPriority;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
