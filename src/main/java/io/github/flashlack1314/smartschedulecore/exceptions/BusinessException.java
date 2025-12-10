package io.github.flashlack1314.smartschedulecore.exceptions;

import lombok.Getter;

/**
 * 业务异常类
 * 用于处理业务逻辑中的异常情况
 *
 * @author flash
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误消息（错误码对应的默认消息）
     */
    private final String message;

    /**
     * 详细错误消息（用户传入的具体错误信息）
     */
    private final String errorMessage;

    /**
     * 构造函数 - 自定义消息和错误码
     *
     * @param errorMessage 详细错误消息
     * @param errorCode    错误码枚举
     */
    public BusinessException(String errorMessage, ErrorCode errorCode) {
        super(errorMessage);
        this.code = errorCode.getCode();
        this.message = errorCode.getErrorMessage();
        this.errorMessage = errorMessage;
    }

    /**
     * 构造函数 - 仅错误码（使用默认消息）
     *
     * @param errorCode 错误码枚举
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getErrorMessage();
        this.errorMessage = errorCode.getErrorMessage();
    }
}