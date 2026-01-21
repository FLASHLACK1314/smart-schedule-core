package io.github.flashlack1314.smartschedulecore.models.enums;

import lombok.Getter;

/**
 * 课程类型枚举
 * <p>
 * 替代 sc_course_type 表
 * </p>
 *
 * @author flash
 * @since v1.0.0
 */
@Getter
public enum CourseType {

    /**
     * 理论课
     */
    THEORY("理论课", "THEORY"),

    /**
     * 实验课
     */
    EXPERIMENT("实验课", "EXPERIMENT"),

    /**
     * 实践课
     */
    PRACTICE("实践课", "PRACTICE"),

    /**
     * 上机课
     */
    COMPUTER("上机课", "COMPUTER");

    /**
     * 类型名称
     */
    private final String typeName;

    /**
     * 类型编码
     */
    private final String typeCode;

    CourseType(String typeName, String typeCode) {
        this.typeName = typeName;
        this.typeCode = typeCode;
    }
}
