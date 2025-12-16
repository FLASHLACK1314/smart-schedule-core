-- 课程表
CREATE TABLE sc_course
(
    course_uuid                    VARCHAR(32) NOT NULL PRIMARY KEY,
    school_uuid                    VARCHAR(32) NOT NULL,
    course_code                    VARCHAR(32) NOT NULL,
    course_name                    VARCHAR(64) NOT NULL,
    course_english_name            VARCHAR(128),
    department_uuid                VARCHAR(32) NOT NULL,
    course_category_uuid           VARCHAR(32),
    course_property_uuid           VARCHAR(32),
    course_type_uuid               VARCHAR(32) NOT NULL,
    course_nature_uuid             VARCHAR(32),
    total_hours      INT DEFAULT 0,
    week_hours       INT DEFAULT 0,
    theory_hours     INT DEFAULT 0,
    experiment_hours INT DEFAULT 0,
    practice_hours   INT DEFAULT 0,
    computer_hours   INT DEFAULT 0,
    credit                         DECIMAL(10, 2) DEFAULT 0,
    theory_classroom_type_uuid     VARCHAR(32),
    experiment_classroom_type_uuid VARCHAR(32),
    practice_classroom_type_uuid   VARCHAR(32),
    is_enabled                     BOOLEAN        DEFAULT TRUE,
    created_at                     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    updated_at                     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

-- 唯一索引
CREATE UNIQUE INDEX uk_course_code ON sc_course (course_code);

-- 普通索引
CREATE INDEX idx_course_school ON sc_course (school_uuid);
CREATE INDEX idx_course_department ON sc_course (department_uuid);
CREATE INDEX idx_course_type ON sc_course (course_type_uuid);
CREATE INDEX idx_course_category ON sc_course (course_category_uuid);
CREATE INDEX idx_course_enabled ON sc_course (is_enabled);

-- 注释
COMMENT ON TABLE sc_course IS '课程表';
COMMENT ON COLUMN sc_course.course_uuid IS '课程主键';
COMMENT ON COLUMN sc_course.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_course.course_code IS '课程编号（唯一）';
COMMENT ON COLUMN sc_course.course_name IS '课程名称';
COMMENT ON COLUMN sc_course.course_english_name IS '课程英文名称';
COMMENT ON COLUMN sc_course.department_uuid IS '开课院系';
COMMENT ON COLUMN sc_course.course_category_uuid IS '课程类别';
COMMENT ON COLUMN sc_course.course_property_uuid IS '课程属性';
COMMENT ON COLUMN sc_course.course_type_uuid IS '课程类型';
COMMENT ON COLUMN sc_course.course_nature_uuid IS '课程性质';
COMMENT ON COLUMN sc_course.total_hours IS '总学时';
COMMENT ON COLUMN sc_course.week_hours IS '周学时';
COMMENT ON COLUMN sc_course.theory_hours IS '理论学时';
COMMENT ON COLUMN sc_course.experiment_hours IS '实验学时';
COMMENT ON COLUMN sc_course.practice_hours IS '实践学时';
COMMENT ON COLUMN sc_course.computer_hours IS '上机学时';
COMMENT ON COLUMN sc_course.credit IS '学分';
COMMENT ON COLUMN sc_course.theory_classroom_type_uuid IS '理论课教室类型';
COMMENT ON COLUMN sc_course.experiment_classroom_type_uuid IS '实验课教室类型';
COMMENT ON COLUMN sc_course.practice_classroom_type_uuid IS '实践课教室类型';
COMMENT ON COLUMN sc_course.is_enabled IS '是否启用';
