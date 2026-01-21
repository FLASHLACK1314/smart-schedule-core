package io.github.flashlack1314.smartschedulecore.models.enums;

import lombok.Getter;

/**
 * 课程性质枚举
 * <p>
 * 替代 sc_course_nature 表
 * </p>
 *
 * @author flash
 * @since v1.0.0
 */
@Getter
public enum CourseNature {

    /**
     * 公共课
     */
    PUBLIC_COURSE("公共课", "PUBLIC"),

    /**
     * 专业基础课
     */
    MAJOR_BASIC("专业基础课", "MAJOR_BASIC"),

    /**
     * 专业核心课
     */
    MAJOR_CORE("专业核心课", "MAJOR_CORE");

    /**
     * 性质名称
     */
    private final String natureName;

    /**
     * 性质编码
     */
    private final String natureCode;

    CourseNature(String natureName, String natureCode) {
        this.natureName = natureName;
        this.natureCode = natureCode;
    }
}
