-- 专业表
CREATE TABLE sc_major
(
    major_uuid      VARCHAR(64) NOT NULL PRIMARY KEY,
    school_uuid     VARCHAR(64) NOT NULL,
    department_uuid VARCHAR(64) NOT NULL,
    major_code      VARCHAR(32) NOT NULL,
    major_name      VARCHAR(64) NOT NULL,
    education_years SMALLINT    NOT NULL DEFAULT 4,
    training_level  VARCHAR(32) NOT NULL DEFAULT '本科',
    is_enabled      BOOLEAN              DEFAULT TRUE,
    created_at      TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP            DEFAULT CURRENT_TIMESTAMP
);

-- 唯一索引
CREATE UNIQUE INDEX uk_major_code ON sc_major (major_code);

-- 普通索引
CREATE INDEX idx_major_school ON sc_major (school_uuid);
CREATE INDEX idx_major_department ON sc_major (department_uuid);
CREATE INDEX idx_major_enabled ON sc_major (is_enabled);

-- 注释
COMMENT ON TABLE sc_major IS '专业表';
COMMENT ON COLUMN sc_major.major_uuid IS '专业主键';
COMMENT ON COLUMN sc_major.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_major.department_uuid IS '所属学院';
COMMENT ON COLUMN sc_major.major_code IS '专业代码（唯一）';
COMMENT ON COLUMN sc_major.major_name IS '专业名称';
COMMENT ON COLUMN sc_major.education_years IS '学制(年)';
COMMENT ON COLUMN sc_major.training_level IS '培养层次(本科/专科/研究生)';
COMMENT ON COLUMN sc_major.is_enabled IS '是否启用';
