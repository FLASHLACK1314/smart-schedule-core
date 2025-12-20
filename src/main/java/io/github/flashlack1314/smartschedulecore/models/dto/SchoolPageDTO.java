package io.github.flashlack1314.smartschedulecore.models.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 学校分页响应数据传输对象
 *
 * @author flash
 */
@Data
@Accessors(chain = true)
public class SchoolPageDTO {

    /**
     * 学校列表数据
     */
    private List<SchoolDTO> records;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Long current;

    /**
     * 每页大小
     */
    private Long size;

    /**
     * 总页数
     */
    private Long pages;
}