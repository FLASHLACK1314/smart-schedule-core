package io.github.flashlack1314.smartschedulecore.services;

import io.github.flashlack1314.smartschedulecore.models.dto.SchoolDTO;
import io.github.flashlack1314.smartschedulecore.models.dto.SchoolPageDTO;
import io.github.flashlack1314.smartschedulecore.models.vo.SchoolCreateVO;
import io.github.flashlack1314.smartschedulecore.models.vo.SchoolQueryVO;
import io.github.flashlack1314.smartschedulecore.models.vo.SchoolUpdateVO;

/**
 * 学校服务接口
 *
 * @author flash
 */
public interface SchoolService {

    /**
     * 分页查询学校列表
     *
     * @param queryVO 查询条件
     * @return 分页结果
     */
    SchoolPageDTO getSchoolPage(SchoolQueryVO queryVO);

    /**
     * 根据UUID获取学校详情
     *
     * @param schoolUuid 学校UUID
     * @return 学校信息
     */
    SchoolDTO getSchoolByUuid(String schoolUuid);

    /**
     * 创建学校
     *
     * @param createVO 创建参数
     * @return 创建后的学校信息
     */
    SchoolDTO createSchool(SchoolCreateVO createVO);

    /**
     * 更新学校信息
     *
     * @param updateVO 更新参数
     * @return 更新后的学校信息
     */
    SchoolDTO updateSchool(SchoolUpdateVO updateVO);

    /**
     * 删除学校（级联删除）
     *
     * @param schoolUuid 学校UUID
     */
    void deleteSchool(String schoolUuid);
}