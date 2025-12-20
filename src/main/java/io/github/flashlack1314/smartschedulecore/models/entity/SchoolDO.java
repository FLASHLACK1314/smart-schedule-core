package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 学校表实体类
 * <p>
 * 对应数据库表：`sc_school`
 * 主键采用 UUID 自动生成。
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_school")
@Accessors(chain = true)
public class SchoolDO {

    /**
     * 学校主键，采用 UUID 自动生成
     */
    @TableId(value = "school_uuid", type = IdType.ASSIGN_UUID)
    private String schoolUuid;

    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 学校英文名称
     */
    private String schoolNameEn;

    /**
     * 学校代码（唯一）
     */
    private String schoolCode;

    /**
     * 学校类型：大学/中学/小学
     */
    private String schoolType;

    /**
     * 学校地址
     */
    private String schoolAddress;

    /**
     * 学校联系电话
     */
    private String schoolPhone;

    /**
     * 学校邮箱地址
     */
    private String schoolEmail;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}