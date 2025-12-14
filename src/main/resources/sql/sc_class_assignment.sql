-- 排课表（最核心表）
CREATE TABLE sc_class_assignment
(
    class_assignment_uuid VARCHAR(64) NOT NULL PRIMARY KEY,
    school_uuid           VARCHAR(64) NOT NULL,
    semester_uuid         VARCHAR(64) NOT NULL,
    course_uuid           VARCHAR(64) NOT NULL,
    teacher_uuid          VARCHAR(64) NOT NULL,
    teaching_class_uuid   VARCHAR(64) NOT NULL,
    campus_uuid           VARCHAR(64) NOT NULL,
    building_uuid         VARCHAR(64) NOT NULL,
    classroom_uuid        VARCHAR(64) NOT NULL,
    classroom_type_uuid   VARCHAR(64) NOT NULL,
    credit_hour_type_uuid VARCHAR(64) NOT NULL,
    teaching_hours  INT DEFAULT 0,
    scheduled_hours INT DEFAULT 0,
    total_hours     INT DEFAULT 0,
    class_time            JSONB       NOT NULL,
    specified_time        JSONB,
    consecutive_sessions  SMALLINT       DEFAULT 2,
    scheduling_priority   SMALLINT       DEFAULT 100,
    created_at            TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

-- 普通索引
CREATE INDEX idx_assignment_school ON sc_class_assignment (school_uuid);
CREATE INDEX idx_assignment_semester ON sc_class_assignment (semester_uuid);
CREATE INDEX idx_assignment_course ON sc_class_assignment (course_uuid);
CREATE INDEX idx_assignment_teacher ON sc_class_assignment (teacher_uuid);
CREATE INDEX idx_assignment_teaching_class ON sc_class_assignment (teaching_class_uuid);
CREATE INDEX idx_assignment_classroom ON sc_class_assignment (classroom_uuid);
CREATE INDEX idx_assignment_priority ON sc_class_assignment (scheduling_priority);
CREATE INDEX idx_assignment_school_semester ON sc_class_assignment (school_uuid, semester_uuid);

-- 注释
COMMENT ON TABLE sc_class_assignment IS '排课表';
COMMENT ON COLUMN sc_class_assignment.class_assignment_uuid IS '排课主键';
COMMENT ON COLUMN sc_class_assignment.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_class_assignment.semester_uuid IS '学期';
COMMENT ON COLUMN sc_class_assignment.course_uuid IS '课程';
COMMENT ON COLUMN sc_class_assignment.teacher_uuid IS '教师';
COMMENT ON COLUMN sc_class_assignment.teaching_class_uuid IS '教学班';
COMMENT ON COLUMN sc_class_assignment.campus_uuid IS '校区';
COMMENT ON COLUMN sc_class_assignment.building_uuid IS '教学楼';
COMMENT ON COLUMN sc_class_assignment.classroom_uuid IS '教室';
COMMENT ON COLUMN sc_class_assignment.classroom_type_uuid IS '教室类型';
COMMENT ON COLUMN sc_class_assignment.credit_hour_type_uuid IS '学时类型';
COMMENT ON COLUMN sc_class_assignment.teaching_hours IS '教师实际授课学时';
COMMENT ON COLUMN sc_class_assignment.scheduled_hours IS '已排课学时';
COMMENT ON COLUMN sc_class_assignment.total_hours IS '总需学时';
COMMENT ON COLUMN sc_class_assignment.class_time IS '上课时间(周次、星期、节次)';
COMMENT ON COLUMN sc_class_assignment.specified_time IS '指定固定时间';
COMMENT ON COLUMN sc_class_assignment.consecutive_sessions IS '连堂节数';
COMMENT ON COLUMN sc_class_assignment.scheduling_priority IS '排课优先级';
