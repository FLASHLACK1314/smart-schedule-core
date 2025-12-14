package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 教室表实体类
 * <p>
 * 对应数据库表：`sc_classroom`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_classroom")
@Accessors(chain = true)
public class ClassroomDO {

    /**
     * 教室主键，采用 UUID 自动生成
     */
    @TableId(value = "classroom_uuid", type = IdType.ASSIGN_UUID)
    private String classroomUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 关联校区UUID
     */
    private String campusUuid;

    /**
     * 关联教学楼UUID
     */
    private String buildingUuid;

    /**
     * 教室类型UUID
     */
    private String classroomTypeUuid;

    /**
     * 教室编号（唯一）
     */
    private String classroomNumber;

    /**
     * 教室名称
     */
    private String classroomName;

    /**
     * 楼层
     */
    private Integer floor;

    /**
     * 容量
     */
    private Integer capacity;

    /**
     * 考场容量
     */
    private Integer examinationCapacity;

    /**
     * 面积（平方米）
     */
    private BigDecimal area;

    /**
     * 教室标签（JSONB，如：["智慧教室", "录播教室", "阶梯教室"]）
     */
    private String tags;

    /**
     * 是否多媒体教室
     */
    private Boolean isMultimedia;

    /**
     * 是否有空调
     */
    private Boolean isAirConditioned;

    /**
     * 是否考场
     */
    private Boolean isExaminationRoom;

    /**
     * 管理部门UUID
     */
    private String managementDepartmentUuid;

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
