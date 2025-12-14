package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 学生表实体类
 * <p>
 * 对应数据库表：`sc_student`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_student")
@Accessors(chain = true)
public class StudentDO {

    /**
     * 学生主键，采用 UUID 自动生成
     */
    @TableId(value = "student_uuid", type = IdType.ASSIGN_UUID)
    private String studentUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 关联用户UUID
     */
    private String userUuid;

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
     * 所属行政班UUID
     */
    private String administrativeClassUuid;

    /**
     * 学号（唯一）
     */
    private String studentCode;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 是否毕业
     */
    private Boolean isGraduated;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
