package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 课程属性表实体类
 * <p>
 * 对应数据库表：`sc_course_property`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_course_property")
@Accessors(chain = true)
public class CoursePropertyDO {

    /**
     * 课程属性主键，采用 UUID 自动生成
     */
    @TableId(value = "course_property_uuid", type = IdType.ASSIGN_UUID)
    private String coursePropertyUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 属性名称(必修/选修/限选)
     */
    private String propertyName;

    /**
     * 属性编码（唯一）
     */
    private String propertyCode;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
