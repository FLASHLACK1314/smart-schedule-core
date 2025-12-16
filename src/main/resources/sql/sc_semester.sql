-- 学期表
CREATE TABLE sc_semester
(
    semester_uuid VARCHAR(32) NOT NULL PRIMARY KEY,
    school_uuid   VARCHAR(32) NOT NULL,
    semester_name VARCHAR(64) NOT NULL,
    start_date    DATE        NOT NULL,
    end_date      DATE        NOT NULL,
    is_current    BOOLEAN   DEFAULT FALSE,
    is_enabled    BOOLEAN   DEFAULT TRUE,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 唯一索引
CREATE UNIQUE INDEX uk_semester_name_school ON sc_semester (semester_name, school_uuid);

-- 普通索引
CREATE INDEX idx_semester_school ON sc_semester (school_uuid);
CREATE INDEX idx_semester_current ON sc_semester (is_current);
CREATE INDEX idx_semester_enabled ON sc_semester (is_enabled);

-- 注释
COMMENT ON TABLE sc_semester IS '学期表';
COMMENT ON COLUMN sc_semester.semester_uuid IS '学期主键';
COMMENT ON COLUMN sc_semester.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_semester.semester_name IS '学期名称(如: 2024-2025学年第1学期)';
COMMENT ON COLUMN sc_semester.start_date IS '学期开始日期';
COMMENT ON COLUMN sc_semester.end_date IS '学期结束日期';
COMMENT ON COLUMN sc_semester.is_current IS '是否当前学期';
COMMENT ON COLUMN sc_semester.is_enabled IS '是否启用';
