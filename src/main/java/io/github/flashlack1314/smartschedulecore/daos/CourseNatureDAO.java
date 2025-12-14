package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.CourseNatureMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.CourseNatureDO;
import org.springframework.stereotype.Repository;

/**
 * 课程性质数据访问对象 (DAO)
 * <p>
 * 封装 CourseNatureMapper，提供课程性质表的数据访问操作
 * 对应数据库表：`sc_course_nature`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class CourseNatureDAO extends ServiceImpl<CourseNatureMapper, CourseNatureDO> {

}
