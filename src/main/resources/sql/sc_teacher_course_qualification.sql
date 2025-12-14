-- 教师课程资格表
CREATE TABLE sc_teacher_course_qualification
(
    qualification_uuid  VARCHAR(64) NOT NULL PRIMARY KEY,
    school_uuid         VARCHAR(64) NOT NULL,
    teacher_uuid        VARCHAR(64) NOT NULL,
    course_uuid         VARCHAR(64) NOT NULL,
    qualification_level SMALLINT    NOT NULL DEFAULT 1,
    is_primary          BOOLEAN              DEFAULT FALSE,
    approval_status     SMALLINT             DEFAULT 0,
    approved_by         VARCHAR(64),
    approved_at         TIMESTAMP,
    created_at          TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP            DEFAULT CURRENT_TIMESTAMP
);

-- 普通索引
CREATE INDEX idx_qualification_school ON sc_teacher_course_qualification (school_uuid);
CREATE INDEX idx_qualification_teacher ON sc_teacher_course_qualification (teacher_uuid);
CREATE INDEX idx_qualification_course ON sc_teacher_course_qualification (course_uuid);
CREATE INDEX idx_qualification_status ON sc_teacher_course_qualification (approval_status);

-- 注释
COMMENT ON TABLE sc_teacher_course_qualification IS '教师课程资格表';
COMMENT ON COLUMN sc_teacher_course_qualification.qualification_uuid IS '资格主键';
COMMENT ON COLUMN sc_teacher_course_qualification.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_teacher_course_qualification.teacher_uuid IS '关联教师';
COMMENT ON COLUMN sc_teacher_course_qualification.course_uuid IS '关联课程';
COMMENT ON COLUMN sc_teacher_course_qualification.qualification_level IS '资格等级(1-5级)';
COMMENT ON COLUMN sc_teacher_course_qualification.is_primary IS '是否主讲';
COMMENT ON COLUMN sc_teacher_course_qualification.approval_status IS '审批状态(0:待审批 1:已通过 2:已拒绝)';
COMMENT ON COLUMN sc_teacher_course_qualification.approved_by IS '审批人';
COMMENT ON COLUMN sc_teacher_course_qualification.approved_at IS '审批时间';
