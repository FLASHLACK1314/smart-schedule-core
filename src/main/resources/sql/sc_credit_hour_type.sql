-- 学时类型表
CREATE TABLE sc_credit_hour_type
(
    credit_hour_type_uuid VARCHAR(64) NOT NULL PRIMARY KEY,
    school_uuid           VARCHAR(64) NOT NULL,
    type_name             VARCHAR(32) NOT NULL,
    type_code             VARCHAR(32) NOT NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 唯一索引
CREATE UNIQUE INDEX uk_credit_hour_type_code ON sc_credit_hour_type (type_code);

-- 普通索引
CREATE INDEX idx_credit_hour_type_school ON sc_credit_hour_type (school_uuid);

-- 注释
COMMENT ON TABLE sc_credit_hour_type IS '学时类型表';
COMMENT ON COLUMN sc_credit_hour_type.credit_hour_type_uuid IS '学时类型主键';
COMMENT ON COLUMN sc_credit_hour_type.school_uuid IS '关联学校';
COMMENT ON COLUMN sc_credit_hour_type.type_name IS '学时类型名称(理论学时/实验学时/上机学时/实践学时)';
COMMENT ON COLUMN sc_credit_hour_type.type_code IS '学时类型编码（唯一）';
