package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.AdministrativeClassMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.AdministrativeClassDO;
import org.springframework.stereotype.Repository;

/**
 * 行政班数据访问对象 (DAO)
 * <p>
 * 封装 AdministrativeClassMapper，提供行政班表的数据访问操作
 * 对应数据库表：`sc_administrative_class`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class AdministrativeClassDAO extends ServiceImpl<AdministrativeClassMapper, AdministrativeClassDO> {

}
