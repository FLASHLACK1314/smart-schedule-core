package io.github.flashlack1314.smartschedulecore.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.CourseDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 课程表 Mapper 接口
 * <p>
 * 对应数据库表：`sc_course`
 * 继承 MyBatis Plus 的 BaseMapper，提供基础的 CRUD 操作
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Mapper
public interface CourseMapper extends BaseMapper<CourseDO> {

}
