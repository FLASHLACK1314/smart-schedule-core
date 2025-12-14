package io.github.flashlack1314.smartschedulecore.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.CreditHourTypeDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 学时类型表 Mapper 接口
 * <p>
 * 对应数据库表：`sc_credit_hour_type`
 * 继承 MyBatis Plus 的 BaseMapper，提供基础的 CRUD 操作
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Mapper
public interface CreditHourTypeMapper extends BaseMapper<CreditHourTypeDO> {

}
