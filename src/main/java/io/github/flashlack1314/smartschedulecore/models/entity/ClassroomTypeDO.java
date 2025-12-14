package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 教室类型表实体类
 * <p>
 * 对应数据库表：`sc_classroom_type`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_classroom_type")
@Accessors(chain = true)
public class ClassroomTypeDO {

    /**
     * 教室类型主键，采用 UUID 自动生成
     */
    @TableId(value = "classroom_type_uuid", type = IdType.ASSIGN_UUID)
    private String classroomTypeUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 类型名称(普通教室/多媒体教室/实验室/机房/报告厅)
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
