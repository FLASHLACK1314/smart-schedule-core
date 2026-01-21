package io.github.flashlack1314.smartschedulecore.models.enums;

import lombok.Getter;

/**
 * 教室类型枚举
 * <p>
 * 替代 sc_classroom_type 表
 * </p>
 *
 * @author flash
 * @since v1.0.0
 */
@Getter
public enum ClassroomType {

    /**
     * 普通教室
     */
    NORMAL("普通教室", "NORMAL"),

    /**
     * 多媒体教室
     */
    MULTIMEDIA("多媒体教室", "MULTIMEDIA"),

    /**
     * 阶梯教室
     */
    LECTURE_HALL("阶梯教室", "LECTURE_HALL"),

    /**
     * 实验室
     */
    LABORATORY("实验室", "LABORATORY"),

    /**
     * 计算机房
     */
    COMPUTER_ROOM("计算机房", "COMPUTER_ROOM"),

    /**
     * 智慧教室
     */
    SMART_CLASSROOM("智慧教室", "SMART"),

    /**
     * 体育场馆
     */
    SPORTS_FACILITY("体育场馆", "SPORTS"),

    /**
     * 音乐教室
     */
    MUSIC_ROOM("音乐教室", "MUSIC"),

    /**
     * 美术教室
     */
    ART_ROOM("美术教室", "ART");

    /**
     * 类型名称
     */
    private final String typeName;

    /**
     * 类型编码
     */
    private final String typeCode;

    ClassroomType(String typeName, String typeCode) {
        this.typeName = typeName;
        this.typeCode = typeCode;
    }
}
