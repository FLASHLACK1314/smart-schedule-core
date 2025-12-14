package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.ClassAssignmentMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.ClassAssignmentDO;
import org.springframework.stereotype.Repository;

/**
 * 排课数据访问对象 (DAO)
 * <p>
 * 封装 ClassAssignmentMapper，提供排课表的数据访问操作
 * 对应数据库表：`sc_class_assignment`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class ClassAssignmentDAO extends ServiceImpl<ClassAssignmentMapper, ClassAssignmentDO> {

}
