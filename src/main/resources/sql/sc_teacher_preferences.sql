-- 教师偏好表
CREATE TABLE sc_teacher_preferences
(
    preference_uuid               VARCHAR(64) NOT NULL PRIMARY KEY,
    school_uuid                   VARCHAR(64) NOT NULL,
    teacher_uuid                  VARCHAR(64) NOT NULL,
    preferred_time                JSONB,
    avoided_time                  JSONB,
    preferred_campus_uuid         VARCHAR(64),
    preferred_classroom_type_uuid VARCHAR(64),
    max_courses_per_day           SMALLINT,
    max_consecutive_courses       SMALLINT,
    created_at                    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at                    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 普通索引
CREATE INDEX idx_preference_school ON sc_teacher_preferences (school_uuid);
CREATE INDEX idx_preference_teacher ON sc_teacher_preferences (teacher_uuid);

-- 注释
COMMENT ON TABLE sc_teacher_preferences IS '教师偏好表';
COMMENT ON COLUMN sc_teacher_preferences.preference_uuid IS '偏好主键';
COMMENT ON COLUMN sc_teacher_preferences.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_teacher_preferences.teacher_uuid IS '关联教师';
COMMENT ON COLUMN sc_teacher_preferences.preferred_time IS '偏好时间段(JSONB)';
COMMENT ON COLUMN sc_teacher_preferences.avoided_time IS '避开时间段(JSONB)';
COMMENT ON COLUMN sc_teacher_preferences.preferred_campus_uuid IS '偏好校区';
COMMENT ON COLUMN sc_teacher_preferences.preferred_classroom_type_uuid IS '偏好教室类型';
COMMENT ON COLUMN sc_teacher_preferences.max_courses_per_day IS '每天最多课程数';
COMMENT ON COLUMN sc_teacher_preferences.max_consecutive_courses IS '最多连续课程数';
