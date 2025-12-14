package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 课程类型表实体类
 * <p>
 * 对应数据库表：`sc_course_type`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_course_type")
@Accessors(chain = true)
public class CourseTypeDO {

    /**
     * 课程类型主键，采用 UUID 自动生成
     */
    @TableId(value = "course_type_uuid", type = IdType.ASSIGN_UUID)
    private String courseTypeUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 类型名称(理论课/实验课/实践课/上机课)
     */
    private String typeName;

    /**
     * 类型编码（唯一）
     */
    private String typeCode;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
