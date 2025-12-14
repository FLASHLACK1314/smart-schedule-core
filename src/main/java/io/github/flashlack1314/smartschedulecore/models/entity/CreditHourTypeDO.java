package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 学时类型表实体类
 * <p>
 * 对应数据库表：`sc_credit_hour_type`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_credit_hour_type")
@Accessors(chain = true)
public class CreditHourTypeDO {

    /**
     * 学时类型主键，采用 UUID 自动生成
     */
    @TableId(value = "credit_hour_type_uuid", type = IdType.ASSIGN_UUID)
    private String creditHourTypeUuid;

    /**
     * 关联学校UUID
     */
    private String schoolUuid;

    /**
     * 类型名称(理论学时/实验学时/上机学时/实践学时)
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
