package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 年级表实体类
 * <p>
 * 对应数据库表：`sc_grade`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_grade")
@Accessors(chain = true)
public class GradeDO {

    /**
     * 年级主键，采用 UUID 自动生成
     */
    @TableId(value = "grade_uuid", type = IdType.ASSIGN_UUID)
    private String gradeUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 年级名称(如: 2024级)
     */
    private String gradeName;

    /**
     * 入学年份
     */
    private Integer startYear;

    /**
     * 是否启用
     */
    private Boolean isEnabled;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
