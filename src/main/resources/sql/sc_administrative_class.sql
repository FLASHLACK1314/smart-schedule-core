-- 行政班表
CREATE TABLE sc_administrative_class
(
    administrative_class_uuid VARCHAR(64) NOT NULL PRIMARY KEY,
    school_uuid               VARCHAR(64) NOT NULL,
    department_uuid           VARCHAR(64) NOT NULL,
    major_uuid                VARCHAR(64) NOT NULL,
    grade_uuid                VARCHAR(64) NOT NULL,
    class_code                VARCHAR(32) NOT NULL,
    class_name                VARCHAR(64) NOT NULL,
    student_count             INT       DEFAULT 0,
    counselor_uuid            VARCHAR(64),
    monitor_uuid              VARCHAR(64),
    is_enabled                BOOLEAN   DEFAULT TRUE,
    created_at                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 唯一索引
CREATE UNIQUE INDEX uk_class_code ON sc_administrative_class (class_code);

-- 普通索引
CREATE INDEX idx_class_school ON sc_administrative_class (school_uuid);
CREATE INDEX idx_class_department ON sc_administrative_class (department_uuid);
CREATE INDEX idx_class_major ON sc_administrative_class (major_uuid);
CREATE INDEX idx_class_grade ON sc_administrative_class (grade_uuid);
CREATE INDEX idx_class_enabled ON sc_administrative_class (is_enabled);

-- 注释
COMMENT ON TABLE sc_administrative_class IS '行政班表';
COMMENT ON COLUMN sc_administrative_class.administrative_class_uuid IS '行政班主键';
COMMENT ON COLUMN sc_administrative_class.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_administrative_class.department_uuid IS '所属院系';
COMMENT ON COLUMN sc_administrative_class.major_uuid IS '所属专业';
COMMENT ON COLUMN sc_administrative_class.grade_uuid IS '年级';
COMMENT ON COLUMN sc_administrative_class.class_code IS '班级编号（唯一）';
COMMENT ON COLUMN sc_administrative_class.class_name IS '班级名称';
COMMENT ON COLUMN sc_administrative_class.student_count IS '学生人数';
COMMENT ON COLUMN sc_administrative_class.counselor_uuid IS '辅导员';
COMMENT ON COLUMN sc_administrative_class.monitor_uuid IS '班长';
COMMENT ON COLUMN sc_administrative_class.is_enabled IS '是否启用';
