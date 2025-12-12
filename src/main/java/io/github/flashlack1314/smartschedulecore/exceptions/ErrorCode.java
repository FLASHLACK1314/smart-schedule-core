package io.github.flashlack1314.smartschedulecore.exceptions;

import lombok.Getter;

/**
 * 异常错误码枚举类
 * 定义系统中常用的错误码，使用三元组格式：(错误名称, 错误码, 错误描述)
 *
 * @author flash
 */
@Getter
public enum ErrorCode {

    // ========== HTTP标准错误码 ==========

    // 成功响应
    SUCCESS("Success", 200, "操作成功"),

    // 客户端错误 4xx
    BAD_REQUEST("BadRequest", 400, "请求参数错误"),
    UNAUTHORIZED("Unauthorized", 401, "未授权访问"),
    FORBIDDEN("Forbidden", 403, "禁止访问"),
    NOT_FOUND("NotFound", 404, "资源不存在"),
    CONFLICT("Conflict", 409, "数据冲突"),
    BUSINESS_ERROR("BusinessError", 422, "业务逻辑错误"),

    // 服务器错误 5xx
    INTERNAL_ERROR("InternalError", 500, "服务器内部错误"),
    SERVICE_UNAVAILABLE("ServiceUnavailable", 503, "服务不可用"),

    // ========== 通用业务错误码 5000+ ==========
    BODY_ERROR("BodyError", 5000, "请求体格式错误"),
    PARAM_ERROR("ParamError", 5001, "参数错误"),
    DATA_NOT_FOUND("DataNotFound", 5002, "数据不存在"),
    DATA_EXISTS("DataExists", 5003, "数据已存在"),
    OPERATION_FAILED("OperationFailed", 5004, "操作失败"),
    VALIDATION_FAILED("ValidationFailed", 5005, "验证失败"),

    // ========== 业务相关错误码 ==========

    // 用户相关 1001-1999
    USER_NOT_FOUND("UserNotFound", 1001, "用户不存在"),
    USER_DISABLED("UserDisabled", 1002, "用户已被禁用"),
    PASSWORD_ERROR("PasswordError", 1003, "密码错误"),
    EMAIL_ALREADY_EXISTS("EmailAlreadyExists", 1004, "邮箱已存在"),
    PHONE_ALREADY_EXISTS("PhoneAlreadyExists", 1005, "手机号已存在"),

    // 数据库相关 5100-5199
    DATABASE_ERROR("DatabaseError", 5100, "数据库操作失败");

    // 课程相关 2001-2999

    // 排课相关 3001-3999

    /**
     * 错误名称
     */
    private final String name;

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误消息
     */
    private final String errorMessage;

    /**
     * 构造函数
     *
     * @param name         错误名称
     * @param code         错误码
     * @param errorMessage 错误消息
     */
    ErrorCode(String name, Integer code, String errorMessage) {
        this.name = name;
        this.code = code;
        this.errorMessage = errorMessage;
    }

    /**
     * 根据错误码查找错误枚举
     *
     * @param code 错误码
     * @return 错误枚举
     */
    public static ErrorCode fromCode(Integer code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return null;
    }

    /**
     * 根据错误名称查找错误枚举
     *
     * @param name 错误名称
     * @return 错误枚举
     */
    public static ErrorCode fromName(String name) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.getName().equals(name)) {
                return errorCode;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s(%d): %s", name, code, errorMessage);
    }
}