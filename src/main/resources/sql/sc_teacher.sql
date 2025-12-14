-- 教师表
CREATE TABLE sc_teacher
(
    teacher_uuid         VARCHAR(64) NOT NULL PRIMARY KEY,
    school_uuid          VARCHAR(64) NOT NULL,
    user_uuid            VARCHAR(64),
    department_uuid      VARCHAR(64) NOT NULL,
    teacher_type_uuid    VARCHAR(64) NOT NULL,
    teacher_code         VARCHAR(32) NOT NULL,
    teacher_name         VARCHAR(32) NOT NULL,
    teacher_english_name VARCHAR(128),
    gender VARCHAR(4) DEFAULT '男',
    job_title            VARCHAR(32),
    phone                VARCHAR(16),
    email                VARCHAR(64),
    created_at           TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP            DEFAULT CURRENT_TIMESTAMP
);

-- 唯一索引
CREATE UNIQUE INDEX uk_teacher_code ON sc_teacher (teacher_code);

-- 普通索引
CREATE INDEX idx_teacher_school ON sc_teacher (school_uuid);
CREATE INDEX idx_teacher_department ON sc_teacher (department_uuid);
CREATE INDEX idx_teacher_type ON sc_teacher (teacher_type_uuid);
CREATE INDEX idx_teacher_user ON sc_teacher (user_uuid);
CREATE INDEX idx_teacher_name ON sc_teacher (teacher_name);

-- 注释
COMMENT ON TABLE sc_teacher IS '教师表';
COMMENT ON COLUMN sc_teacher.teacher_uuid IS '教师主键';
COMMENT ON COLUMN sc_teacher.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_teacher.user_uuid IS '关联用户账号';
COMMENT ON COLUMN sc_teacher.department_uuid IS '所属院系';
COMMENT ON COLUMN sc_teacher.teacher_type_uuid IS '教师类型';
COMMENT ON COLUMN sc_teacher.teacher_code IS '教师工号（唯一）';
COMMENT ON COLUMN sc_teacher.teacher_name IS '教师姓名';
COMMENT ON COLUMN sc_teacher.teacher_english_name IS '教师英文名';
COMMENT ON COLUMN sc_teacher.gender IS '性别(男/女)';
COMMENT ON COLUMN sc_teacher.job_title IS '职称';
COMMENT ON COLUMN sc_teacher.phone IS '教师电话';
COMMENT ON COLUMN sc_teacher.email IS '教师邮箱';
