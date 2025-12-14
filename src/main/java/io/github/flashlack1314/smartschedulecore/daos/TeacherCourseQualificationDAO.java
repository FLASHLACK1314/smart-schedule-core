package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.TeacherCourseQualificationMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.TeacherCourseQualificationDO;
import org.springframework.stereotype.Repository;

/**
 * 教师课程资格数据访问对象 (DAO)
 * <p>
 * 封装 TeacherCourseQualificationMapper，提供教师课程资格表的数据访问操作
 * 对应数据库表：`sc_teacher_course_qualification`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class TeacherCourseQualificationDAO extends ServiceImpl<TeacherCourseQualificationMapper, TeacherCourseQualificationDO> {

}
