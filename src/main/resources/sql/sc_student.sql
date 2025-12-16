-- 学生表
CREATE TABLE sc_student
(
    student_uuid              VARCHAR(32) NOT NULL PRIMARY KEY,
    school_uuid               VARCHAR(32) NOT NULL,
    user_uuid                 VARCHAR(32),
    department_uuid           VARCHAR(32) NOT NULL,
    major_uuid                VARCHAR(32) NOT NULL,
    grade_uuid                VARCHAR(32) NOT NULL,
    administrative_class_uuid VARCHAR(32),
    student_code              VARCHAR(32) NOT NULL,
    student_name              VARCHAR(32) NOT NULL,
    gender VARCHAR(4) DEFAULT '男',
    is_graduated              BOOLEAN              DEFAULT FALSE,
    created_at                TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at                TIMESTAMP            DEFAULT CURRENT_TIMESTAMP
);

-- 唯一索引
CREATE UNIQUE INDEX uk_student_code ON sc_student (student_code);

-- 普通索引
CREATE INDEX idx_student_school ON sc_student (school_uuid);
CREATE INDEX idx_student_department ON sc_student (department_uuid);
CREATE INDEX idx_student_major ON sc_student (major_uuid);
CREATE INDEX idx_student_grade ON sc_student (grade_uuid);
CREATE INDEX idx_student_class ON sc_student (administrative_class_uuid);
CREATE INDEX idx_student_user ON sc_student (user_uuid);
CREATE INDEX idx_student_graduated ON sc_student (is_graduated);

-- 注释
COMMENT ON TABLE sc_student IS '学生表';
COMMENT ON COLUMN sc_student.student_uuid IS '学生主键';
COMMENT ON COLUMN sc_student.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_student.user_uuid IS '关联用户账号';
COMMENT ON COLUMN sc_student.department_uuid IS '所属学院';
COMMENT ON COLUMN sc_student.major_uuid IS '所属专业';
COMMENT ON COLUMN sc_student.grade_uuid IS '年级';
COMMENT ON COLUMN sc_student.administrative_class_uuid IS '所属行政班';
COMMENT ON COLUMN sc_student.student_code IS '学号（唯一）';
COMMENT ON COLUMN sc_student.student_name IS '学生姓名';
COMMENT ON COLUMN sc_student.gender IS '性别(男/女)';
COMMENT ON COLUMN sc_student.is_graduated IS '是否毕业';
