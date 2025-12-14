-- 课程类别表
CREATE TABLE sc_course_category
(
    course_category_uuid VARCHAR(64) NOT NULL PRIMARY KEY,
    school_uuid          VARCHAR(64) NOT NULL,
    category_name        VARCHAR(32) NOT NULL,
    category_code        VARCHAR(32) NOT NULL,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 唯一索引
CREATE UNIQUE INDEX uk_course_category_code ON sc_course_category (category_code);

-- 普通索引
CREATE INDEX idx_course_category_school ON sc_course_category (school_uuid);

-- 注释
COMMENT ON TABLE sc_course_category IS '课程类别表';
COMMENT ON COLUMN sc_course_category.course_category_uuid IS '课程类别主键';
COMMENT ON COLUMN sc_course_category.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_course_category.category_name IS '类别名称(通识课/专业课/实践课)';
COMMENT ON COLUMN sc_course_category.category_code IS '类别编码（唯一）';
