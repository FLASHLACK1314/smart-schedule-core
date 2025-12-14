package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 排课冲突表实体类
 * <p>
 * 对应数据库表：`sc_scheduling_conflict`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_scheduling_conflict")
@Accessors(chain = true)
public class SchedulingConflictDO {

    /**
     * 冲突主键，采用 UUID 自动生成
     */
    @TableId(value = "conflict_uuid", type = IdType.ASSIGN_UUID)
    private String conflictUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 关联学期UUID
     */
    private String semesterUuid;

    /**
     * 第一个排课UUID
     */
    private String firstAssignmentUuid;

    /**
     * 第二个排课UUID
     */
    private String secondAssignmentUuid;

    /**
     * 冲突类型（1教师/2教室/3班级/4其他）
     */
    private Integer conflictType;

    /**
     * 冲突时间（JSONB格式）
     */
    private String conflictTime;

    /**
     * 解决状态（0未解决/1已解决/2忽略）
     */
    private Integer resolutionStatus;

    /**
     * 解决方法（1调整第一个/2调整第二个/3同时调整/4其他）
     */
    private Integer resolutionMethod;

    /**
     * 解决备注
     */
    private String resolutionNotes;

    /**
     * 解决人UUID
     */
    private String resolvedBy;

    /**
     * 解决时间
     */
    private LocalDateTime resolvedAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
