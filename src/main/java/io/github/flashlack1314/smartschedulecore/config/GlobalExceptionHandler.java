package io.github.flashlack1314.smartschedulecore.config;

import io.github.flashlack1314.smartschedulecore.exceptions.BusinessException;
import io.github.flashlack1314.smartschedulecore.models.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 统一处理应用程序中的异常，并返回标准ResultVO格式的响应
 *
 * @author flash
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     * 当Service层抛出BusinessException时，会被此方法捕获并转换为ResultVO格式
     *
     * @param exception 业务异常
     * @return ResultVO格式的错误响应
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResultVO<Void>> handleBusinessException(BusinessException exception) {
        log.warn("业务异常: code={}, message={}, errorMessage={}",
                exception.getCode(),
                exception.getMessage(),
                exception.getErrorMessage());

        // 使用ResultVO的error方法处理BusinessException
        // 这样会自动复制ErrorCode的详细信息到响应中
        ResultVO<Void> result = ResultVO.error(exception);

        // 根据业务错误码确定HTTP状态码
        int statusCode = determineHttpStatus(exception.getCode());

        return ResponseEntity.status(statusCode).body(result);
    }

    /**
     * 处理其他未捕获的异常
     *
     * @param exception 异常
     * @return ResultVO格式的错误响应
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultVO<Void>> handleGenericException(Exception exception) {
        log.error("未处理的异常: ", exception);

        ResultVO<Void> result = ResultVO.error("服务器内部错误");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * 根据业务错误码确定对应的HTTP状态码
     *
     * @param businessCode 业务错误码
     * @return HTTP状态码
     */
    private int determineHttpStatus(int businessCode) {
        // 直接使用业务错误码作为HTTP状态码
        // 如果业务错误码超出HTTP状态码范围，Spring会自动处理
        return businessCode;
    }
}