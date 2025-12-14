package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.StudentMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.StudentDO;
import org.springframework.stereotype.Repository;

/**
 * 学生数据访问对象 (DAO)
 * <p>
 * 封装 StudentMapper，提供学生表的数据访问操作
 * 对应数据库表：`sc_student`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class StudentDAO extends ServiceImpl<StudentMapper, StudentDO> {

}
