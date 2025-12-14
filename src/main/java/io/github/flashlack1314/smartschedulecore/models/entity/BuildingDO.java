package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 教学楼表实体类
 * <p>
 * 对应数据库表：`sc_building`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_building")
@Accessors(chain = true)
public class BuildingDO {

    /**
     * 教学楼主键，采用 UUID 自动生成
     */
    @TableId(value = "building_uuid", type = IdType.ASSIGN_UUID)
    private String buildingUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 关联校区UUID
     */
    private String campusUuid;

    /**
     * 教学楼名称
     */
    private String buildingName;

    /**
     * 教学楼编码（唯一）
     */
    private String buildingCode;

    /**
     * 楼层数
     */
    private Integer floorCount;

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
