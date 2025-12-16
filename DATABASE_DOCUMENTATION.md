# Smart Schedule Core - 数据库设计文档

## 项目概述

**项目名称**: Smart Schedule Core (智能排课核心系统)
**数据库名称**: `smart-schedule-core`
**技术栈**: Spring Boot 3.4.12 + JPA + MyBatis Plus + PostgreSQL
**Java版本**: Java 17
**开发者**: flashlack1314

Smart Schedule Core 是一个基于AI的智慧排课系统，旨在解决高校排课复杂性问题，通过智能算法实现教师、教室、课程、学生等资源的优化分配。

---

## 目录

1. [数据库架构概览](#数据库架构概览)
2. [表结构分类](#表结构分类)
3. [核心表详细设计](#核心表详细设计)
4. [表关系图谱](#表关系图谱)
5. [索引策略](#索引策略)
6. [外键约束规则](#外键约束规则)
7. [初始化流程](#初始化流程)

---

## 数据库架构概览

### 统计信息

- **总表数**: 26 张
- **字符集**: UTF-8
- **数据库**: PostgreSQL
- **主键类型**: VARCHAR(64) - Hutool UUID

### 设计原则

1. **标准化命名**: 所有表以 `sc_` (smart-schedule) 为前缀
2. **UUID主键**: 使用Hutool生成的64位UUID作为主键，确保全局唯一性
3. **多学校支持**: 所有业务表包含 `school_uuid` 字段，支持数据隔离
4. **外键完整性**: 严格的外键约束，支持级联更新和删除策略
5. **JSON灵活字段**: 对于动态结构数据使用JSON/JSONB类型存储
6. **时间戳管理**: 所有表包含 `created_at` 和 `updated_at` 字段
7. **索引优化**: 为高频查询字段建立单列和组合索引

---

## 表结构分类

### 模块1：用户和角色管理（3张表，已存在）

#### 1.1 sc_school - 学校表

**表路径**: [resources/sql/sc_school.sql](resources/sql/sc_school.sql)

| 字段名            | 类型           | 约束              | 说明             |
|----------------|--------------|-----------------|----------------|
| school_uuid    | VARCHAR(64)  | PRIMARY KEY     | 学校主键           |
| school_name    | VARCHAR(128) | NOT NULL        | 学校中文名称         |
| school_name_en | VARCHAR(128) | NULL            | 学校英文名称         |
| school_code    | VARCHAR(32)  | UNIQUE NOT NULL | 学校代码           |
| school_type    | VARCHAR(32)  | NULL            | 学校类型（大学/中学/小学） |
| school_address | VARCHAR(256) | NULL            | 学校地址           |
| school_phone   | VARCHAR(32)  | NULL            | 联系电话           |
| school_email   | VARCHAR(64)  | NULL            | 邮箱地址           |
| created_at     | TIMESTAMP    | NOT NULL        | 创建时间           |
| updated_at     | TIMESTAMP    | NOT NULL        | 更新时间           |

**用途**: 顶层表，支持多学校管理，所有业务表通过 school_uuid 关联。

---

#### 1.2 sc_role - 角色表

**表路径**: [resources/sql/sc_role.sql](resources/sql/sc_role.sql)

| 字段名              | 类型            | 约束          | 说明     |
|------------------|---------------|-------------|--------|
| role_uuid        | VARCHAR(64)   | PRIMARY KEY | 角色主键   |
| role_name        | VARCHAR(32)   | NOT NULL    | 角色中文名称 |
| role_name_en     | VARCHAR(32)   | NULL        | 角色英文名称 |
| role_permissions | VARCHAR(1000) | NULL        | 角色权限描述 |
| created_at       | TIMESTAMP     | NOT NULL    | 创建时间   |
| updated_at       | TIMESTAMP     | NOT NULL    | 更新时间   |

**默认数据**: 管理员(admin)、教师(teacher)、学生(student)、教务处老师(academic)

---

#### 1.3 sc_user - 用户表（仅负责认证）

**表路径**: [resources/sql/sc_user.sql](resources/sql/sc_user.sql)

| 字段名              | 类型           | 约束                       | 说明           |
|------------------|--------------|--------------------------|--------------|
| user_uuid        | VARCHAR(64)  | PRIMARY KEY              | 用户主键         |
| user_role_uuid   | VARCHAR(64)  | FK → sc_role, NOT NULL   | 关联角色表        |
| user_school_uuid | VARCHAR(64)  | FK → sc_school, NOT NULL | 关联学校表        |
| user_name        | VARCHAR(32)  | NOT NULL                 | 用户姓名         |
| user_email       | VARCHAR(64)  | NOT NULL                 | 用户邮箱         |
| user_phone_num   | VARCHAR(32)  | NOT NULL                 | 用户手机号        |
| user_password    | VARCHAR(255) | NOT NULL                 | 加密密码(BCrypt) |

**索引**:

- `idx_user_school`: 学校关联索引

**注意**: 学号/工号不存储在用户表中，而是通过关联的业务表获取：

- 教师工号: `sc_teacher.teacher_code`
- 学生学号: `sc_student.student_code`

---

### 模块2：组织架构（3张表）

#### 2.1 sc_department - 院系/部门表

**表路径**: [resources/sql/sc_department.sql](resources/sql/sc_department.sql)

| 字段名                     | 类型           | 约束                       | 说明     |
|-------------------------|--------------|--------------------------|--------|
| department_uuid         | VARCHAR(32)  | PRIMARY KEY              | 部门主键   |
| school_uuid             | VARCHAR(32)  | FK → sc_school, NOT NULL | 关联学校   |
| department_code         | VARCHAR(32)  | UNIQUE NOT NULL          | 部门编码   |
| department_name         | VARCHAR(64)  | NOT NULL                 | 部门名称   |
| department_english_name | VARCHAR(128) | NULL                     | 部门英文名称 |
| department_short_name   | VARCHAR(32)  | NULL                     | 部门简称   |
| is_teaching_department  | BOOLEAN      | DEFAULT TRUE             | 是否开课院系 |
| is_enabled              | BOOLEAN      | DEFAULT TRUE             | 是否启用   |
| created_at              | TIMESTAMP    | NOT NULL                 | 创建时间   |
| updated_at              | TIMESTAMP    | NOT NULL                 | 更新时间   |

**索引**:

- `uk_department_code`: 部门编码唯一索引
- `idx_department_school`: 学校索引

---

#### 2.2 sc_major - 专业表

**表路径**: [resources/sql/sc_major.sql](resources/sql/sc_major.sql)

| 字段名             | 类型          | 约束                           | 说明              |
|-----------------|-------------|------------------------------|-----------------|
| major_uuid      | VARCHAR(64) | PRIMARY KEY                  | 专业主键            |
| school_uuid     | VARCHAR(64) | FK → sc_school, NOT NULL     | 关联学校            |
| department_uuid | VARCHAR(64) | FK → sc_department, NOT NULL | 所属学院            |
| major_code      | VARCHAR(32) | UNIQUE NOT NULL              | 专业代码            |
| major_name      | VARCHAR(64) | NOT NULL                     | 专业名称            |
| education_years | SMALLINT    | NOT NULL                     | 学制(年)           |
| training_level  | VARCHAR(32) | NOT NULL                     | 培养层次(本科/专科/研究生) |
| is_enabled      | BOOLEAN     | DEFAULT TRUE                 | 是否启用            |
| created_at      | TIMESTAMP   | NOT NULL                     | 创建时间            |
| updated_at      | TIMESTAMP   | NOT NULL                     | 更新时间            |

**索引**:

- `uk_major_code`: 专业代码唯一索引
- `idx_major_department`: 所属学院索引

---

#### 2.3 sc_grade - 年级表

**表路径**: [resources/sql/sc_grade.sql](resources/sql/sc_grade.sql)

| 字段名         | 类型          | 约束                       | 说明             |
|-------------|-------------|--------------------------|----------------|
| grade_uuid  | VARCHAR(64) | PRIMARY KEY              | 年级主键           |
| school_uuid | VARCHAR(64) | FK → sc_school, NOT NULL | 关联学校           |
| grade_name  | VARCHAR(32) | NOT NULL                 | 年级名称(如: 2024级) |
| start_year  | INT         | NOT NULL                 | 入学年份           |
| is_enabled  | BOOLEAN     | DEFAULT TRUE             | 是否启用           |
| created_at  | TIMESTAMP   | NOT NULL                 | 创建时间           |
| updated_at  | TIMESTAMP   | NOT NULL                 | 更新时间           |

**索引**:

- `uk_grade_name_school`: 年级名称+学校唯一索引

---

### 模块3：课程体系（7张表）

#### 3.1 sc_course_category - 课程类别表

**表路径**: [resources/sql/sc_course_category.sql](resources/sql/sc_course_category.sql)

| 字段名                  | 类型          | 约束                       | 说明     |
|----------------------|-------------|--------------------------|--------|
| course_category_uuid | VARCHAR(64) | PRIMARY KEY              | 课程类别主键 |
| school_uuid          | VARCHAR(64) | FK → sc_school, NOT NULL | 关联学校   |
| category_name        | VARCHAR(32) | NOT NULL                 | 类别名称   |
| category_code        | VARCHAR(32) | UNIQUE NOT NULL          | 类别编码   |
| created_at           | TIMESTAMP   | NOT NULL                 | 创建时间   |
| updated_at           | TIMESTAMP   | NOT NULL                 | 更新时间   |

**数据示例**: 通识课、专业课、实践课等

---

#### 3.2 sc_course_property - 课程属性表

**表路径**: [resources/sql/sc_course_property.sql](resources/sql/sc_course_property.sql)

| 字段名                  | 类型          | 约束                       | 说明     |
|----------------------|-------------|--------------------------|--------|
| course_property_uuid | VARCHAR(64) | PRIMARY KEY              | 课程属性主键 |
| school_uuid          | VARCHAR(64) | FK → sc_school, NOT NULL | 关联学校   |
| property_name        | VARCHAR(32) | NOT NULL                 | 属性名称   |
| property_code        | VARCHAR(32) | UNIQUE NOT NULL          | 属性编码   |
| created_at           | TIMESTAMP   | NOT NULL                 | 创建时间   |
| updated_at           | TIMESTAMP   | NOT NULL                 | 更新时间   |

**数据示例**: 必修、选修、限选等

---

#### 3.3 sc_course_type - 课程类型表

**表路径**: [resources/sql/sc_course_type.sql](resources/sql/sc_course_type.sql)

| 字段名              | 类型          | 约束                       | 说明     |
|------------------|-------------|--------------------------|--------|
| course_type_uuid | VARCHAR(64) | PRIMARY KEY              | 课程类型主键 |
| school_uuid      | VARCHAR(64) | FK → sc_school, NOT NULL | 关联学校   |
| type_name        | VARCHAR(32) | NOT NULL                 | 类型名称   |
| type_code        | VARCHAR(32) | UNIQUE NOT NULL          | 类型编码   |
| created_at       | TIMESTAMP   | NOT NULL                 | 创建时间   |
| updated_at       | TIMESTAMP   | NOT NULL                 | 更新时间   |

**数据示例**: 理论课、实验课、实践课等

---

#### 3.4 sc_course_nature - 课程性质表

**表路径**: [resources/sql/sc_course_nature.sql](resources/sql/sc_course_nature.sql)

| 字段名                | 类型          | 约束                       | 说明     |
|--------------------|-------------|--------------------------|--------|
| course_nature_uuid | VARCHAR(64) | PRIMARY KEY              | 课程性质主键 |
| school_uuid        | VARCHAR(64) | FK → sc_school, NOT NULL | 关联学校   |
| nature_name        | VARCHAR(32) | NOT NULL                 | 性质名称   |
| nature_code        | VARCHAR(32) | UNIQUE NOT NULL          | 性质编码   |
| created_at         | TIMESTAMP   | NOT NULL                 | 创建时间   |
| updated_at         | TIMESTAMP   | NOT NULL                 | 更新时间   |

**数据示例**: 公共课、专业基础课、专业核心课等

---

#### 3.5 sc_credit_hour_type - 学时类型表

**表路径**: [resources/sql/sc_credit_hour_type.sql](resources/sql/sc_credit_hour_type.sql)

| 字段名                   | 类型          | 约束                       | 说明     |
|-----------------------|-------------|--------------------------|--------|
| credit_hour_type_uuid | VARCHAR(64) | PRIMARY KEY              | 学时类型主键 |
| school_uuid           | VARCHAR(64) | FK → sc_school, NOT NULL | 关联学校   |
| type_name             | VARCHAR(32) | NOT NULL                 | 学时类型名称 |
| type_code             | VARCHAR(32) | UNIQUE NOT NULL          | 学时类型编码 |
| created_at            | TIMESTAMP   | NOT NULL                 | 创建时间   |
| updated_at            | TIMESTAMP   | NOT NULL                 | 更新时间   |

**数据示例**: 理论学时、实验学时、上机学时、实践学时等

---

#### 3.6 sc_course - 课程表（核心表）

**表路径**: [resources/sql/sc_course.sql](resources/sql/sc_course.sql)

| 字段名                            | 类型            | 约束                            | 说明      |
|--------------------------------|---------------|-------------------------------|---------|
| course_uuid                    | VARCHAR(64)   | PRIMARY KEY                   | 课程主键    |
| school_uuid                    | VARCHAR(64)   | FK → sc_school, NOT NULL      | 关联学校    |
| course_code                    | VARCHAR(32)   | UNIQUE NOT NULL               | 课程编号    |
| course_name                    | VARCHAR(64)   | NOT NULL                      | 课程名称    |
| course_english_name            | VARCHAR(128)  | NULL                          | 课程英文名称  |
| department_uuid                | VARCHAR(64)   | FK → sc_department, NOT NULL  | 开课院系    |
| course_category_uuid           | VARCHAR(64)   | FK → sc_course_category       | 课程类别    |
| course_property_uuid           | VARCHAR(64)   | FK → sc_course_property       | 课程属性    |
| course_type_uuid               | VARCHAR(64)   | FK → sc_course_type, NOT NULL | 课程类型    |
| course_nature_uuid             | VARCHAR(64)   | FK → sc_course_nature         | 课程性质    |
| total_hours                    | DECIMAL(10,2) | DEFAULT 0                     | 总学时     |
| week_hours                     | DECIMAL(10,2) | DEFAULT 0                     | 周学时     |
| theory_hours                   | DECIMAL(10,2) | DEFAULT 0                     | 理论学时    |
| experiment_hours               | DECIMAL(10,2) | DEFAULT 0                     | 实验学时    |
| practice_hours                 | DECIMAL(10,2) | DEFAULT 0                     | 实践学时    |
| computer_hours                 | DECIMAL(10,2) | DEFAULT 0                     | 上机学时    |
| credit                         | DECIMAL(10,2) | DEFAULT 0                     | 学分      |
| theory_classroom_type_uuid     | VARCHAR(64)   | FK → sc_classroom_type        | 理论课教室类型 |
| experiment_classroom_type_uuid | VARCHAR(64)   | FK → sc_classroom_type        | 实验课教室类型 |
| practice_classroom_type_uuid   | VARCHAR(64)   | FK → sc_classroom_type        | 实践课教室类型 |
| is_enabled                     | BOOLEAN       | DEFAULT TRUE                  | 是否启用    |
| created_at                     | TIMESTAMP     | NOT NULL                      | 创建时间    |
| updated_at                     | TIMESTAMP     | NOT NULL                      | 更新时间    |

**索引**:

- `uk_course_code`: 课程编号唯一索引
- `idx_course_department`: 开课院系索引
- `idx_course_type`: 课程类型索引
- `idx_course_school`: 学校索引

**设计特点**:

- 学时细分为理论、实验、实践、上机四种类型
- 针对不同学时类型指定不同的教室类型需求
- 支持课程启用/禁用管理

---

#### 3.7 sc_semester - 学期表

**表路径**: [resources/sql/sc_semester.sql](resources/sql/sc_semester.sql)

| 字段名           | 类型          | 约束                       | 说明                       |
|---------------|-------------|--------------------------|--------------------------|
| semester_uuid | VARCHAR(64) | PRIMARY KEY              | 学期主键                     |
| school_uuid   | VARCHAR(64) | FK → sc_school, NOT NULL | 关联学校                     |
| semester_name | VARCHAR(64) | NOT NULL                 | 学期名称(如: 2024-2025学年第1学期) |
| start_date    | DATE        | NOT NULL                 | 学期开始日期                   |
| end_date      | DATE        | NOT NULL                 | 学期结束日期                   |
| is_current    | BOOLEAN     | DEFAULT FALSE            | 是否当前学期                   |
| is_enabled    | BOOLEAN     | DEFAULT TRUE             | 是否启用                     |
| created_at    | TIMESTAMP   | NOT NULL                 | 创建时间                     |
| updated_at    | TIMESTAMP   | NOT NULL                 | 更新时间                     |

**索引**:

- `uk_semester_name_school`: 学期名称+学校唯一索引
- `idx_semester_current`: 当前学期索引

---

### 模块4：教室资源（4张表）

#### 4.1 sc_campus - 校区表

**表路径**: [resources/sql/sc_campus.sql](resources/sql/sc_campus.sql)

| 字段名            | 类型            | 约束                       | 说明   |
|----------------|---------------|--------------------------|------|
| campus_uuid    | VARCHAR(64)   | PRIMARY KEY              | 校区主键 |
| school_uuid    | VARCHAR(64)   | FK → sc_school, NOT NULL | 关联学校 |
| campus_name    | VARCHAR(64)   | NOT NULL                 | 校区名称 |
| campus_code    | VARCHAR(32)   | UNIQUE NOT NULL          | 校区编码 |
| campus_address | VARCHAR(256)  | NULL                     | 校区地址 |
| latitude       | DECIMAL(10,7) | NULL                     | 纬度   |
| longitude      | DECIMAL(10,7) | NULL                     | 经度   |
| is_enabled     | BOOLEAN       | DEFAULT TRUE             | 是否启用 |
| created_at     | TIMESTAMP     | NOT NULL                 | 创建时间 |
| updated_at     | TIMESTAMP     | NOT NULL                 | 更新时间 |

**索引**:

- `uk_campus_code`: 校区编码唯一索引
- `idx_campus_school`: 学校索引

**用途**: 支持多校区管理，包含地理位置信息

---

#### 4.2 sc_building - 教学楼表

**表路径**: [resources/sql/sc_building.sql](resources/sql/sc_building.sql)

| 字段名           | 类型          | 约束                       | 说明    |
|---------------|-------------|--------------------------|-------|
| building_uuid | VARCHAR(64) | PRIMARY KEY              | 教学楼主键 |
| school_uuid   | VARCHAR(64) | FK → sc_school, NOT NULL | 关联学校  |
| campus_uuid   | VARCHAR(64) | FK → sc_campus, NOT NULL | 所属校区  |
| building_name | VARCHAR(64) | NOT NULL                 | 教学楼名称 |
| building_code | VARCHAR(32) | UNIQUE NOT NULL          | 教学楼编码 |
| floor_count   | SMALLINT    | NOT NULL                 | 楼层数   |
| is_enabled    | BOOLEAN     | DEFAULT TRUE             | 是否启用  |
| created_at    | TIMESTAMP   | NOT NULL                 | 创建时间  |
| updated_at    | TIMESTAMP   | NOT NULL                 | 更新时间  |

**索引**:

- `uk_building_code`: 教学楼编码唯一索引
- `idx_building_campus`: 校区索引

---

#### 4.3 sc_classroom_type - 教室类型表

**表路径**: [resources/sql/sc_classroom_type.sql](resources/sql/sc_classroom_type.sql)

| 字段名                 | 类型          | 约束                       | 说明     |
|---------------------|-------------|--------------------------|--------|
| classroom_type_uuid | VARCHAR(64) | PRIMARY KEY              | 教室类型主键 |
| school_uuid         | VARCHAR(64) | FK → sc_school, NOT NULL | 关联学校   |
| type_name           | VARCHAR(32) | NOT NULL                 | 类型名称   |
| type_code           | VARCHAR(32) | UNIQUE NOT NULL          | 类型编码   |
| created_at          | TIMESTAMP   | NOT NULL                 | 创建时间   |
| updated_at          | TIMESTAMP   | NOT NULL                 | 更新时间   |

**数据示例**: 普通教室、多媒体教室、实验室、机房、报告厅等

---

#### 4.4 sc_classroom - 教室表（核心表）

**表路径**: [resources/sql/sc_classroom.sql](resources/sql/sc_classroom.sql)

| 字段名                        | 类型            | 约束                               | 说明                          |
|----------------------------|---------------|----------------------------------|-----------------------------|
| classroom_uuid             | VARCHAR(64)   | PRIMARY KEY                      | 教室主键                        |
| school_uuid                | VARCHAR(64)   | FK → sc_school, NOT NULL         | 关联学校                        |
| campus_uuid                | VARCHAR(64)   | FK → sc_campus, NOT NULL         | 所属校区                        |
| building_uuid              | VARCHAR(64)   | FK → sc_building, NOT NULL       | 所属教学楼                       |
| classroom_type_uuid        | VARCHAR(64)   | FK → sc_classroom_type, NOT NULL | 教室类型                        |
| classroom_number           | VARCHAR(32)   | UNIQUE NOT NULL                  | 教室编号                        |
| classroom_name             | VARCHAR(64)   | NOT NULL                         | 教室名称                        |
| floor                      | VARCHAR(4)    | NOT NULL                         | 楼层                          |
| capacity                   | INT           | NOT NULL                         | 教室容量                        |
| examination_capacity       | INT           | NULL                             | 考场容量                        |
| area                       | DECIMAL(10,2) | NULL                             | 教室面积(平方米)                   |
| tags                       | JSONB         | NULL                             | 教室标签(数组，如：["智慧教室", "录播教室"]) |
| is_multimedia              | BOOLEAN       | DEFAULT FALSE                    | 是否多媒体教室                     |
| is_air_conditioned         | BOOLEAN       | DEFAULT FALSE                    | 是否有空调                       |
| is_examination_room        | BOOLEAN       | DEFAULT FALSE                    | 是否考场                        |
| management_department_uuid | VARCHAR(64)   | FK → sc_department               | 管理部门                        |
| is_enabled                 | BOOLEAN       | DEFAULT TRUE                     | 是否启用                        |
| created_at                 | TIMESTAMP     | NOT NULL                         | 创建时间                        |
| updated_at                 | TIMESTAMP     | NOT NULL                         | 更新时间                        |

**索引**:

- `uk_classroom_number`: 教室编号唯一索引
- `idx_classroom_campus_building`: 校区+教学楼组合索引
- `idx_classroom_capacity`: 容量索引
- `idx_classroom_type`: 类型索引

**设计特点**:

- 教室标签使用JSONB数组存储，支持多标签
- 区分普通容量和考场容量
- 支持教室状态管理

---

### 模块5：师生管理（5张表）

#### 5.1 sc_teacher_type - 教师类型表

**表路径**: [resources/sql/sc_teacher_type.sql](resources/sql/sc_teacher_type.sql)

| 字段名               | 类型          | 约束                       | 说明     |
|-------------------|-------------|--------------------------|--------|
| teacher_type_uuid | VARCHAR(64) | PRIMARY KEY              | 教师类型主键 |
| school_uuid       | VARCHAR(64) | FK → sc_school, NOT NULL | 关联学校   |
| type_name         | VARCHAR(32) | NOT NULL                 | 类型名称   |
| created_at        | TIMESTAMP   | NOT NULL                 | 创建时间   |
| updated_at        | TIMESTAMP   | NOT NULL                 | 更新时间   |

**数据示例**: 专任教师、兼职教师、外聘教师等

---

#### 5.2 sc_teacher - 教师表（核心表）

**表路径**: [resources/sql/sc_teacher.sql](resources/sql/sc_teacher.sql)

| 字段名                  | 类型           | 约束                             | 说明                 |
|----------------------|--------------|--------------------------------|--------------------|
| teacher_uuid         | VARCHAR(64)  | PRIMARY KEY                    | 教师主键               |
| school_uuid          | VARCHAR(64)  | FK → sc_school, NOT NULL       | 关联学校               |
| user_uuid            | VARCHAR(64)  | FK → sc_user                   | 关联用户账号             |
| department_uuid      | VARCHAR(64)  | FK → sc_department, NOT NULL   | 所属院系               |
| teacher_type_uuid    | VARCHAR(64)  | FK → sc_teacher_type, NOT NULL | 教师类型               |
| teacher_code         | VARCHAR(32)  | UNIQUE NOT NULL                | 教师工号               |
| teacher_name         | VARCHAR(32)  | NOT NULL                       | 教师姓名               |
| teacher_english_name | VARCHAR(128) | NULL                           | 教师英文名              |
| gender               | BOOLEAN      | NOT NULL                       | 性别(false:女 true:男) |
| job_title            | VARCHAR(32)  | NULL                           | 教师职称               |
| phone                | VARCHAR(16)  | NULL                           | 教师电话               |
| email                | VARCHAR(64)  | NULL                           | 教师邮箱               |
| created_at           | TIMESTAMP    | NOT NULL                       | 创建时间               |
| updated_at           | TIMESTAMP    | NOT NULL                       | 更新时间               |

**索引**:

- `uk_teacher_code`: 教师工号唯一索引
- `idx_teacher_department`: 所属院系索引
- `idx_teacher_name`: 教师姓名索引

---

#### 5.3 sc_teacher_preferences - 教师偏好表

**表路径**: [resources/sql/sc_teacher_preferences.sql](resources/sql/sc_teacher_preferences.sql)

| 字段名                           | 类型          | 约束                        | 说明      |
|-------------------------------|-------------|---------------------------|---------|
| preference_uuid               | VARCHAR(64) | PRIMARY KEY               | 偏好主键    |
| school_uuid                   | VARCHAR(64) | FK → sc_school, NOT NULL  | 关联学校    |
| teacher_uuid                  | VARCHAR(64) | FK → sc_teacher, NOT NULL | 关联教师    |
| preferred_time                | JSONB       | NULL                      | 偏好时间段   |
| avoided_time                  | JSONB       | NULL                      | 避开时间段   |
| preferred_campus_uuid         | VARCHAR(64) | FK → sc_campus            | 偏好校区    |
| preferred_classroom_type_uuid | VARCHAR(64) | FK → sc_classroom_type    | 偏好教室类型  |
| max_courses_per_day           | SMALLINT    | NULL                      | 每天最多课程数 |
| max_consecutive_courses       | SMALLINT    | NULL                      | 最多连续课程数 |
| created_at                    | TIMESTAMP   | NOT NULL                  | 创建时间    |
| updated_at                    | TIMESTAMP   | NOT NULL                  | 更新时间    |

**索引**:

- `idx_preference_teacher`: 教师索引

**用途**: 存储教师排课偏好，为智能排课算法提供约束条件

**JSON字段格式示例**:

```json
// preferred_time / avoided_time
{
  "weekday": [
    1,
    2,
    3
  ],
  // 星期几(1-7)
  "period": [
    1,
    2,
    3,
    4
  ]
  // 第几节课
}
```

---

#### 5.4 sc_teacher_course_qualification - 教师课程资格表

**表路径**: [resources/sql/sc_teacher_course_qualification.sql](resources/sql/sc_teacher_course_qualification.sql)

| 字段名                 | 类型          | 约束                        | 说明                      |
|---------------------|-------------|---------------------------|-------------------------|
| qualification_uuid  | VARCHAR(64) | PRIMARY KEY               | 资格主键                    |
| school_uuid         | VARCHAR(64) | FK → sc_school, NOT NULL  | 关联学校                    |
| teacher_uuid        | VARCHAR(64) | FK → sc_teacher, NOT NULL | 关联教师                    |
| course_uuid         | VARCHAR(64) | FK → sc_course, NOT NULL  | 关联课程                    |
| qualification_level | SMALLINT    | NOT NULL                  | 资格等级(1-5级)              |
| is_primary          | BOOLEAN     | DEFAULT FALSE             | 是否主讲                    |
| approval_status     | SMALLINT    | DEFAULT 0                 | 审批状态(0:待审批 1:已通过 2:已拒绝) |
| approved_by         | VARCHAR(64) | FK → sc_user              | 审批人                     |
| approved_at         | TIMESTAMP   | NULL                      | 审批时间                    |
| created_at          | TIMESTAMP   | NOT NULL                  | 创建时间                    |
| updated_at          | TIMESTAMP   | NOT NULL                  | 更新时间                    |

**索引**:

- `idx_qualification_teacher`: 教师索引
- `idx_qualification_course`: 课程索引
- `idx_qualification_status`: 审批状态索引

**用途**: 管理教师授课资格，确保教师只能教授其有资格的课程

---

#### 5.5 sc_student - 学生表

**表路径**: [resources/sql/sc_student.sql](resources/sql/sc_student.sql)

| 字段名                       | 类型          | 约束                           | 说明                 |
|---------------------------|-------------|------------------------------|--------------------|
| student_uuid              | VARCHAR(64) | PRIMARY KEY                  | 学生主键               |
| school_uuid               | VARCHAR(64) | FK → sc_school, NOT NULL     | 关联学校               |
| user_uuid                 | VARCHAR(64) | FK → sc_user                 | 关联用户账号             |
| department_uuid           | VARCHAR(64) | FK → sc_department, NOT NULL | 所属学院               |
| major_uuid                | VARCHAR(64) | FK → sc_major, NOT NULL      | 所属专业               |
| grade_uuid                | VARCHAR(64) | FK → sc_grade, NOT NULL      | 年级                 |
| administrative_class_uuid | VARCHAR(64) | FK → sc_administrative_class | 所属行政班              |
| student_code              | VARCHAR(32) | UNIQUE NOT NULL              | 学号                 |
| student_name              | VARCHAR(32) | NOT NULL                     | 学生姓名               |
| gender                    | BOOLEAN     | NOT NULL                     | 性别(false:女 true:男) |
| is_graduated              | BOOLEAN     | DEFAULT FALSE                | 是否毕业               |
| created_at                | TIMESTAMP   | NOT NULL                     | 创建时间               |
| updated_at                | TIMESTAMP   | NOT NULL                     | 更新时间               |

**索引**:

- `uk_student_code`: 学号唯一索引
- `idx_student_department`: 学院索引
- `idx_student_major`: 专业索引
- `idx_student_class`: 班级索引
- `idx_student_department_major_class`: 院系+专业+班级组合索引

---

### 模块6：排课核心（4张表）

#### 6.1 sc_administrative_class - 行政班表

**表路径**: [resources/sql/sc_administrative_class.sql](resources/sql/sc_administrative_class.sql)

| 字段名                       | 类型          | 约束                           | 说明    |
|---------------------------|-------------|------------------------------|-------|
| administrative_class_uuid | VARCHAR(64) | PRIMARY KEY                  | 行政班主键 |
| school_uuid               | VARCHAR(64) | FK → sc_school, NOT NULL     | 关联学校  |
| department_uuid           | VARCHAR(64) | FK → sc_department, NOT NULL | 所属院系  |
| major_uuid                | VARCHAR(64) | FK → sc_major, NOT NULL      | 所属专业  |
| grade_uuid                | VARCHAR(64) | FK → sc_grade, NOT NULL      | 年级    |
| class_code                | VARCHAR(32) | UNIQUE NOT NULL              | 班级编号  |
| class_name                | VARCHAR(64) | NOT NULL                     | 班级名称  |
| student_count             | INT         | DEFAULT 0                    | 学生人数  |
| counselor_uuid            | VARCHAR(64) | FK → sc_teacher              | 辅导员   |
| monitor_uuid              | VARCHAR(64) | FK → sc_student              | 班长    |
| is_enabled                | BOOLEAN     | DEFAULT TRUE                 | 是否启用  |
| created_at                | TIMESTAMP   | NOT NULL                     | 创建时间  |
| updated_at                | TIMESTAMP   | NOT NULL                     | 更新时间  |

**索引**:

- `uk_class_code`: 班级编号唯一索引
- `idx_class_department_major`: 院系+专业组合索引
- `idx_class_grade`: 年级索引

**用途**: 行政班是学生日常管理的基本单位

---

#### 6.2 sc_teaching_class - 教学班表（核心表）

**表路径**: [resources/sql/sc_teaching_class.sql](resources/sql/sc_teaching_class.sql)

| 字段名                    | 类型          | 约束                           | 说明             |
|------------------------|-------------|------------------------------|----------------|
| teaching_class_uuid    | VARCHAR(64) | PRIMARY KEY                  | 教学班主键          |
| school_uuid            | VARCHAR(64) | FK → sc_school, NOT NULL     | 关联学校           |
| semester_uuid          | VARCHAR(64) | FK → sc_semester, NOT NULL   | 学期             |
| course_uuid            | VARCHAR(64) | FK → sc_course, NOT NULL     | 课程             |
| department_uuid        | VARCHAR(64) | FK → sc_department, NOT NULL | 开课院系           |
| teaching_class_code    | VARCHAR(32) | UNIQUE NOT NULL              | 教学班编号          |
| teaching_class_name    | VARCHAR(64) | NOT NULL                     | 教学班名称          |
| administrative_classes | JSONB       | NOT NULL                     | 包含的行政班(UUID数组) |
| is_compulsory          | BOOLEAN     | DEFAULT TRUE                 | 是否必修课          |
| class_size             | INT         | DEFAULT 0                    | 班级规模           |
| actual_student_count   | INT         | DEFAULT 0                    | 实际学生人数         |
| is_enabled             | BOOLEAN     | DEFAULT TRUE                 | 是否启用           |
| created_at             | TIMESTAMP   | NOT NULL                     | 创建时间           |
| updated_at             | TIMESTAMP   | NOT NULL                     | 更新时间           |

**索引**:

- `uk_teaching_class_code`: 教学班编号唯一索引
- `idx_teaching_class_semester`: 学期索引
- `idx_teaching_class_course`: 课程索引
- `idx_teaching_class_department`: 院系索引

**设计特点**:

- 教学班是排课的基本单位
- 使用JSONB数组存储包含的行政班级UUID
- 区分必修课和选修课

**JSON字段格式示例**:

```json
// administrative_classes
[
  "uuid1",
  "uuid2",
  "uuid3"
]
```

---

#### 6.3 sc_class_assignment - 排课表（最核心表）

**表路径**: [resources/sql/sc_class_assignment.sql](resources/sql/sc_class_assignment.sql)

| 字段名                   | 类型            | 约束                                 | 说明             |
|-----------------------|---------------|------------------------------------|----------------|
| class_assignment_uuid | VARCHAR(64)   | PRIMARY KEY                        | 排课主键           |
| school_uuid           | VARCHAR(64)   | FK → sc_school, NOT NULL           | 关联学校           |
| semester_uuid         | VARCHAR(64)   | FK → sc_semester, NOT NULL         | 学期             |
| course_uuid           | VARCHAR(64)   | FK → sc_course, NOT NULL           | 课程             |
| teacher_uuid          | VARCHAR(64)   | FK → sc_teacher, NOT NULL          | 教师             |
| teaching_class_uuid   | VARCHAR(64)   | FK → sc_teaching_class, NOT NULL   | 教学班            |
| campus_uuid           | VARCHAR(64)   | FK → sc_campus, NOT NULL           | 校区             |
| building_uuid         | VARCHAR(64)   | FK → sc_building, NOT NULL         | 教学楼            |
| classroom_uuid        | VARCHAR(64)   | FK → sc_classroom, NOT NULL        | 教室             |
| classroom_type_uuid   | VARCHAR(64)   | FK → sc_classroom_type, NOT NULL   | 教室类型           |
| credit_hour_type_uuid | VARCHAR(64)   | FK → sc_credit_hour_type, NOT NULL | 学时类型           |
| teaching_hours        | DECIMAL(10,2) | DEFAULT 0                          | 教师实际授课学时       |
| scheduled_hours       | DECIMAL(10,2) | DEFAULT 0                          | 已排课学时          |
| total_hours           | DECIMAL(10,2) | DEFAULT 0                          | 总需学时           |
| class_time            | JSONB         | NOT NULL                           | 上课时间(周次、星期、节次) |
| specified_time        | JSONB         | NULL                               | 指定固定时间         |
| consecutive_sessions  | SMALLINT      | DEFAULT 2                          | 连堂节数           |
| scheduling_priority   | SMALLINT      | DEFAULT 100                        | 排课优先级          |
| created_at            | TIMESTAMP     | NOT NULL                           | 创建时间           |
| updated_at            | TIMESTAMP     | NOT NULL                           | 更新时间           |

**索引**:

- `idx_assignment_semester`: 学期索引
- `idx_assignment_teacher`: 教师索引
- `idx_assignment_classroom`: 教室索引
- `idx_assignment_course`: 课程索引
- `idx_assignment_teaching_class`: 教学班索引
- `idx_assignment_priority`: 优先级索引
- `idx_assignment_school_semester`: 学校+学期组合索引

**外键** (共11个外键，系统最复杂表):

- `school_uuid` → `sc_school.school_uuid`
- `semester_uuid` → `sc_semester.semester_uuid`
- `course_uuid` → `sc_course.course_uuid`
- `teacher_uuid` → `sc_teacher.teacher_uuid`
- `teaching_class_uuid` → `sc_teaching_class.teaching_class_uuid`
- `campus_uuid` → `sc_campus.campus_uuid`
- `building_uuid` → `sc_building.building_uuid`
- `classroom_uuid` → `sc_classroom.classroom_uuid`
- `classroom_type_uuid` → `sc_classroom_type.classroom_type_uuid`
- `credit_hour_type_uuid` → `sc_credit_hour_type.credit_hour_type_uuid`

**设计特点**:

- 排课表是整个系统的核心业务表
- class_time 使用JSONB存储上课时间(周次、星期、节次)
- specified_time 用于指定固定上课时间
- 支持排课优先级管理
- 区分教学学时和排课学时

**JSON字段格式示例**:

```json
// class_time
{
  "weeks": [
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8
  ],
  // 周次
  "weekday": 1,
  // 星期几(1-7)
  "periods": [
    1,
    2
  ]
  // 第几节课
}

// specified_time
{
  "weekday": 3,
  // 星期三
  "periods": [
    1,
    2
  ]
  // 第1-2节
}
```

---

#### 6.4 sc_scheduling_conflict - 排课冲突表

**表路径**: [resources/sql/sc_scheduling_conflict.sql](resources/sql/sc_scheduling_conflict.sql)

| 字段名                    | 类型           | 约束                                 | 说明                                |
|------------------------|--------------|------------------------------------|-----------------------------------|
| conflict_uuid          | VARCHAR(64)  | PRIMARY KEY                        | 冲突主键                              |
| school_uuid            | VARCHAR(64)  | FK → sc_school, NOT NULL           | 关联学校                              |
| semester_uuid          | VARCHAR(64)  | FK → sc_semester, NOT NULL         | 学期                                |
| first_assignment_uuid  | VARCHAR(64)  | FK → sc_class_assignment, NOT NULL | 第一个排课                             |
| second_assignment_uuid | VARCHAR(64)  | FK → sc_class_assignment, NOT NULL | 第二个排课                             |
| conflict_type          | SMALLINT     | NOT NULL                           | 冲突类型(1:教师 2:教室 3:班级 4:其他)         |
| conflict_time          | JSONB        | NOT NULL                           | 冲突时间                              |
| description            | VARCHAR(255) | NULL                               | 冲突描述                              |
| resolution_status      | SMALLINT     | DEFAULT 0                          | 解决状态(0:未解决 1:已解决 2:忽略)            |
| resolution_method      | SMALLINT     | NULL                               | 解决方法(1:调整第一个 2:调整第二个 3:同时调整 4:其他) |
| resolution_notes       | VARCHAR(255) | NULL                               | 解决备注                              |
| resolved_by            | VARCHAR(64)  | FK → sc_user                       | 解决人                               |
| resolved_at            | TIMESTAMP    | NULL                               | 解决时间                              |
| created_at             | TIMESTAMP    | NOT NULL                           | 创建时间                              |
| updated_at             | TIMESTAMP    | NOT NULL                           | 更新时间                              |

**索引**:

- `idx_conflict_semester`: 学期索引
- `idx_conflict_first_assignment`: 第一个排课索引
- `idx_conflict_second_assignment`: 第二个排课索引
- `idx_conflict_type`: 冲突类型索引
- `idx_conflict_resolution_status`: 解决状态索引
- `idx_conflict_status_type`: 状态+类型组合索引

**用途**:

- 记录排课冲突(教师冲突、教室冲突、班级冲突等)
- 支持冲突解决流程管理
- 可创建视图快速查看未解决冲突

---

## 表关系图谱

### 核心业务关系链

```
用户权限管理
sc_school (学校) ─── 顶层表，所有表都关联
    │
    ├─→ sc_role (角色)
    │       └─→ sc_user (用户)
    │               ├─→ sc_teacher (教师)
    │               └─→ sc_student (学生)
    │
    ├─→ sc_department (院系，树形结构)
    │       ├─→ sc_major (专业)
    │       │       └─→ sc_administrative_class (行政班)
    │       │               └─→ sc_student (学生)
    │       ├─→ sc_teacher (教师)
    │       └─→ sc_course (课程)
    │
    ├─→ sc_campus (校区)
    │       └─→ sc_building (教学楼)
    │               └─→ sc_classroom (教室)
    │
    ├─→ 课程字典表（5张）
    │       └─→ sc_course (课程)
    │
    └─→ sc_semester (学期)
            └─→ sc_teaching_class (教学班)
                    └─→ sc_class_assignment (排课表)
                            └─→ sc_scheduling_conflict (排课冲突表)

教师管理
sc_teacher (教师)
    ├─→ sc_teacher_preferences (教师偏好)
    └─→ sc_teacher_course_qualification (教师课程资格)
            └─→ sc_course (课程)
```

### 多对多关系

1. **教师 ←→ 课程**
    - 通过 `sc_teacher_course_qualification` 实现
    - 记录教师授课资格和审批状态

2. **教学班 ←→ 行政班**
    - 通过 `sc_teaching_class.administrative_classes` JSONB字段实现
    - 一个教学班可以包含多个行政班

3. **排课 ←→ 冲突**
    - 通过 `sc_scheduling_conflict` 的双向关联实现
    - 一个排课可能与多个其他排课产生冲突

---

## 索引策略

### 索引类型分布

1. **唯一索引 (UNIQUE)**
    - 用于业务主键: 编号、代码、名称等
    - 确保数据唯一性

2. **单列索引 (INDEX)**
    - 高频查询字段: 状态、类型、日期等
    - 外键字段: 提升JOIN性能

3. **组合索引 (COMPOSITE INDEX)**
    - 多条件查询: 院系+专业、校区+楼栋等
    - 排序查询: 状态+类型等

### 索引命名规范

- 唯一索引: `uk_{table}_{column}`
- 单列索引: `idx_{table}_{column}`
- 组合索引: `idx_{table}_{column1}_{column2}`

---

## 外键约束规则

### 删除策略 (ON DELETE)

1. **RESTRICT** - 禁止删除
    - 用于核心关联，防止误删
    - 示例: 课程类型、教室类型、部门等

2. **CASCADE** - 级联删除
    - 用于从属关系，主记录删除时同步删除
    - 示例: 校区删除时删除下属教学楼

3. **SET NULL** - 设置为空
    - 用于可选关联，删除时保留记录但清空引用
    - 示例: 审批人、辅导员等

### 更新策略 (ON UPDATE)

- 所有外键统一使用 **CASCADE** (级联更新)
- 确保主键变更时自动更新所有引用

---

## 初始化流程

### 表创建顺序

严格按照外键依赖关系创建，共26张表:

**第一层 - 基础表** (无外键依赖)

1. sc_school (学校表)
2. sc_role (角色表)

**第二层 - 用户和组织**

3. sc_user (用户表)
4. sc_department (院系表)
5. sc_grade (年级表)

**第三层 - 课程和资源字典**

6. sc_course_category (课程类别表)
7. sc_course_property (课程属性表)
8. sc_course_type (课程类型表)
9. sc_course_nature (课程性质表)
10. sc_credit_hour_type (学时类型表)
11. sc_campus (校区表)
12. sc_classroom_type (教室类型表)
13. sc_teacher_type (教师类型表)

**第四层 - 教学楼和专业**

14. sc_building (教学楼表)
15. sc_major (专业表)

**第五层 - 课程和教室**

16. sc_course (课程表)
17. sc_classroom (教室表)
18. sc_semester (学期表)

**第六层 - 人员和班级**

19. sc_teacher (教师表)
20. sc_administrative_class (行政班表)

**第七层 - 学生和教师扩展**

21. sc_student (学生表)
22. sc_teacher_preferences (教师偏好表)
23. sc_teacher_course_qualification (教师课程资格表)

**第八层 - 排课核心**

24. sc_teaching_class (教学班表)
25. sc_class_assignment (排课表)
26. sc_scheduling_conflict (排课冲突表)

---

### 初始化数据

#### 1. 默认学校

- **名称**: 智能排程系统演示学校
- **英文名**: Smart Schedule Demo School
- **代码**: DEMO001
- **类型**: 大学

#### 2. 默认角色

1. **管理员** (admin): 超级管理员权限
2. **教师** (teacher): 教师权限
3. **学生** (student): 学生权限
4. **教务处老师** (academic): 教务处权限

#### 3. 默认用户（密码：qwer1234）

- **管理员**: flashlack1314@163.com
- **教师**: teacher@flashlack.cn
- **学生**: student@flashlack.cn
- **教务处老师**: academic@flashlack.cn

#### 4. 课程字典数据

**课程类别 (sc_course_category)**:

- 通识课 (GENERAL)
- 专业课 (PROFESSIONAL)
- 实践课 (PRACTICE)

**课程属性 (sc_course_property)**:

- 必修 (REQUIRED)
- 选修 (ELECTIVE)
- 限选 (LIMITED)

**课程类型 (sc_course_type)**:

- 理论课 (THEORY)
- 实验课 (EXPERIMENT)
- 实践课 (PRACTICE)
- 上机课 (COMPUTER)

**课程性质 (sc_course_nature)**:

- 公共课 (PUBLIC)
- 专业基础课 (MAJOR_BASIC)
- 专业核心课 (MAJOR_CORE)
- 专业选修课 (MAJOR_ELECTIVE)

**学时类型 (sc_credit_hour_type)**:

- 理论学时 (THEORY)
- 实验学时 (EXPERIMENT)
- 上机学时 (COMPUTER)
- 实践学时 (PRACTICE)

#### 5. 教室类型数据

**教室类型 (sc_classroom_type)**:

- 普通教室 (NORMAL)
- 多媒体教室 (MULTIMEDIA)
- 实验室 (LAB)
- 机房 (COMPUTER_ROOM)
- 报告厅 (AUDITORIUM)
- 阶梯教室 (STEP)

#### 6. 教师类型数据

**教师类型 (sc_teacher_type)**:

- 专任教师 (FULL_TIME)
- 兼职教师 (PART_TIME)
- 外聘教师 (EXTERNAL)

---

## 附录

### 技术栈版本

- **Java**: 17
- **Spring Boot**: 3.4.12
- **PostgreSQL**: 14+
- **MyBatis Plus**: 3.5.15
- **Hutool**: 5.8.40

### 与旧系统对比

| 项目     | 旧系统 (智课方舟) | 新系统 (Smart Schedule Core)        |
|--------|------------|----------------------------------|
| 表数量    | 33张        | 26张 (精简21%)                      |
| 数据库    | MySQL      | PostgreSQL                       |
| 技术栈    | Go + GORM  | Spring Boot + JPA + MyBatis Plus |
| 主键类型   | CHAR(32)   | VARCHAR(64)                      |
| 多学校支持  | 不支持        | 支持                               |
| JSON类型 | MySQL JSON | PostgreSQL JSONB                 |

### 删除的表（8张）

1. **cs_system** - 系统配置表（不需要）
2. **cs_request_log** - 请求日志表（不需要）
3. **cs_permission** - 权限表（已在user表中用JSON存储）
4. **cs_unit_category** - 单位类别表（过度设计）
5. **cs_unit_type** - 单位类型表（过度设计）
6. **cs_classroom_tag** - 教室标签表（用JSON存储在classroom表中）
7. **cs_tables_chairs_type** - 桌椅类型表（对排课影响不大）
8. **cs_academic_affairs_permission** - 教务权限表（简化权限管理）

---

## 版权信息

**开发者**: flashlack1314
**许可证**: MIT License
**文档生成时间**: 2025-12-15
**数据库版本**: v1.1
**文档版本**: v1.1

### 更新记录

#### v1.1 (2025-12-15)

- 重构 `sc_user` 表结构，移除 `user_student_id`、`user_status`、`user_ban` 字段
- 学号/工号现通过 `sc_teacher.teacher_code` 和 `sc_student.student_code` 获取
- 用户表仅负责认证，业务信息存储在对应的业务表中
