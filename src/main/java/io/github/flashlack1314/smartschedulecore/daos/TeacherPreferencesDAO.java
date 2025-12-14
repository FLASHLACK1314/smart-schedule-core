package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.TeacherPreferencesMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.TeacherPreferencesDO;
import org.springframework.stereotype.Repository;

/**
 * 教师偏好数据访问对象 (DAO)
 * <p>
 * 封装 TeacherPreferencesMapper，提供教师偏好表的数据访问操作
 * 对应数据库表：`sc_teacher_preferences`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class TeacherPreferencesDAO extends ServiceImpl<TeacherPreferencesMapper, TeacherPreferencesDO> {

}
