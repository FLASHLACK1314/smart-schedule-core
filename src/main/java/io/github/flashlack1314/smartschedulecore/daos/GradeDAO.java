package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.GradeMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.GradeDO;
import org.springframework.stereotype.Repository;

/**
 * 年级数据访问对象 (DAO)
 * <p>
 * 封装 GradeMapper，提供年级表的数据访问操作
 * 对应数据库表：`sc_grade`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class GradeDAO extends ServiceImpl<GradeMapper, GradeDO> {

}
