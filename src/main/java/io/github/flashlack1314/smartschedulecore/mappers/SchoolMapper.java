package io.github.flashlack1314.smartschedulecore.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.SchoolDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 学校表 Mapper 接口
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Mapper
public interface SchoolMapper extends BaseMapper<SchoolDO> {
}