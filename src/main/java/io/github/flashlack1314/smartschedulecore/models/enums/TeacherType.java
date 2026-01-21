package io.github.flashlack1314.smartschedulecore.models.enums;

import lombok.Getter;

/**
 * 教师类型枚举
 * <p>
 * 替代 sc_teacher_type 表
 * </p>
 *
 * @author flash
 * @since v1.0.0
 */
@Getter
public enum TeacherType {

    /**
     * 专职教师
     */
    FULL_TIME("专职教师", "FULL_TIME"),

    /**
     * 兼职教师
     */
    PART_TIME("兼职教师", "PART_TIME"),

    /**
     * 外聘教师
     */
    EXTERNAL("外聘教师", "EXTERNAL"),

    /**
     * 客座教授
     */
    VISITING_PROFESSOR("客座教授", "VISITING");

    /**
     * 类型名称
     */
    private final String typeName;

    /**
     * 类型编码
     */
    private final String typeCode;

    TeacherType(String typeName, String typeCode) {
        this.typeName = typeName;
        this.typeCode = typeCode;
    }
}
