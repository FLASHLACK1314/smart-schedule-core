package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 课程性质表实体类
 * <p>
 * 对应数据库表：`sc_course_nature`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_course_nature")
@Accessors(chain = true)
public class CourseNatureDO {

    /**
     * 课程性质主键，采用 UUID 自动生成
     */
    @TableId(value = "course_nature_uuid", type = IdType.ASSIGN_UUID)
    private String courseNatureUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 性质名称(公共课/专业基础课/专业核心课)
     */
    private String natureName;

    /**
     * 性质编码（唯一）
     */
    private String natureCode;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
