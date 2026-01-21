package io.github.flashlack1314.smartschedulecore.models.enums;

import lombok.Getter;

/**
 * 课程类别枚举
 * <p>
 * 替代 sc_course_category 表
 * </p>
 *
 * @author flash
 * @since v1.0.0
 */
@Getter
public enum CourseCategory {

    /**
     * 通识课
     */
    GENERAL_EDUCATION("通识课", "GENERAL"),

    /**
     * 专业课
     */
    MAJOR_COURSE("专业课", "MAJOR"),

    /**
     * 实践课
     */
    PRACTICE_COURSE("实践课", "PRACTICE");

    /**
     * 类别名称
     */
    private final String categoryName;

    /**
     * 类别编码
     */
    private final String categoryCode;

    CourseCategory(String categoryName, String categoryCode) {
        this.categoryName = categoryName;
        this.categoryCode = categoryCode;
    }
}
