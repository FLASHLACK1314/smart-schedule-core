package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.SchedulingConflictMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.SchedulingConflictDO;
import org.springframework.stereotype.Repository;

/**
 * 排课冲突数据访问对象 (DAO)
 * <p>
 * 封装 SchedulingConflictMapper，提供排课冲突表的数据访问操作
 * 对应数据库表：`sc_scheduling_conflict`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class SchedulingConflictDAO extends ServiceImpl<SchedulingConflictMapper, SchedulingConflictDO> {

}
