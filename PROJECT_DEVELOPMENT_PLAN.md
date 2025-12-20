# 智能排程系统项目开发计划

## 📊 项目现状分析

### 已完成模块

- ✅ **用户认证系统**：注册、登录、Token管理
- ✅ **权限控制系统**：基于角色的拦截器（admin/teacher/student/academic）
- ✅ **密码管理**：密码修改、重置、邮箱验证码功能
- ✅ **邮件服务**：验证码发送、HTML邮件模板
- ✅ **全局异常处理**：统一的错误响应格式
- ✅ **数据库自动初始化**：26张表的完整架构

### 技术架构

- **后端框架**：Spring Boot 3.4.12 + Java 17
- **数据库**：PostgreSQL + Redis
- **ORM**：Spring Data JPA + MyBatis Plus 3.5.15
- **工具库**：Hutool 5.8.40, Lombok
- **邮件服务**：Spring Mail + Thymeleaf
- **测试框架**：JUnit 5, Mockito

### 数据库架构

**6大模块，26张表**的完整排课系统：

| 模块      | 表数量 | 核心表                                                             |
|---------|-----|-----------------------------------------------------------------|
| 用户和角色管理 | 2张  | sc_role, sc_user                                                |
| 学校和组织架构 | 4张  | sc_school, sc_department, sc_major, sc_grade                    |
| 课程体系    | 7张  | sc_course, sc_course_category, sc_course_type等                  |
| 教室资源    | 4张  | sc_classroom, sc_campus, sc_building, sc_classroom_type         |
| 师生管理    | 5张  | sc_teacher, sc_student, sc_teacher_preferences等                 |
| 排课核心    | 4张  | sc_teaching_class, sc_class_assignment, sc_scheduling_conflict等 |

---

## 🎯 开发计划

### 第一阶段：基础数据管理模块（2-3周）

#### 1.1 学校和院系管理 🔥 高优先级

**目标**：建立组织架构基础，为其他模块提供依赖

**接口设计**：

- **SchoolController**
    - `GET /api/v1/schools` - 获取学校列表（分页）
    - `POST /api/v1/schools` - 创建学校 `@RequireRole("admin")`
    - `PUT /api/v1/schools/{uuid}` - 更新学校信息 `@RequireRole("admin")`
    - `DELETE /api/v1/schools/{uuid}` - 删除学校 `@RequireRole("admin")`
    - `POST /api/v1/schools/batch-import` - 批量导入学校 `@RequireRole("admin")`

- **DepartmentController**
    - `GET /api/v1/departments` - 获取院系列表（按学校筛选）
    - `POST /api/v1/departments` - 创建院系 `@RequireRole("admin")`
    - `PUT /api/v1/departments/{uuid}` - 更新院系信息 `@RequireRole("admin")`
    - `DELETE /api/v1/departments/{uuid}` - 删除院系 `@RequireRole("admin")`
    - `POST /api/v1/departments/batch-import` - 批量导入院系 `@RequireRole("admin")`

**核心实体类**：

```java
// SchoolDO - 学校表
public class SchoolDO {
    private String schoolUuid;          // 学校UUID
    private String schoolName;          // 学校名称
    private String schoolCode;          // 学校代码（唯一）
    private String schoolType;          // 学校类型：大学/中学/小学
    private String schoolAddress;       // 学校地址
    private String schoolPhone;         // 联系电话
    private String schoolEmail;         // 邮箱地址
}

// DepartmentDO - 院系表
public class DepartmentDO {
    private String departmentUuid;      // 院系UUID
    private String schoolUuid;          // 所属学校
    private String departmentName;      // 院系名称
    private String departmentCode;      // 院系代码
    private String departmentDean;      // 院长
}
```

#### 1.2 专业和年级管理 🔥 高优先级

**目标**：建立学术体系架构，为 学生管理提供基础

**接口设计**：

- **MajorController**
    - `GET /api/v1/majors` - 获取专业列表（按院系筛选）
    - `POST /api/v1/majors` - 创建专业 `@RequireRole("admin")`
    - `PUT /api/v1/majors/{uuid}` - 更新专业信息 `@RequireRole("admin")`
    - `DELETE /api/v1/majors/{uuid}` - 删除专业 `@RequireRole("admin")`
    - `POST /api/v1/majors/batch-import` - 批量导入专业 `@RequireRole("admin")`

- **GradeController**
    - `GET /api/v1/grades` - 获取年级列表
    - `POST /api/v1/grades` - 创建年级 `@RequireRole("admin")`
    - `PUT /api/v1/grades/{uuid}` - 更新年级信息 `@RequireRole("admin")`
    - `DELETE /api/v1/grades/{uuid}` - 删除年级 `@RequireRole("admin")`

#### 1.3 校区和教学楼管理 🔥 中优先级

**目标**：建立物理空间架构，为教室管理提供基础

**接口设计**：

- **CampusController**
    - `GET /api/v1/campuses` - 获取校区列表（按学校筛选）
    - `POST /api/v1/campuses` - 创建校区 `@RequireRole("admin")`
    - `PUT /api/v1/campuses/{uuid}` - 更新校区信息 `@RequireRole("admin")`
    - `DELETE /api/v1/campuses/{uuid}` - 删除校区 `@RequireRole("admin")`

- **BuildingController**
    - `GET /api/v1/buildings` - 获取教学楼列表（按校区筛选）
    - `POST /api/v1/buildings` - 创建教学楼 `@RequireRole("admin")`
    - `PUT /api/v1/buildings/{uuid}` - 更新教学楼信息 `@RequireRole("admin")`
    - `DELETE /api/v1/buildings/{uuid}` - 删除教学楼 `@RequireRole("admin")`

**批量导入功能设计**：

```java

@PostMapping("/batch-import")
@RequireRole({"admin", "academic"})
public ResultVO<BatchImportResult> batchImport(
        @RequestParam("file") MultipartFile file,
        @RequestParam("type") ImportType type
) {
    // 支持Excel/CSV格式
    // 提供模板下载
    // 行级错误定位
    // 批量回滚机制
    return ResultVO.success(dataImportService.batchImport(file, type));
}
```

---

### 第二阶段：教师和学生管理模块（2-3周）

#### 2.1 教师管理 🔥 高优先级

**目标**：建立教师信息库，为排课提供核心人力资源

**接口设计**：

- **TeacherController**
    - `GET /api/v1/teachers` - 获取教师列表（分页，多条件筛选）
    - `GET /api/v1/teachers/{uuid}` - 获取教师详情
    - `POST /api/v1/teachers` - 创建教师 `@RequireRole({"admin", "academic"})`
    - `PUT /api/v1/teachers/{uuid}` - 更新教师信息 `@RequireRole({"admin", "academic"})`
    - `DELETE /api/v1/teachers/{uuid}` - 删除教师 `@RequireRole("admin")`
    - `POST /api/v1/teachers/batch-import` - 批量导入教师 `@RequireRole({"admin", "academic"})`

- **TeacherPreferenceController**
    - `GET /api/v1/teacher-preferences/{teacherUuid}` - 获取教师偏好
    - `POST /api/v1/teacher-preferences` - 设置教师偏好 `@RequireRole({"admin", "academic", "teacher"})`
    - `PUT /api/v1/teacher-preferences/{teacherUuid}` - 更新教师偏好

- **TeacherQualificationController**
    - `GET /api/v1/teacher-qualifications/{teacherUuid}` - 获取教师授课资格
    - `POST /api/v1/teacher-qualifications` - 添加教师授课资格 `@RequireRole({"admin", "academic"})`
    - `DELETE /api/v1/teacher-qualifications/{id}` - 移除教师授课资格 `@RequireRole({"admin", "academic"})`

**核心实体类**：

```java
// TeacherDO - 教师表
public class TeacherDO {
    private String teacherUuid;         // 教师UUID
    private String schoolUuid;          // 所属学校
    private String userUuid;            // 关联用户账号
    private String departmentUuid;      // 所属院系
    private String teacherTypeUuid;     // 教师类型
    private String teacherCode;         // 教师工号（唯一）
    private String teacherName;         // 教师姓名
    private String teacherEnglishName;  // 英文名
    private String gender;              // 性别
    private String jobTitle;            // 职称
    private String phone;               // 电话
    private String email;               // 邮箱
}

// TeacherPreferencesDO - 教师偏好表
public class TeacherPreferencesDO {
    private String preferenceUuid;      // 偏好UUID
    private String teacherUuid;         // 教师UUID
    private String preferredTimeSlots;  // 偏好时间段（JSON）
    private String preferredCampuses;   // 偏好校区
    private String avoidedTimeSlots;    // 避免时间段
    private Integer maxWeeklyHours;     // 最大周课时
    private String specialRequirements; // 特殊要求
}
```

#### 2.2 学生管理 🔥 高优先级

**目标**：建立学生信息库，为排课提供核心服务对象

**接口设计**：

- **StudentController**
    - `GET /api/v1/students` - 获取学生列表（分页，多条件筛选）
    - `GET /api/v1/students/{uuid}` - 获取学生详情
    - `POST /api/v1/students` - 创建学生 `@RequireRole({"admin", "academic"})`
    - `PUT /api/v1/students/{uuid}` - 更新学生信息 `@RequireRole({"admin", "academic"})`
    - `DELETE /api/v1/students/{uuid}` - 删除学生 `@RequireRole("admin")`
    - `POST /api/v1/students/batch-import` - 批量导入学生 `@RequireRole({"admin", "academic"})`

- **AdministrativeClassController**
    - `GET /api/v1/administrative-classes` - 获取行政班列表
    - `POST /api/v1/administrative-classes` - 创建行政班 `@RequireRole({"admin", "academic"})`
    - `PUT /api/v1/administrative-classes/{uuid}` - 更新行政班信息 `@RequireRole({"admin", "academic"})`
    - `DELETE /api/v1/administrative-classes/{uuid}` - 删除行政班 `@RequireRole("admin")`
    - `POST /api/v1/administrative-classes/batch-import` - 批量导入行政班 `@RequireRole({"admin", "academic"})`

---

### 第三阶段：课程和教学资源管理模块（2-3周）

#### 3.1 课程管理 🔥 高优先级

**目标**：建立课程体系，为排课提供核心内容

**接口设计**：

- **CourseController**
    - `GET /api/v1/courses` - 获取课程列表（分页，多条件筛选）
    - `GET /api/v1/courses/{uuid}` - 获取课程详情
    - `POST /api/v1/courses` - 创建课程 `@RequireRole({"admin", "academic"})`
    - `PUT /api/v1/courses/{uuid}` - 更新课程信息 `@RequireRole({"admin", "academic"})`
    - `DELETE /api/v1/courses/{uuid}` - 删除课程 `@RequireRole("admin")`
    - `POST /api/v1/courses/batch-import` - 批量导入课程 `@RequireRole({"admin", "academic"})`
    - `PUT /api/v1/courses/{uuid}/status` - 启用/禁用课程 `@RequireRole({"admin", "academic"})`

- **CourseMetadataController**
    - `GET /api/v1/course-categories` - 获取课程类别列表
    - `GET /api/v1/course-types` - 获取课程类型列表
    - `GET /api/v1/course-natures` - 获取课程性质列表
    - `GET /api/v1/course-properties` - 获取课程属性列表

**核心实体类**：

```java
// CourseDO - 课程表
public class CourseDO {
    private String courseUuid;                    // 课程UUID
    private String schoolUuid;                    // 所属学校
    private String courseCode;                    // 课程编号（唯一）
    private String courseName;                    // 课程名称
    private String courseEnglishName;            // 英文名称
    private String departmentUuid;                // 开课院系
    private String courseCategoryUuid;           // 课程类别
    private String coursePropertyUuid;           // 课程属性
    private String courseTypeUuid;               // 课程类型
    private String courseNatureUuid;             // 课程性质
    private Integer totalHours;                   // 总学时
    private Integer weekHours;                    // 周学时
    private Integer theoryHours;                  // 理论学时
    private Integer experimentHours;              // 实验学时
    private BigDecimal credit;                    // 学分
    private String theoryClassroomTypeUuid;       // 理论课教室类型
    private String experimentClassroomTypeUuid;   // 实验课教室类型
    private Boolean isEnabled;                    // 是否启用
}
```

#### 3.2 教室管理 🔥 高优先级

**目标**：建立教室资源库，为排课提供核心场地资源

**接口设计**：

- **ClassroomController**
    - `GET /api/v1/classrooms` - 获取教室列表（分页，多条件筛选）
    - `GET /api/v1/classrooms/{uuid}` - 获取教室详情
    - `POST /api/v1/classrooms` - 创建教室 `@RequireRole({"admin", "academic"})`
    - `PUT /api/v1/classrooms/{uuid}` - 更新教室信息 `@RequireRole({"admin", "academic"})`
    - `DELETE /api/v1/classrooms/{uuid}` - 删除教室 `@RequireRole("admin")`
    - `POST /api/v1/classrooms/batch-import` - 批量导入教室 `@RequireRole({"admin", "academic"})`
    - `PUT /api/v1/classrooms/{uuid}/status` - 启用/禁用教室 `@RequireRole({"admin", "academic"})`

- **ClassroomTypeController**
    - `GET /api/v1/classroom-types` - 获取教室类型列表

#### 3.3 学期管理 🔥 中优先级

**目标**：建立时间维度管理，为排课提供时间框架

**接口设计**：

- **SemesterController**
    - `GET /api/v1/semesters` - 获取学期列表
    - `POST /api/v1/semesters` - 创建学期 `@RequireRole({"admin", "academic"})`
    - `PUT /api/v1/semesters/{uuid}` - 更新学期信息 `@RequireRole({"admin", "academic"})`
    - `DELETE /api/v1/semesters/{uuid}` - 删除学期 `@RequireRole("admin")`
    - `PUT /api/v1/semesters/{uuid}/status` - 设置当前学期 `@RequireRole({"admin", "academic"})`

---

### 第四阶段：高级功能和排课准备（1-2周）

#### 4.1 教学班管理 🔥 中优先级

**目标**：建立教学班体系，作为排课的直接操作对象

**接口设计**：

- **TeachingClassController**
    - `GET /api/v1/teaching-classes` - 获取教学班列表
    - `POST /api/v1/teaching-classes` - 创建教学班 `@RequireRole({"admin", "academic"})`
    - `PUT /api/v1/teaching-classes/{uuid}` - 更新教学班信息 `@RequireRole({"admin", "academic"})`
    - `DELETE /api/v1/teaching-classes/{uuid}` - 删除教学班 `@RequireRole("admin")`
    - `POST /api/v1/teaching-classes/batch-import` - 批量导入教学班 `@RequireRole({"admin", "academic"})`

#### 4.2 数据统计和报表 🔥 低优先级

**目标**：提供数据洞察，支持管理决策

**接口设计**：

- **StatisticsController**
    - `GET /api/v1/statistics/overview` - 系统概览统计 `@RequireRole({"admin", "academic"})`
    - `GET /api/v1/statistics/teachers` - 教师统计 `@RequireRole({"admin", "academic"})`
    - `GET /api/v1/statistics/students` - 学生统计 `@RequireRole({"admin", "academic"})`
    - `GET /api/v1/statistics/courses` - 课程统计 `@RequireRole({"admin", "academic"})`
    - `GET /api/v1/statistics/classrooms` - 教室统计 `@RequireRole({"admin", "academic"})`

#### 4.3 文件上传和导出 🔥 中优先级

**目标**：提升用户体验，支持批量操作

**接口设计**：

- **FileController**
    - `POST /api/v1/files/upload-template` - 上传导入模板 `@RequireRole({"admin", "academic"})`
    - `GET /api/v1/files/download-template/{type}` - 下载导入模板
    - `POST /api/v1/files/export/{type}` - 导出数据 `@RequireRole({"admin", "academic"})`

**文件处理功能设计**：

```java
// 支持的导入格式
public enum ImportType {
    SCHOOL("school", "学校信息"),
    DEPARTMENT("department", "院系信息"),
    TEACHER("teacher", "教师信息"),
    STUDENT("student", "学生信息"),
    COURSE("course", "课程信息"),
    CLASSROOM("classroom", "教室信息");
}

// 批量导入结果
public class BatchImportResult {
    private int totalRows;           // 总行数
    private int successCount;        // 成功数量
    private int failureCount;        // 失败数量
    private List<ImportError> errors; // 错误详情
}
```

---

### 第五阶段：排课核心算法（⚠️ 最后阶段）

#### 5.1 排课算法准备 🔥 高优先级

**核心算法组件**：

1. **冲突检测算法**
    - 教师时间冲突检测
    - 教室使用冲突检测
    - 学生班级冲突检测

2. **资源分配算法**
    - 教师-课程匹配
    - 教室-课程匹配
    - 时间段分配

3. **优化算法**
    - 遗传算法优化
    - 模拟退火算法
    - 贪心算法基础版

#### 5.2 排课管理 🔥 高优先级

**接口设计**：

- **SchedulingController**
    - `POST /api/v1/scheduling/auto` - 自动排课 `@RequireRole({"admin", "academic"})`
    - `POST /api/v1/scheduling/manual` - 手动排课 `@RequireRole({"admin", "academic"})`
    - `GET /api/v1/scheduling/results` - 获取排课结果
    - `PUT /api/v1/scheduling/adjust/{id}` - 调整排课 `@RequireRole({"admin", "academic"})`
    - `DELETE /api/v1/scheduling/{id}` - 删除排课 `@RequireRole({"admin", "academic"})`

- **ConflictController**
    - `GET /api/v1/scheduling/conflicts` - 获取排课冲突 `@RequireRole({"admin", "academic"})`
    - `PUT /api/v1/scheduling/resolve-conflict/{id}` - 解决冲突 `@RequireRole({"admin", "academic"})`

**排课核心实体**：

```java
// ClassAssignmentDO - 排课表
public class ClassAssignmentDO {
    private String assignmentUuid;      // 排课UUID
    private String teachingClassUuid;   // 教学班UUID
    private String teacherUuid;         // 教师UUID
    private String classroomUuid;       // 教室UUID
    private String semesterUuid;        // 学期UUID
    private Integer weekDay;            // 星期几（1-7）
    private Integer startSection;       // 开始节次
    private Integer endSection;         // 结束节次
    private Date startDate;             // 开始日期
    private Date endDate;               // 结束日期
    private Integer weekInterval;       // 周期间隔
}

// SchedulingConflictDO - 排课冲突表
public class SchedulingConflictDO {
    private String conflictUuid;        // 冲突UUID
    private String conflictType;        // 冲突类型
    private String description;         // 冲突描述
    private String resource1Uuid;       // 资源1
    private String resource2Uuid;       // 资源2
    private Integer weekDay;            // 星期几
    private Integer section;            // 节次
    private String status;              // 状态
}
```

---

## 📋 开发优先级和时间规划

### 立即开始（第1-3周）🔥

1. **学校和院系管理** - 其他模块的基础依赖
2. **专业和年级管理** - 学生管理的前提条件
3. **校区和教学楼管理** - 教室管理的基础

### 紧急跟进（第4-6周）🚀

4. **教师管理** - 排课的核心人力资源
5. **学生管理** - 排课的核心服务对象

### 中期开发（第7-9周）📈

6. **课程管理** - 排课的核心教学内容
7. **教室管理** - 排课的核心场地资源
8. **学期管理** - 排课的时间维度框架

### 后期完善（第10-11周）🎯

9. **教学班管理** - 排课的直接操作对象
10. **数据统计和报表** - 管理决策支持
11. **文件上传和导出** - 用户体验提升

### 最终目标（第12周+）⭐

12. **排课核心算法** - 项目核心价值实现

---

## 🔧 技术实现规范

### 权限控制规范

```java
// 角色权限矩阵
@RestController
@RequestMapping("/api/v1/xxx")
public class XxxController {

    @GetMapping
    public ResultVO<List<XxxDTO>> list() {
        // 所有角色可访问
    }

    @PostMapping
    @RequireRole({"admin", "academic"})
    public ResultVO<Void> create() {
        // 管理员和教务处可创建
    }

    @DeleteMapping
    @RequireRole("admin")
    public ResultVO<Void> delete() {
        // 仅管理员可删除
    }
}
```

### 批量导入实现规范

```java

@Service
public class BatchImportService {

    public BatchImportResult batchImport(MultipartFile file, ImportType type) {
        try {
            // 1. 文件格式验证
            validateFileFormat(file);

            // 2. 读取Excel/CSV数据
            List<Map<String, Object>> data = readExcelData(file);

            // 3. 数据验证
            ValidationResult validation = validateData(data, type);

            // 4. 批量插入数据库
            if (validation.isValid()) {
                return batchInsert(data, type);
            } else {
                return BatchImportResult.error(validation.getErrors());
            }

        } catch (Exception e) {
            // 5. 异常处理和回滚
            log.error("批量导入失败", e);
            return BatchImportResult.error("导入过程中发生错误");
        }
    }
}
```

### 错误处理规范

```java
// 业务错误码扩展
public enum ErrorCode {
    // 批量导入相关
    BATCH_IMPORT_FAILED("BatchImportFailed", 6001, "批量导入失败"),
    FILE_FORMAT_INVALID("FileFormatInvalid", 6002, "文件格式不正确"),
    DATA_VALIDATION_FAILED("DataValidationFailed", 6003, "数据验证失败"),
    DUPLICATE_DATA("DuplicateData", 6004, "数据重复"),

    // 资源管理相关
    RESOURCE_NOT_FOUND("ResourceNotFound", 6005, "资源不存在"),
    RESOURCE_CONFLICT("ResourceConflict", 6006, "资源冲突"),
    RESOURCE_LIMIT_EXCEEDED("ResourceLimitExceeded", 6007, "资源超出限制");
}
```

---

## 📝 预期成果和价值

### 功能完整性

完成前四阶段后，系统将具备：

- ✅ **完整的教学资源管理系统**
- ✅ **师生信息管理平台**
- ✅ **课程管理功能**
- ✅ **批量数据导入导出能力**
- ✅ **完善的权限控制体系**
- ✅ **标准化的REST API接口**

### 系统价值

1. **独立的教育管理系统**：即使不包含排课功能，也是一个完整的教务管理系统
2. **标准化数据接口**：为排课算法提供高质量、标准化的数据
3. **可扩展架构**：为后续算法升级和功能扩展奠定基础
4. **生产就绪**：具备实际部署和使用能力

### 排课基础

完成前四阶段为智能排课奠定基础：

- **数据完整性**：教师、学生、课程、教室等核心数据完备
- **约束条件**：各种排课约束条件清晰定义
- **优化目标**：教师偏好、教室利用率等优化目标明确
- **冲突检测**：完善的冲突检测机制

---

## 🚀 实施建议

### 开发团队配置建议

- **后端开发**：2-3人，负责API开发和数据库设计
- **前端开发**：1-2人，负责管理界面开发
- **测试人员**：1人，负责功能测试和集成测试
- **产品经理**：1人，负责需求管理和项目协调

### 技术选型建议

- **前端框架**：Vue.js 3 + Element Plus（管理后台友好）
- **文件处理**：Apache POI（Excel处理）
- **数据可视化**：ECharts（统计图表）
- **API文档**：Swagger/OpenAPI 3.0

### 质量保证

- **代码审查**：所有PR必须经过代码审查
- **自动化测试**：单元测试覆盖率不低于80%
- **性能测试**：批量导入性能测试
- **安全测试**：权限控制和安全漏洞测试

这个开发计划为智能排程系统提供了清晰的实施路径，确保每个阶段都有明确的目标和可交付成果，最终构建出一个功能完整、架构优良的教育管理系统。