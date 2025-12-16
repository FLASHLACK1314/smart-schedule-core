-- 课程属性表
CREATE TABLE sc_course_property
(
    course_property_uuid VARCHAR(32) NOT NULL PRIMARY KEY,
    school_uuid          VARCHAR(32) NOT NULL,
    property_name        VARCHAR(32) NOT NULL,
    property_code        VARCHAR(32) NOT NULL,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 唯一索引
CREATE UNIQUE INDEX uk_course_property_code ON sc_course_property (property_code);

-- 普通索引
CREATE INDEX idx_course_property_school ON sc_course_property (school_uuid);

-- 注释
COMMENT ON TABLE sc_course_property IS '课程属性表';
COMMENT ON COLUMN sc_course_property.course_property_uuid IS '课程属性主键';
COMMENT ON COLUMN sc_course_property.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_course_property.property_name IS '属性名称(必修/选修/限选)';
COMMENT ON COLUMN sc_course_property.property_code IS '属性编码（唯一）';
