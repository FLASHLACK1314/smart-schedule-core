package io.github.flashlack1314.smartschedulecore.config;

import io.github.flashlack1314.smartschedulecore.exceptions.BusinessException;
import io.github.flashlack1314.smartschedulecore.models.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
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
     * 处理事务异常
     *
     * @param exception 事务异常
     * @return ResultVO格式的错误响应
     */
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ResultVO<Void>> handleTransactionException(TransactionSystemException exception) {
        log.error("事务异常: ", exception);

        ResultVO<Void> result = ResultVO.error("事务处理失败，请重试");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * 处理数据完整性异常（如外键约束违反）
     *
     * @param exception 数据完整性异常
     * @return ResultVO格式的错误响应
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResultVO<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        log.error("数据完整性异常: ", exception);

        ResultVO<Void> result = ResultVO.error("数据完整性约束违反，请检查数据关联关系");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
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
        // HTTP标准状态码直接返回
        if (businessCode >= 100 && businessCode < 600) {
            return businessCode;
        }

        // 业务错误码映射到HTTP状态码
        if (businessCode == 0 || businessCode == 200) {
            return 200; // 成功
        } else if (businessCode >= 1001 && businessCode <= 1999) {
            // 用户相关错误 1001-1999
            return 400; // Bad Request
        } else if (businessCode >= 2001 && businessCode <= 2999) {
            // 学校相关错误 2001-2999
            return 422; // Unprocessable Entity
        } else if (businessCode >= 3001 && businessCode <= 3999) {
            // 排课相关错误 3001-3999
            return 409; // Conflict
        } else if (businessCode >= 4001 && businessCode <= 4999) {
            // 课程相关错误 4001-4999
            return 422; // Unprocessable Entity
        } else if (businessCode >= 5000 && businessCode <= 5999) {
            // 通用业务错误 5000-5999
            return 400; // Bad Request
        } else if (businessCode >= 6000 && businessCode <= 6999) {
            // 数据验证错误 6000-6999
            return 400; // Bad Request
        } else if (businessCode >= 7000 && businessCode <= 7999) {
            // 权限相关错误 7000-7999
            return 403; // Forbidden
        } else if (businessCode >= 8000 && businessCode <= 8999) {
            // 文件处理错误 8000-8999
            return 422; // Unprocessable Entity
        } else if (businessCode >= 9000 && businessCode <= 9999) {
            // 第三方服务错误 9000-9999
            return 503; // Service Unavailable
        } else {
            // 超出范围的错误码，默认使用400
            return 400;
        }
    }
}