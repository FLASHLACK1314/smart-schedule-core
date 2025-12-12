package io.github.flashlack1314.smartschedulecore.daos;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.flashlack1314.smartschedulecore.mappers.SchoolMapper;
import io.github.flashlack1314.smartschedulecore.models.entity.SchoolDO;
import org.springframework.stereotype.Component;

/**
 * 学校表数据访问对象
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Component
public class SchoolDAO extends ServiceImpl<SchoolMapper, SchoolDO> {

    /**
     * 根据学校代码查询学校
     *
     * @param schoolCode 学校代码
     * @return 学校信息，如果不存在返回 null
     */
    public SchoolDO selectBySchoolCode(String schoolCode) {
        return baseMapper.selectOne(
                new LambdaQueryWrapper<SchoolDO>()
                        .eq(SchoolDO::getSchoolCode, schoolCode)
        );
    }

    /**
     * 根据学校名称查询学校
     *
     * @param schoolName 学校名称
     * @return 学校信息，如果不存在返回 null
     */
    public SchoolDO selectBySchoolName(String schoolName) {
        return baseMapper.selectOne(
                new LambdaQueryWrapper<SchoolDO>()
                        .eq(SchoolDO::getSchoolName, schoolName)
        );
    }
}