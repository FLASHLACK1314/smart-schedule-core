package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 教师类型表实体类
 * <p>
 * 对应数据库表：`sc_teacher_type`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_teacher_type")
@Accessors(chain = true)
public class TeacherTypeDO {

    /**
     * 教师类型主键，采用 UUID 自动生成
     */
    @TableId(value = "teacher_type_uuid", type = IdType.ASSIGN_UUID)
    private String teacherTypeUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 类型名称(专任教师/兼职教师/外聘教师)
     */
    private String typeName;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
