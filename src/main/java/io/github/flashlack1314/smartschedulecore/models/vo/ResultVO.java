package io.github.flashlack1314.smartschedulecore.models.vo;

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
     * 响应状态码
     * 200: 成功
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
     * 响应数据
     */
    private T data;

    /**
     * 时间戳（响应时间）
     */
    private Long timestamp;

    // ========== 成功响应方法 ==========

    /**
     * 成功响应（无数据）
     *
     * @return 成功响应结果
     */
    public static <T> ResultVO<T> success() {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(200);
        result.setMessage("success");
        result.setTimestamp(System.currentTimeMillis());
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
        result.setCode(200);
        result.setMessage(message);
        result.setTimestamp(System.currentTimeMillis());
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
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        result.setTimestamp(System.currentTimeMillis());
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
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        result.setTimestamp(System.currentTimeMillis());
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
        result.setCode(500);
        result.setMessage("服务器内部错误");
        result.setTimestamp(System.currentTimeMillis());
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
        result.setCode(500);
        result.setMessage(message);
        result.setTimestamp(System.currentTimeMillis());
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
        result.setCode(code);
        result.setMessage(message);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }
}