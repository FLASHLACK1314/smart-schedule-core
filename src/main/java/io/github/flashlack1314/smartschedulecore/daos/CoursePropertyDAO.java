package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.CoursePropertyMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.CoursePropertyDO;
import org.springframework.stereotype.Repository;

/**
 * 课程属性数据访问对象 (DAO)
 * <p>
 * 封装 CoursePropertyMapper，提供课程属性表的数据访问操作
 * 对应数据库表：`sc_course_property`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class CoursePropertyDAO extends ServiceImpl<CoursePropertyMapper, CoursePropertyDO> {

}
