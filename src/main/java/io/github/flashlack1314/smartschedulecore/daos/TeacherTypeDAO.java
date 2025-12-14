package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.TeacherTypeMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.TeacherTypeDO;
import org.springframework.stereotype.Repository;

/**
 * 教师类型数据访问对象 (DAO)
 * <p>
 * 封装 TeacherTypeMapper，提供教师类型表的数据访问操作
 * 对应数据库表：`sc_teacher_type`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class TeacherTypeDAO extends ServiceImpl<TeacherTypeMapper, TeacherTypeDO> {

}
