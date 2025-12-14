-- 年级表
CREATE TABLE sc_grade
(
    grade_uuid  VARCHAR(64) NOT NULL PRIMARY KEY,
    school_uuid VARCHAR(64) NOT NULL,
    grade_name  VARCHAR(32) NOT NULL,
    start_year  INT         NOT NULL,
    is_enabled  BOOLEAN   DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 唯一索引（学校+年级名称唯一）
CREATE UNIQUE INDEX uk_grade_name_school ON sc_grade (grade_name, school_uuid);

-- 普通索引
CREATE INDEX idx_grade_school ON sc_grade (school_uuid);
CREATE INDEX idx_grade_enabled ON sc_grade (is_enabled);

-- 注释
COMMENT ON TABLE sc_grade IS '年级表';
COMMENT ON COLUMN sc_grade.grade_uuid IS '年级主键';
COMMENT ON COLUMN sc_grade.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_grade.grade_name IS '年级名称(如: 2024级)';
COMMENT ON COLUMN sc_grade.start_year IS '入学年份';
COMMENT ON COLUMN sc_grade.is_enabled IS '是否启用';
