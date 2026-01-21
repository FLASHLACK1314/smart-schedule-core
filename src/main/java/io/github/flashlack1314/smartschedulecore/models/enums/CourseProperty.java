package io.github.flashlack1314.smartschedulecore.models.enums;

import lombok.Getter;

/**
 * 课程属性枚举
 * <p>
 * 替代 sc_course_property 表
 * </p>
 *
 * @author flash
 * @since v1.0.0
 */
@Getter
public enum CourseProperty {

    /**
     * 必修
     */
    REQUIRED("必修", "REQUIRED"),

    /**
     * 选修
     */
    ELECTIVE("选修", "ELECTIVE"),

    /**
     * 限选
     */
    LIMITED_ELECTIVE("限选", "LIMITED");

    /**
     * 属性名称
     */
    private final String propertyName;

    /**
     * 属性编码
     */
    private final String propertyCode;

    CourseProperty(String propertyName, String propertyCode) {
        this.propertyName = propertyName;
        this.propertyCode = propertyCode;
    }
}
