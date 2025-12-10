package io.github.flashlack1314.smartschedulecore.exceptions.dataBase;

/**
 * 数据库初始化异常
 * 在数据库表初始化过程中发生的异常
 *
 * @author flash
 */
public class DatabaseInitializationException extends RuntimeException {

    public DatabaseInitializationException(String message) {
        super(message);
    }

    public DatabaseInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}