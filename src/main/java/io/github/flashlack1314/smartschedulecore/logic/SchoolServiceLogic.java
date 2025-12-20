package io.github.flashlack1314.smartschedulecore.logic;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.flashlack1314.smartschedulecore.daos.SchoolDAO;
import io.github.flashlack1314.smartschedulecore.exceptions.BusinessException;
import io.github.flashlack1314.smartschedulecore.exceptions.ErrorCode;
import io.github.flashlack1314.smartschedulecore.models.dto.SchoolDTO;
import io.github.flashlack1314.smartschedulecore.models.dto.SchoolPageDTO;
import io.github.flashlack1314.smartschedulecore.models.entity.SchoolDO;
import io.github.flashlack1314.smartschedulecore.models.vo.SchoolCreateVO;
import io.github.flashlack1314.smartschedulecore.models.vo.SchoolQueryVO;
import io.github.flashlack1314.smartschedulecore.models.vo.SchoolUpdateVO;
import io.github.flashlack1314.smartschedulecore.services.SchoolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 学校服务实现类
 *
 * @author flash
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SchoolServiceLogic implements SchoolService {

    private final SchoolDAO schoolDAO;

    @Override
    public SchoolPageDTO getSchoolPage(SchoolQueryVO queryVO) {
        log.info("分页查询学校列表: pageNum={}, pageSize={}, schoolName='{}', schoolCode='{}', schoolType='{}'",
                queryVO.getPageNum(), queryVO.getPageSize(),
                queryVO.getSchoolName(), queryVO.getSchoolCode(), queryVO.getSchoolType());

        // 构建查询条件
        LambdaQueryWrapper<SchoolDO> wrapper = new LambdaQueryWrapper<>();

        // 添加查询条件 - 修复：只构建一次wrapper
        if (StringUtils.hasText(queryVO.getSchoolName())) {
            log.info("添加学校名称模糊查询条件: {}", queryVO.getSchoolName());
            wrapper.like(SchoolDO::getSchoolName, queryVO.getSchoolName());
        }
        if (StringUtils.hasText(queryVO.getSchoolCode())) {
            log.info("添加学校代码精确查询条件: {}", queryVO.getSchoolCode());
            wrapper.eq(SchoolDO::getSchoolCode, queryVO.getSchoolCode());
        }
        if (StringUtils.hasText(queryVO.getSchoolType())) {
            log.info("添加学校类型精确查询条件: {}", queryVO.getSchoolType());
            wrapper.eq(SchoolDO::getSchoolType, queryVO.getSchoolType());
        }
        // 注意：先获取总数，不要添加ORDER BY

        // 修复：获取总数时不要包含ORDER BY
        long total = schoolDAO.count(wrapper);
        log.info("查询总数: {}", total);

        // 修复：创建分页对象时才添加ORDER BY
        Page<SchoolDO> page = new Page<>(queryVO.getPageNum(), queryVO.getPageSize());
        // 设置手动计算的总数
        page.setTotal(total);

        LambdaQueryWrapper<SchoolDO> pageWrapper = new LambdaQueryWrapper<>();

        // 重新构建查询条件用于分页查询
        if (StringUtils.hasText(queryVO.getSchoolName())) {
            pageWrapper.like(SchoolDO::getSchoolName, queryVO.getSchoolName());
        }
        if (StringUtils.hasText(queryVO.getSchoolCode())) {
            pageWrapper.eq(SchoolDO::getSchoolCode, queryVO.getSchoolCode());
        }
        if (StringUtils.hasText(queryVO.getSchoolType())) {
            pageWrapper.eq(SchoolDO::getSchoolType, queryVO.getSchoolType());
        }
        // 在分页查询中添加ORDER BY
        pageWrapper.orderBy(true, false, SchoolDO::getCreatedAt);

        // 执行分页查询
        IPage<SchoolDO> schoolPage = schoolDAO.page(page, pageWrapper);

        log.info("分页查询结果: total={}, current={}, size={}, pages={}, records.size={}",
                schoolPage.getTotal(), schoolPage.getCurrent(), schoolPage.getSize(),
                schoolPage.getPages(), schoolPage.getRecords().size());

        // 记录查询到的具体学校名称
        for (SchoolDO record : schoolPage.getRecords()) {
            log.info("查询到记录: {} - {}", record.getSchoolUuid(), record.getSchoolName());
        }

        // 使用MyBatis Plus的分页结果构建响应
        return convertToPageDTO(schoolPage);
    }

    @Override
    public SchoolDTO getSchoolByUuid(String schoolUuid) {
        log.debug("根据UUID查询学校: schoolUuid={}", schoolUuid);

        SchoolDO school = schoolDAO.getById(schoolUuid);
        if (school == null) {
            log.warn("学校不存在: schoolUuid={}", schoolUuid);
            throw new BusinessException(ErrorCode.SCHOOL_NOT_FOUND);
        }

        return convertToDTO(school);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SchoolDTO createSchool(SchoolCreateVO createVO) {
        log.info("创建学校: schoolName={}, schoolCode={}", createVO.getSchoolName(), createVO.getSchoolCode());

        // 1. 检查学校代码是否已存在
        LambdaQueryWrapper<SchoolDO> codeWrapper = new LambdaQueryWrapper<>();
        codeWrapper.eq(SchoolDO::getSchoolCode, createVO.getSchoolCode());
        if (schoolDAO.count(codeWrapper) > 0) {
            log.warn("学校代码已存在: schoolCode={}", createVO.getSchoolCode());
            throw new BusinessException(ErrorCode.SCHOOL_CODE_EXISTS);
        }

        // 2. 检查学校名称是否已存在（可选，根据业务需求）
        if (StringUtils.hasText(createVO.getSchoolName())) {
            LambdaQueryWrapper<SchoolDO> nameWrapper = new LambdaQueryWrapper<>();
            nameWrapper.eq(SchoolDO::getSchoolName, createVO.getSchoolName());
            if (schoolDAO.count(nameWrapper) > 0) {
                log.warn("学校名称已存在: schoolName={}", createVO.getSchoolName());
                throw new BusinessException(ErrorCode.SCHOOL_NAME_EXISTS);
            }
        }

        // 3. 创建学校
        SchoolDO school = new SchoolDO();
        BeanUtil.copyProperties(createVO, school);

        // 注释掉手动设置时间，使用数据库默认值
        // java.time.LocalDateTime now = java.time.LocalDateTime.now();
        // school.setCreatedAt(now);
        // school.setUpdatedAt(now);

        boolean saved = schoolDAO.save(school);
        if (!saved) {
            log.error("学校创建失败: {}", createVO);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }

        log.info("学校创建成功: schoolUuid={}", school.getSchoolUuid());
        return convertToDTO(school);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SchoolDTO updateSchool(SchoolUpdateVO updateVO) {
        log.info("更新学校: schoolUuid={}", updateVO.getSchoolUuid());

        // 1. 检查学校是否存在
        SchoolDO existingSchool = schoolDAO.getById(updateVO.getSchoolUuid());
        if (existingSchool == null) {
            log.warn("学校不存在: schoolUuid={}", updateVO.getSchoolUuid());
            throw new BusinessException(ErrorCode.SCHOOL_NOT_FOUND);
        }

        // 2. 检查学校名称唯一性（如果名称发生变化）
        if (StringUtils.hasText(updateVO.getSchoolName()) &&
                !updateVO.getSchoolName().equals(existingSchool.getSchoolName())) {
            LambdaQueryWrapper<SchoolDO> nameWrapper = new LambdaQueryWrapper<>();
            nameWrapper.eq(SchoolDO::getSchoolName, updateVO.getSchoolName())
                    .ne(SchoolDO::getSchoolUuid, updateVO.getSchoolUuid());
            if (schoolDAO.count(nameWrapper) > 0) {
                log.warn("学校名称已存在: schoolName={}", updateVO.getSchoolName());
                throw new BusinessException(ErrorCode.SCHOOL_NAME_EXISTS);
            }
        }

        // 3. 更新学校信息
        BeanUtil.copyProperties(updateVO, existingSchool, "schoolUuid");

        // 注释掉手动设置更新时间，使用数据库触发器或更新时处理
        // existingSchool.setUpdatedAt(java.time.LocalDateTime.now());

        boolean updated = schoolDAO.updateById(existingSchool);
        if (!updated) {
            log.error("学校更新失败: schoolUuid={}", updateVO.getSchoolUuid());
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }

        log.info("学校更新成功: schoolUuid={}", updateVO.getSchoolUuid());
        return convertToDTO(existingSchool);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSchool(String schoolUuid) {
        log.info("删除学校: schoolUuid={}", schoolUuid);

        // 1. 检查学校是否存在
        SchoolDO school = schoolDAO.getById(schoolUuid);
        if (school == null) {
            log.warn("学校不存在: schoolUuid={}", schoolUuid);
            throw new BusinessException(ErrorCode.SCHOOL_NOT_FOUND);
        }

        // 2. 直接删除学校（级联删除由数据库外键约束处理）
        boolean deleted = schoolDAO.removeById(schoolUuid);
        if (!deleted) {
            log.error("学校删除失败: schoolUuid={}", schoolUuid);
            throw new BusinessException(ErrorCode.SCHOOL_DELETE_FAILED);
        }

        log.info("学校删除成功: schoolUuid={}", schoolUuid);
    }

    /**
     * 转换DO到DTO
     *
     * @param schoolDO 学校实体对象
     * @return 学校数据传输对象
     */
    private SchoolDTO convertToDTO(SchoolDO schoolDO) {
        SchoolDTO dto = new SchoolDTO();
        BeanUtil.copyProperties(schoolDO, dto);
        return dto;
    }

    /**
     * 转换IPage到SchoolPageDTO
     *
     * @param schoolPage MyBatis Plus分页结果
     * @return 学校分页响应数据传输对象
     */
    private SchoolPageDTO convertToPageDTO(IPage<SchoolDO> schoolPage) {
        SchoolPageDTO pageDTO = new SchoolPageDTO();

        // 转换数据列表
        pageDTO.setRecords(schoolPage.getRecords().stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList()));

        // 设置分页信息
        pageDTO.setTotal(schoolPage.getTotal());
        pageDTO.setCurrent(schoolPage.getCurrent());
        pageDTO.setSize(schoolPage.getSize());
        pageDTO.setPages(schoolPage.getPages());

        return pageDTO;
    }
}