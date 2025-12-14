package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.CourseTypeMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.CourseTypeDO;
import org.springframework.stereotype.Repository;

/**
 * 课程类型数据访问对象 (DAO)
 * <p>
 * 封装 CourseTypeMapper，提供课程类型表的数据访问操作
 * 对应数据库表：`sc_course_type`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class CourseTypeDAO extends ServiceImpl<CourseTypeMapper, CourseTypeDO> {

}
