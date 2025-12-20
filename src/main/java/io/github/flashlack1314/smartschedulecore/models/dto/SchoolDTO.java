package io.github.flashlack1314.smartschedulecore.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 学校数据传输对象
 *
 * @author flash
 */
@Data
@Accessors(chain = true)
public class SchoolDTO {

    /**
     * 学校UUID
     */
    private String schoolUuid;

    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 英文名称
     */
    private String schoolNameEn;

    /**
     * 学校代码
     */
    private String schoolCode;

    /**
     * 学校类型
     */
    private String schoolType;

    /**
     * 学校地址
     */
    private String schoolAddress;

    /**
     * 联系电话
     */
    private String schoolPhone;

    /**
     * 邮箱地址
     */
    private String schoolEmail;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}