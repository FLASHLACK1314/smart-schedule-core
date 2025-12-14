package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.SemesterMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.SemesterDO;
import org.springframework.stereotype.Repository;

/**
 * 学期数据访问对象 (DAO)
 * <p>
 * 封装 SemesterMapper，提供学期表的数据访问操作
 * 对应数据库表：`sc_semester`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class SemesterDAO extends ServiceImpl<SemesterMapper, SemesterDO> {

}
