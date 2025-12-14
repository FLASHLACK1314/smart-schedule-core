package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.MajorMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.MajorDO;
import org.springframework.stereotype.Repository;

/**
 * 专业数据访问对象 (DAO)
 * <p>
 * 封装 MajorMapper，提供专业表的数据访问操作
 * 对应数据库表：`sc_major`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class MajorDAO extends ServiceImpl<MajorMapper, MajorDO> {

}
