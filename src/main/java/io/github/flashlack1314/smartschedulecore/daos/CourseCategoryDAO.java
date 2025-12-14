package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.CourseCategoryMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.CourseCategoryDO;
import org.springframework.stereotype.Repository;

/**
 * 课程类别数据访问对象 (DAO)
 * <p>
 * 封装 CourseCategoryMapper，提供课程类别表的数据访问操作
 * 对应数据库表：`sc_course_category`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class CourseCategoryDAO extends ServiceImpl<CourseCategoryMapper, CourseCategoryDO> {

}
