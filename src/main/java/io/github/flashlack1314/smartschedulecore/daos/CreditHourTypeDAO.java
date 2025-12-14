package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.CreditHourTypeMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.CreditHourTypeDO;
import org.springframework.stereotype.Repository;

/**
 * 学时类型数据访问对象 (DAO)
 * <p>
 * 封装 CreditHourTypeMapper，提供学时类型表的数据访问操作
 * 对应数据库表：`sc_credit_hour_type`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class CreditHourTypeDAO extends ServiceImpl<CreditHourTypeMapper, CreditHourTypeDO> {

}
