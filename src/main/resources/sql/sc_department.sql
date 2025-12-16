-- 院系/部门表
CREATE TABLE sc_department
(
    department_uuid        VARCHAR(32) NOT NULL PRIMARY KEY,
    school_uuid            VARCHAR(32) NOT NULL,
    department_code         VARCHAR(32) NOT NULL,
    department_name         VARCHAR(64) NOT NULL,
    department_english_name VARCHAR(128),
    department_short_name   VARCHAR(32),
    parent_department_uuid VARCHAR(32),
    is_teaching_department  BOOLEAN   DEFAULT TRUE,
    is_enabled              BOOLEAN   DEFAULT TRUE,
    department_order        INT       DEFAULT 100,
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 唯一索引
CREATE UNIQUE INDEX uk_department_code ON sc_department (department_code);

-- 普通索引
CREATE INDEX idx_department_school ON sc_department (school_uuid);
CREATE INDEX idx_department_parent ON sc_department (parent_department_uuid);
CREATE INDEX idx_department_enabled ON sc_department (is_enabled);

-- 注释
COMMENT ON TABLE sc_department IS '院系/部门表';
COMMENT ON COLUMN sc_department.department_uuid IS '部门主键';
COMMENT ON COLUMN sc_department.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_department.department_code IS '部门编码（唯一）';
COMMENT ON COLUMN sc_department.department_name IS '部门名称';
COMMENT ON COLUMN sc_department.department_english_name IS '部门英文名称';
COMMENT ON COLUMN sc_department.department_short_name IS '部门简称';
COMMENT ON COLUMN sc_department.parent_department_uuid IS '上级部门（支持树形结构）';
COMMENT ON COLUMN sc_department.is_teaching_department IS '是否开课院系';
COMMENT ON COLUMN sc_department.is_enabled IS '是否启用';
COMMENT ON COLUMN sc_department.department_order IS '排序';
