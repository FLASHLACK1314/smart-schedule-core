package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 校区表实体类
 * <p>
 * 对应数据库表：`sc_campus`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_campus")
@Accessors(chain = true)
public class CampusDO {

    /**
     * 校区主键，采用 UUID 自动生成
     */
    @TableId(value = "campus_uuid", type = IdType.ASSIGN_UUID)
    private String campusUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 校区名称
     */
    private String campusName;

    /**
     * 校区编码（唯一）
     */
    private String campusCode;

    /**
     * 校区地址
     */
    private String campusAddress;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

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
