package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.CourseMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.CourseDO;
import org.springframework.stereotype.Repository;

/**
 * 课程数据访问对象 (DAO)
 * <p>
 * 封装 CourseMapper，提供课程表的数据访问操作
 * 对应数据库表：`sc_course`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class CourseDAO extends ServiceImpl<CourseMapper, CourseDO> {

}
