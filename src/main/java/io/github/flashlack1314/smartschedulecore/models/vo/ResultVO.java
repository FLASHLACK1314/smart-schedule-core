package io.github.flashlack1314.smartschedulecore.models.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.flashlack1314.smartschedulecore.exceptions.BusinessException;
import io.github.flashlack1314.smartschedulecore.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 统一响应结果类 (Result Value Object)
 * 用于封装API响应数据，提供统一的响应格式
 *
 * @param <T> 响应数据的泛型类型
 * @author flash
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ResultVO<T> {

    /**
     * 输出信息（替代时间戳）
     */
    private String output;

    /**
     * 响应状态码
     * 200: 成功
     * 0: 成功（兼容版本）
     * 400: 客户端请求错误
     * 401: 未授权
     * 403: 禁止访问
     * 404: 资源不存在
     * 500: 服务器内部错误
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 详细错误消息（可选）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorMessage;

    /**
     * 响应数据
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // ========== 成功响应方法 ==========

    /**
     * 成功响应（无数据）
     *
     * @return 成功响应结果
     */
    public static <T> ResultVO<T> success() {
        ResultVO<T> result = new ResultVO<>();
        result.setOutput("操作成功");
        result.setCode(200);
        result.setMessage("success");
        return result;
    }

    /**
     * 成功响应（带消息）
     *
     * @param message 成功消息
     * @return 成功响应结果
     */
    public static <T> ResultVO<T> success(String message) {
        ResultVO<T> result = new ResultVO<>();
        result.setOutput(message);
        result.setCode(200);
        result.setMessage(message);
        return result;
    }

    /**
     * 成功响应（带数据）
     *
     * @param data 响应数据
     * @return 成功响应结果
     */
    public static <T> ResultVO<T> success(T data) {
        ResultVO<T> result = new ResultVO<>();
        result.setOutput("success");
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    /**
     * 成功响应（带消息和数据）
     *
     * @param message 成功消息
     * @param data    响应数据
     * @return 成功响应结果
     */
    public static <T> ResultVO<T> success(String message, T data) {
        ResultVO<T> result = new ResultVO<>();
        result.setOutput(message);
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    // ========== 错误响应方法 ==========

    /**
     * 错误响应（默认服务器错误）
     *
     * @return 错误响应结果
     */
    public static <T> ResultVO<T> error() {
        ResultVO<T> result = new ResultVO<>();
        result.setOutput("服务器内部错误");
        result.setCode(500);
        result.setMessage("服务器内部错误");
        return result;
    }

    /**
     * 错误响应（自定义消息）
     *
     * @param message 错误消息
     * @return 错误响应结果
     */
    public static <T> ResultVO<T> error(String message) {
        ResultVO<T> result = new ResultVO<>();
        result.setOutput(message);
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    /**
     * 错误响应（自定义状态码和消息）
     *
     * @param code    响应状态码
     * @param message 错误消息
     * @return 错误响应结果
     */
    public static <T> ResultVO<T> error(Integer code, String message) {
        ResultVO<T> result = new ResultVO<>();
        result.setOutput(message);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 错误响应（自定义状态码、消息和详细错误信息）
     *
     * @param code         响应状态码
     * @param message      错误消息
     * @param errorMessage 详细错误消息
     * @return 错误响应结果
     */
    public static <T> ResultVO<T> error(Integer code, String message, String errorMessage) {
        ResultVO<T> result = new ResultVO<>();
        result.setOutput(errorMessage);
        result.setCode(code);
        result.setMessage(message);
        result.setErrorMessage(errorMessage);
        return result;
    }

    /**
     * 根据BusinessException创建错误响应
     *
     * @param exception 业务异常
     * @return 错误响应结果
     */
    public static <T> ResultVO<T> error(BusinessException exception) {
        ResultVO<T> result = new ResultVO<>();
        result.setOutput(exception.getErrorMessage());
        result.setCode(exception.getCode());
        result.setMessage(exception.getMessage());
        result.setErrorMessage(exception.getErrorMessage());
        return result;
    }

    /**
     * 根据ErrorCode创建错误响应
     * 自动复制ErrorCode的详细信息到响应中
     *
     * @param errorCode 错误码枚举
     * @return 错误响应结果
     */
    public static <T> ResultVO<T> error(ErrorCode errorCode) {
        ResultVO<T> result = new ResultVO<>();
        result.setOutput(errorCode.getErrorMessage());
        result.setCode(errorCode.getCode());
        result.setMessage(errorCode.getErrorMessage());
        result.setErrorMessage(errorCode.getErrorMessage());
        return result;
    }

    /**
     * 根据ErrorCode创建错误响应（带自定义消息）
     * 自动复制ErrorCode的详细信息到响应中，但使用自定义message
     *
     * @param errorCode     错误码枚举
     * @param customMessage 自定义响应消息
     * @return 错误响应结果
     */
    public static <T> ResultVO<T> error(ErrorCode errorCode, String customMessage) {
        ResultVO<T> result = new ResultVO<>();
        result.setOutput(errorCode.getErrorMessage());
        result.setCode(errorCode.getCode());
        result.setMessage(customMessage);
        result.setErrorMessage(errorCode.getErrorMessage());
        return result;
    }

    // ========== 常用业务错误响应方法 ==========

    /**
     * 客户端请求错误
     *
     * @param message 错误消息
     * @return 错误响应结果
     */
    public static <T> ResultVO<T> badRequest(String message) {
        return error(400, message);
    }

    /**
     * 未授权
     *
     * @param message 错误消息
     * @return 错误响应结果
     */
    public static <T> ResultVO<T> unauthorized(String message) {
        return error(401, message);
    }

    /**
     * 禁止访问
     *
     * @param message 错误消息
     * @return 错误响应结果
     */
    public static <T> ResultVO<T> forbidden(String message) {
        return error(403, message);
    }

    /**
     * 资源不存在
     *
     * @param message 错误消息
     * @return 错误响应结果
     */
    public static <T> ResultVO<T> notFound(String message) {
        return error(404, message);
    }

    /**
     * 数据已存在
     *
     * @param message 错误消息
     * @return 错误响应结果
     */
    public static <T> ResultVO<T> conflict(String message) {
        return error(409, message);
    }
}