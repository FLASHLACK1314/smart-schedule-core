package io.github.flashlack1314.smartschedulecore.models.vo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 查询学校参数对象
 * 用于Service层内部的参数传递，Controller层使用Query参数接收
 *
 * @author flash
 */
@Data
public class SchoolQueryVO {

    /**
     * 页码
     */
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    @Min(value = 1, message = "每页大小必须大于0")
    @Max(value = 100, message = "每页大小不能超过100")
    private Integer pageSize = 10;

    /**
     * 学校名称（模糊查询）
     */
    private String schoolName;

    /**
     * 学校代码
     */
    private String schoolCode;

    /**
     * 学校类型
     */
    private String schoolType;
}