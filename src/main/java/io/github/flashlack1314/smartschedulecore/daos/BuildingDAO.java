package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.BuildingMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.BuildingDO;
import org.springframework.stereotype.Repository;

/**
 * 教学楼数据访问对象 (DAO)
 * <p>
 * 封装 BuildingMapper，提供教学楼表的数据访问操作
 * 对应数据库表：`sc_building`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class BuildingDAO extends ServiceImpl<BuildingMapper, BuildingDO> {

}
