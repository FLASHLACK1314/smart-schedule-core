-- 排课冲突表
CREATE TABLE sc_scheduling_conflict
(
    conflict_uuid          VARCHAR(32) NOT NULL PRIMARY KEY,
    school_uuid            VARCHAR(32) NOT NULL,
    semester_uuid          VARCHAR(32) NOT NULL,
    first_assignment_uuid  VARCHAR(32) NOT NULL,
    second_assignment_uuid VARCHAR(32) NOT NULL,
    conflict_type          SMALLINT    NOT NULL,
    conflict_time          JSONB       NOT NULL,
    description            VARCHAR(255),
    resolution_status      SMALLINT  DEFAULT 0,
    resolution_method      SMALLINT,
    resolution_notes       VARCHAR(255),
    resolved_by            VARCHAR(64),
    resolved_at            TIMESTAMP,
    created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 普通索引
CREATE INDEX idx_conflict_school ON sc_scheduling_conflict (school_uuid);
CREATE INDEX idx_conflict_semester ON sc_scheduling_conflict (semester_uuid);
CREATE INDEX idx_conflict_first ON sc_scheduling_conflict (first_assignment_uuid);
CREATE INDEX idx_conflict_second ON sc_scheduling_conflict (second_assignment_uuid);
CREATE INDEX idx_conflict_type ON sc_scheduling_conflict (conflict_type);
CREATE INDEX idx_conflict_status ON sc_scheduling_conflict (resolution_status);

-- 注释
COMMENT ON TABLE sc_scheduling_conflict IS '排课冲突表';
COMMENT ON COLUMN sc_scheduling_conflict.conflict_uuid IS '冲突主键';
COMMENT ON COLUMN sc_scheduling_conflict.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_scheduling_conflict.semester_uuid IS '学期';
COMMENT ON COLUMN sc_scheduling_conflict.first_assignment_uuid IS '第一个排课';
COMMENT ON COLUMN sc_scheduling_conflict.second_assignment_uuid IS '第二个排课';
COMMENT ON COLUMN sc_scheduling_conflict.conflict_type IS '冲突类型(1:教师 2:教室 3:班级 4:其他)';
COMMENT ON COLUMN sc_scheduling_conflict.conflict_time IS '冲突时间';
COMMENT ON COLUMN sc_scheduling_conflict.description IS '冲突描述';
COMMENT ON COLUMN sc_scheduling_conflict.resolution_status IS '解决状态(0:未解决 1:已解决 2:忽略)';
COMMENT ON COLUMN sc_scheduling_conflict.resolution_method IS '解决方法(1:调整第一个 2:调整第二个 3:同时调整 4:其他)';
COMMENT ON COLUMN sc_scheduling_conflict.resolution_notes IS '解决备注';
COMMENT ON COLUMN sc_scheduling_conflict.resolved_by IS '解决人';
COMMENT ON COLUMN sc_scheduling_conflict.resolved_at IS '解决时间';
