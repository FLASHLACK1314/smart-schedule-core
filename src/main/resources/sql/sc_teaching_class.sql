-- 教学班表
CREATE TABLE sc_teaching_class
(
    teaching_class_uuid    VARCHAR(64) NOT NULL PRIMARY KEY,
    school_uuid            VARCHAR(64) NOT NULL,
    semester_uuid          VARCHAR(64) NOT NULL,
    course_uuid            VARCHAR(64) NOT NULL,
    department_uuid        VARCHAR(64) NOT NULL,
    teaching_class_code    VARCHAR(32) NOT NULL,
    teaching_class_name    VARCHAR(64) NOT NULL,
    administrative_classes JSONB       NOT NULL,
    is_compulsory          BOOLEAN   DEFAULT TRUE,
    class_size             INT       DEFAULT 0,
    actual_student_count   INT       DEFAULT 0,
    is_enabled             BOOLEAN   DEFAULT TRUE,
    created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 唯一索引
CREATE UNIQUE INDEX uk_teaching_class_code ON sc_teaching_class (teaching_class_code);

-- 普通索引
CREATE INDEX idx_teaching_class_school ON sc_teaching_class (school_uuid);
CREATE INDEX idx_teaching_class_semester ON sc_teaching_class (semester_uuid);
CREATE INDEX idx_teaching_class_course ON sc_teaching_class (course_uuid);
CREATE INDEX idx_teaching_class_department ON sc_teaching_class (department_uuid);
CREATE INDEX idx_teaching_class_enabled ON sc_teaching_class (is_enabled);

-- 注释
COMMENT ON TABLE sc_teaching_class IS '教学班表';
COMMENT ON COLUMN sc_teaching_class.teaching_class_uuid IS '教学班主键';
COMMENT ON COLUMN sc_teaching_class.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_teaching_class.semester_uuid IS '学期';
COMMENT ON COLUMN sc_teaching_class.course_uuid IS '课程';
COMMENT ON COLUMN sc_teaching_class.department_uuid IS '开课院系';
COMMENT ON COLUMN sc_teaching_class.teaching_class_code IS '教学班编号（唯一）';
COMMENT ON COLUMN sc_teaching_class.teaching_class_name IS '教学班名称';
COMMENT ON COLUMN sc_teaching_class.administrative_classes IS '包含的行政班(UUID数组)';
COMMENT ON COLUMN sc_teaching_class.is_compulsory IS '是否必修课';
COMMENT ON COLUMN sc_teaching_class.class_size IS '班级规模';
COMMENT ON COLUMN sc_teaching_class.actual_student_count IS '实际学生人数';
COMMENT ON COLUMN sc_teaching_class.is_enabled IS '是否启用';
