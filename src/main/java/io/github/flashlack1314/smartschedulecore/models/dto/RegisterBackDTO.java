package io.github.flashlack1314.smartschedulecore.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 注册返回数据传输对象
 *
 * @author flash
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RegisterBackDTO {
    private String token;
}
