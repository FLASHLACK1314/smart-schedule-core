package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.CampusMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.CampusDO;
import org.springframework.stereotype.Repository;

/**
 * 校区数据访问对象 (DAO)
 * <p>
 * 封装 CampusMapper，提供校区表的数据访问操作
 * 对应数据库表：`sc_campus`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class CampusDAO extends ServiceImpl<CampusMapper, CampusDO> {

}
