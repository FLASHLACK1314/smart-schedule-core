package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 教师表实体类
 * <p>
 * 对应数据库表：`sc_teacher`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_teacher")
@Accessors(chain = true)
public class TeacherDO {

    /**
     * 教师主键，采用 UUID 自动生成
     */
    @TableId(value = "teacher_uuid", type = IdType.ASSIGN_UUID)
    private String teacherUuid;

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
     * 教师类型UUID
     */
    private String teacherTypeUuid;

    /**
     * 工号（唯一）
     */
    private String teacherCode;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 教师英文名
     */
    private String teacherEnglishName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 职称
     */
    private String jobTitle;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
