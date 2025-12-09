package io.github.flashlack1314.smartschedulecore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author flash
 */
@SpringBootApplication
@MapperScan("io.github.flashlack1314.smartschedulecore.mappers")
public class SmartScheduleCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartScheduleCoreApplication.class, args);
	}

}
