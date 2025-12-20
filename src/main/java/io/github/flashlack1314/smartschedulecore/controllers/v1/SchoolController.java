package io.github.flashlack1314.smartschedulecore.controllers.v1;

import io.github.flashlack1314.smartschedulecore.annotation.RequireRole;
import io.github.flashlack1314.smartschedulecore.models.dto.SchoolDTO;
import io.github.flashlack1314.smartschedulecore.models.dto.SchoolPageDTO;
import io.github.flashlack1314.smartschedulecore.models.vo.ResultVO;
import io.github.flashlack1314.smartschedulecore.models.vo.SchoolCreateVO;
import io.github.flashlack1314.smartschedulecore.models.vo.SchoolQueryVO;
import io.github.flashlack1314.smartschedulecore.models.vo.SchoolUpdateVO;
import io.github.flashlack1314.smartschedulecore.services.SchoolService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 学校管理控制器
 *
 * @author flash
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schools")
public class SchoolController {

    private final SchoolService schoolService;

    /**
     * 分页查询学校列表
     *
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @param schoolName 学校名称（模糊查询）
     * @param schoolCode 学校代码
     * @param schoolType 学校类型
     * @return 学校分页列表
     */
    @GetMapping
    public ResultVO<SchoolPageDTO> getSchools(
            @RequestParam(value = "pageNum", defaultValue = "1") @Min(value = 1, message = "页码必须大于0") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") @Min(value = 1, message = "每页大小必须大于0") @Max(value = 100, message = "每页大小不能超过100") Integer pageSize,
            @RequestParam(value = "schoolName", required = false) String schoolName,
            @RequestParam(value = "schoolCode", required = false) String schoolCode,
            @RequestParam(value = "schoolType", required = false) String schoolType) {

        // 构建查询对象
        SchoolQueryVO queryVO = new SchoolQueryVO();
        queryVO.setPageNum(pageNum);
        queryVO.setPageSize(pageSize);
        queryVO.setSchoolName(schoolName);
        queryVO.setSchoolCode(schoolCode);
        queryVO.setSchoolType(schoolType);

        SchoolPageDTO schoolPage = schoolService.getSchoolPage(queryVO);
        return ResultVO.success("查询成功", schoolPage);
    }

    /**
     * 根据UUID获取学校详情
     *
     * @param uuid 学校UUID
     * @return 学校详情
     */
    @GetMapping("/{uuid}")
    public ResultVO<SchoolDTO> getSchool(@PathVariable("uuid") @NotBlank String uuid) {
        SchoolDTO school = schoolService.getSchoolByUuid(uuid);
        return ResultVO.success("查询成功", school);
    }

    /**
     * 创建学校
     *
     * @param createVO 创建参数
     * @return 创建后的学校信息
     */
    @PostMapping
    @RequireRole("admin")
    public ResultVO<SchoolDTO> createSchool(@Valid @RequestBody SchoolCreateVO createVO) {
        SchoolDTO school = schoolService.createSchool(createVO);
        return ResultVO.success("创建成功", school);
    }

    /**
     * 更新学校信息
     *
     * @param uuid     学校UUID
     * @param updateVO 更新参数
     * @return 更新后的学校信息
     */
    @PutMapping("/{uuid}")
    @RequireRole("admin")
    public ResultVO<SchoolDTO> updateSchool(
            @PathVariable("uuid") @NotBlank String uuid,
            @Valid @RequestBody SchoolUpdateVO updateVO) {
        updateVO.setSchoolUuid(uuid);
        SchoolDTO school = schoolService.updateSchool(updateVO);
        return ResultVO.success("更新成功", school);
    }

    /**
     * 删除学校
     *
     * @param uuid 学校UUID
     * @return 操作结果
     */
    @DeleteMapping("/{uuid}")
    @RequireRole("admin")
    public ResultVO<Void> deleteSchool(@PathVariable("uuid") @NotBlank String uuid) {
        schoolService.deleteSchool(uuid);
        return ResultVO.success("删除成功");
    }
}