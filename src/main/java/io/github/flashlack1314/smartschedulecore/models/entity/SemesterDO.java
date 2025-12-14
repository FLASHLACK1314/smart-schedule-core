package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学期表实体类
 * <p>
 * 对应数据库表：`sc_semester`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_semester")
@Accessors(chain = true)
public class SemesterDO {

    /**
     * 学期主键，采用 UUID 自动生成
     */
    @TableId(value = "semester_uuid", type = IdType.ASSIGN_UUID)
    private String semesterUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 学期名称(如：2024-2025学年第1学期)
     */
    private String semesterName;

    /**
     * 学期开始日期
     */
    private LocalDate startDate;

    /**
     * 学期结束日期
     */
    private LocalDate endDate;

    /**
     * 是否当前学期
     */
    private Boolean isCurrent;

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
