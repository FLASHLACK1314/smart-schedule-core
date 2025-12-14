-- 教学楼表
CREATE TABLE sc_building
(
    building_uuid VARCHAR(64) NOT NULL PRIMARY KEY,
    school_uuid   VARCHAR(64) NOT NULL,
    campus_uuid   VARCHAR(64) NOT NULL,
    building_name VARCHAR(64) NOT NULL,
    building_code VARCHAR(32) NOT NULL,
    floor_count   SMALLINT    NOT NULL DEFAULT 1,
    is_enabled    BOOLEAN              DEFAULT TRUE,
    created_at    TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP            DEFAULT CURRENT_TIMESTAMP
);

-- 唯一索引
CREATE UNIQUE INDEX uk_building_code ON sc_building (building_code);

-- 普通索引
CREATE INDEX idx_building_school ON sc_building (school_uuid);
CREATE INDEX idx_building_campus ON sc_building (campus_uuid);
CREATE INDEX idx_building_enabled ON sc_building (is_enabled);

-- 注释
COMMENT ON TABLE sc_building IS '教学楼表';
COMMENT ON COLUMN sc_building.building_uuid IS '教学楼主键';
COMMENT ON COLUMN sc_building.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_building.campus_uuid IS '所属校区';
COMMENT ON COLUMN sc_building.building_name IS '教学楼名称';
COMMENT ON COLUMN sc_building.building_code IS '教学楼编码（唯一）';
COMMENT ON COLUMN sc_building.floor_count IS '楼层数';
COMMENT ON COLUMN sc_building.is_enabled IS '是否启用';
