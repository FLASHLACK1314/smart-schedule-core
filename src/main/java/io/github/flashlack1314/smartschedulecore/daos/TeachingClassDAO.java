package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.TeachingClassMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.TeachingClassDO;
import org.springframework.stereotype.Repository;

/**
 * 教学班数据访问对象 (DAO)
 * <p>
 * 封装 TeachingClassMapper，提供教学班表的数据访问操作
 * 对应数据库表：`sc_teaching_class`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class TeachingClassDAO extends ServiceImpl<TeachingClassMapper, TeachingClassDO> {

}
