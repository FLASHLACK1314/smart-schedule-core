package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 专业表实体类
 * <p>
 * 对应数据库表：`sc_major`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_major")
@Accessors(chain = true)
public class MajorDO {

    /**
     * 专业主键，采用 UUID 自动生成
     */
    @TableId(value = "major_uuid", type = IdType.ASSIGN_UUID)
    private String majorUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 所属学院UUID
     */
    private String departmentUuid;

    /**
     * 专业代码（唯一）
     */
    private String majorCode;

    /**
     * 专业名称
     */
    private String majorName;

    /**
     * 学制(年)
     */
    private Short educationYears;

    /**
     * 培养层次(本科/专科/研究生)
     */
    private String trainingLevel;

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
