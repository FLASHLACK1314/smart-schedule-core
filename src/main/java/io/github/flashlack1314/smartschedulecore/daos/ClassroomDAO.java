package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.ClassroomMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.ClassroomDO;
import org.springframework.stereotype.Repository;

/**
 * 教室数据访问对象 (DAO)
 * <p>
 * 封装 ClassroomMapper，提供教室表的数据访问操作
 * 对应数据库表：`sc_classroom`
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Repository
public class ClassroomDAO extends ServiceImpl<ClassroomMapper, ClassroomDO> {

}
