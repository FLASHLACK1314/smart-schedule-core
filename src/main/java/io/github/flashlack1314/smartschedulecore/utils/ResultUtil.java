package io.github.flashlack1314.smartschedulecore.utils;

import io.github.flashlack1314.smartschedulecore.models.vo.ResultVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 统一响应结果工具类
 * 提供统一的API响应处理方法
 *
 * @author flash
 */
public class ResultUtil {

    /**
     * 成功响应（无数据）
     *
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<ResultVO<T>> success() {
        ResultVO<T> result = ResultVO.success();
        return ResponseEntity.ok(result);
    }

    /**
     * 成功响应（带消息）
     *
     * @param message 成功消息
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<ResultVO<T>> success(String message) {
        ResultVO<T> result = ResultVO.success(message);
        return ResponseEntity.ok(result);
    }

    /**
     * 成功响应（带数据）
     *
     * @param data 响应数据
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<ResultVO<T>> successWithData(T data) {
        ResultVO<T> result = ResultVO.success(data);
        return ResponseEntity.ok(result);
    }

    /**
     * 成功响应（带消息和数据）
     *
     * @param message 成功消息
     * @param data    响应数据
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<ResultVO<T>> success(String message, T data) {
        ResultVO<T> result = ResultVO.success(message, data);
        return ResponseEntity.ok(result);
    }

    /**
     * 错误响应（默认服务器错误）
     *
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<ResultVO<T>> error() {
        ResultVO<T> result = ResultVO.error();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * 错误响应（自定义消息）
     *
     * @param message 错误消息
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<ResultVO<T>> error(String message) {
        ResultVO<T> result = ResultVO.error(message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * 错误响应（自定义状态码和消息）
     *
     * @param code    HTTP状态码
     * @param message 错误消息
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<ResultVO<T>> error(HttpStatus code, String message) {
        ResultVO<T> result = ResultVO.error(code.value(), message);
        return ResponseEntity.status(code).body(result);
    }

    /**
     * 错误响应（自定义状态码和消息）
     *
     * @param code    HTTP状态码值
     * @param message 错误消息
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<ResultVO<T>> error(int code, String message) {
        ResultVO<T> result = ResultVO.error(code, message);
        return ResponseEntity.status(code).body(result);
    }

    /**
     * 错误响应（自定义状态码、消息和详细错误信息）
     *
     * @param code         HTTP状态码值
     * @param message      错误消息
     * @param errorMessage 详细错误消息
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<ResultVO<T>> error(int code, String message, String errorMessage) {
        ResultVO<T> result = ResultVO.error(code, message, errorMessage);
        return ResponseEntity.status(code).body(result);
    }

    /**
     * 错误响应（根据BusinessException）
     *
     * @param exception 业务异常
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<ResultVO<T>> error(io.github.flashlack1314.smartschedulecore.exceptions.BusinessException exception) {
        ResultVO<T> result = ResultVO.error(exception);
        // 业务错误码可能是4位数，超出HTTP状态码范围，需要判断
        int statusCode = exception.getCode();
        if (statusCode < 100 || statusCode > 999) {
            statusCode = 500; // 使用默认服务器错误状态码
        }
        return ResponseEntity.status(statusCode).body(result);
    }

    // ========== 业务状态码响应方法 ==========

    /**
     * 客户端请求错误（400）
     *
     * @param message 错误消息
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<ResultVO<T>> badRequest(String message) {
        return error(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * 未授权（401）
     *
     * @param message 错误消息
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<ResultVO<T>> unauthorized(String message) {
        return error(HttpStatus.UNAUTHORIZED, message);
    }

    /**
     * 禁止访问（403）
     *
     * @param message 错误消息
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<ResultVO<T>> forbidden(String message) {
        return error(HttpStatus.FORBIDDEN, message);
    }

    /**
     * 资源不存在（404）
     *
     * @param message 错误消息
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<ResultVO<T>> notFound(String message) {
        return error(HttpStatus.NOT_FOUND, message);
    }

    /**
     * 数据冲突（409）
     *
     * @param message 错误消息
     * @return ResponseEntity
     */
    public static <T> ResponseEntity<ResultVO<T>> conflict(String message) {
        return error(HttpStatus.CONFLICT, message);
    }
}