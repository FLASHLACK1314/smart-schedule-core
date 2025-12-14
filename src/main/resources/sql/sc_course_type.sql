-- 课程类型表
CREATE TABLE sc_course_type
(
    course_type_uuid VARCHAR(64) NOT NULL PRIMARY KEY,
    school_uuid      VARCHAR(64) NOT NULL,
    type_name        VARCHAR(32) NOT NULL,
    type_code        VARCHAR(32) NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 唯一索引
CREATE UNIQUE INDEX uk_course_type_code ON sc_course_type (type_code);

-- 普通索引
CREATE INDEX idx_course_type_school ON sc_course_type (school_uuid);

-- 注释
COMMENT ON TABLE sc_course_type IS '课程类型表';
COMMENT ON COLUMN sc_course_type.course_type_uuid IS '课程类型主键';
COMMENT ON COLUMN sc_course_type.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_course_type.type_name IS '类型名称(理论课/实验课/实践课/上机课)';
COMMENT ON COLUMN sc_course_type.type_code IS '类型编码（唯一）';
