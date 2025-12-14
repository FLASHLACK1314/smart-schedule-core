-- 课程性质表
CREATE TABLE sc_course_nature
(
    course_nature_uuid VARCHAR(64) NOT NULL PRIMARY KEY,
    school_uuid        VARCHAR(64) NOT NULL,
    nature_name        VARCHAR(32) NOT NULL,
    nature_code        VARCHAR(32) NOT NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 唯一索引
CREATE UNIQUE INDEX uk_course_nature_code ON sc_course_nature (nature_code);

-- 普通索引
CREATE INDEX idx_course_nature_school ON sc_course_nature (school_uuid);

-- 注释
COMMENT ON TABLE sc_course_nature IS '课程性质表';
COMMENT ON COLUMN sc_course_nature.course_nature_uuid IS '课程性质主键';
COMMENT ON COLUMN sc_course_nature.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_course_nature.nature_name IS '性质名称(公共课/专业基础课/专业核心课)';
COMMENT ON COLUMN sc_course_nature.nature_code IS '性质编码（唯一）';
