-- 校区表
CREATE TABLE sc_campus
(
    campus_uuid    VARCHAR(64) NOT NULL PRIMARY KEY,
    school_uuid    VARCHAR(64) NOT NULL,
    campus_name    VARCHAR(64) NOT NULL,
    campus_code    VARCHAR(32) NOT NULL,
    campus_address VARCHAR(256),
    latitude       DECIMAL(10, 7),
    longitude      DECIMAL(10, 7),
    is_enabled     BOOLEAN   DEFAULT TRUE,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 唯一索引
CREATE UNIQUE INDEX uk_campus_code ON sc_campus (campus_code);

-- 普通索引
CREATE INDEX idx_campus_school ON sc_campus (school_uuid);
CREATE INDEX idx_campus_enabled ON sc_campus (is_enabled);

-- 注释
COMMENT ON TABLE sc_campus IS '校区表';
COMMENT ON COLUMN sc_campus.campus_uuid IS '校区主键';
COMMENT ON COLUMN sc_campus.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_campus.campus_name IS '校区名称';
COMMENT ON COLUMN sc_campus.campus_code IS '校区编码（唯一）';
COMMENT ON COLUMN sc_campus.campus_address IS '校区地址';
COMMENT ON COLUMN sc_campus.latitude IS '纬度';
COMMENT ON COLUMN sc_campus.longitude IS '经度';
COMMENT ON COLUMN sc_campus.is_enabled IS '是否启用';
