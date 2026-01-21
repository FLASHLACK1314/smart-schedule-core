package io.github.flashlack1314.smartschedulecore.models.enums;

import lombok.Getter;

/**
 * 学时类型枚举
 * <p>
 * 替代 sc_credit_hour_type 表
 * </p>
 *
 * @author flash
 * @since v1.0.0
 */
@Getter
public enum CreditHourType {

    /**
     * 理论学时
     */
    THEORY("理论学时", "THEORY"),

    /**
     * 实验学时
     */
    EXPERIMENT("实验学时", "EXPERIMENT"),

    /**
     * 实践学时
     */
    PRACTICE("实践学时", "PRACTICE"),

    /**
     * 上机学时
     */
    COMPUTER("上机学时", "COMPUTER");

    /**
     * 类型名称
     */
    private final String typeName;

    /**
     * 类型编码
     */
    private final String typeCode;

    CreditHourType(String typeName, String typeCode) {
        this.typeName = typeName;
        this.typeCode = typeCode;
    }
}
