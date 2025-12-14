package io.github.flashlack1314.smartschedulecore.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.DepartmentDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 院系表 Mapper 接口
 * <p>
 * 对应数据库表：`sc_department`
 * 继承 MyBatis Plus 的 BaseMapper，提供基础的 CRUD 操作
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<DepartmentDO> {

}
