-- 教室表
CREATE TABLE sc_classroom
(
    classroom_uuid             VARCHAR(64) NOT NULL PRIMARY KEY,
    school_uuid                VARCHAR(64) NOT NULL,
    campus_uuid                VARCHAR(64) NOT NULL,
    building_uuid              VARCHAR(64) NOT NULL,
    classroom_type_uuid        VARCHAR(64) NOT NULL,
    classroom_number           VARCHAR(32) NOT NULL,
    classroom_name             VARCHAR(64) NOT NULL,
    floor                      VARCHAR(4)  NOT NULL,
    capacity                   INT         NOT NULL DEFAULT 0,
    examination_capacity       INT,
    area                       DECIMAL(10, 2),
    tags                       JSONB,
    is_multimedia              BOOLEAN              DEFAULT FALSE,
    is_air_conditioned         BOOLEAN              DEFAULT FALSE,
    is_examination_room        BOOLEAN              DEFAULT FALSE,
    management_department_uuid VARCHAR(64),
    is_enabled                 BOOLEAN              DEFAULT TRUE,
    created_at                 TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at                 TIMESTAMP            DEFAULT CURRENT_TIMESTAMP
);

-- 唯一索引
CREATE UNIQUE INDEX uk_classroom_number ON sc_classroom (classroom_number);

-- 普通索引
CREATE INDEX idx_classroom_school ON sc_classroom (school_uuid);
CREATE INDEX idx_classroom_campus ON sc_classroom (campus_uuid);
CREATE INDEX idx_classroom_building ON sc_classroom (building_uuid);
CREATE INDEX idx_classroom_type ON sc_classroom (classroom_type_uuid);
CREATE INDEX idx_classroom_capacity ON sc_classroom (capacity);
CREATE INDEX idx_classroom_enabled ON sc_classroom (is_enabled);

-- 注释
COMMENT ON TABLE sc_classroom IS '教室表';
COMMENT ON COLUMN sc_classroom.classroom_uuid IS '教室主键';
COMMENT ON COLUMN sc_classroom.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_classroom.campus_uuid IS '所属校区';
COMMENT ON COLUMN sc_classroom.building_uuid IS '所属教学楼';
COMMENT ON COLUMN sc_classroom.classroom_type_uuid IS '教室类型';
COMMENT ON COLUMN sc_classroom.classroom_number IS '教室编号（唯一）';
COMMENT ON COLUMN sc_classroom.classroom_name IS '教室名称';
COMMENT ON COLUMN sc_classroom.floor IS '楼层';
COMMENT ON COLUMN sc_classroom.capacity IS '教室容量';
COMMENT ON COLUMN sc_classroom.examination_capacity IS '考场容量';
COMMENT ON COLUMN sc_classroom.area IS '面积(平方米)';
COMMENT ON COLUMN sc_classroom.tags IS '教室标签(JSONB数组)';
COMMENT ON COLUMN sc_classroom.is_multimedia IS '是否多媒体教室';
COMMENT ON COLUMN sc_classroom.is_air_conditioned IS '是否有空调';
COMMENT ON COLUMN sc_classroom.is_examination_room IS '是否考场';
COMMENT ON COLUMN sc_classroom.management_department_uuid IS '管理部门';
COMMENT ON COLUMN sc_classroom.is_enabled IS '是否启用';
