-- SC School Table
-- 学校表：存储系统中学校的基本信息

DROP TABLE IF EXISTS sc_school CASCADE;

CREATE TABLE sc_school
(
    school_uuid    VARCHAR(64)        NOT NULL PRIMARY KEY,
    school_name    VARCHAR(128)       NOT NULL,
    school_name_en VARCHAR(128),
    school_code    VARCHAR(32) UNIQUE NOT NULL,
    school_type    VARCHAR(32),
    school_address VARCHAR(256),
    school_phone   VARCHAR(32),
    school_email   VARCHAR(64),
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Comment
COMMENT ON TABLE sc_school IS '学校表：存储系统中学校的基本信息';
COMMENT ON COLUMN sc_school.school_uuid IS '学校唯一标识符';
COMMENT ON COLUMN sc_school.school_name IS '学校名称';
COMMENT ON COLUMN sc_school.school_name_en IS '学校英文名称';
COMMENT ON COLUMN sc_school.school_code IS '学校代码（唯一）';
COMMENT ON COLUMN sc_school.school_type IS '学校类型：大学/中学/小学';
COMMENT ON COLUMN sc_school.school_address IS '学校地址';
COMMENT ON COLUMN sc_school.school_phone IS '学校联系电话';
COMMENT ON COLUMN sc_school.school_email IS '学校邮箱地址';
COMMENT ON COLUMN sc_school.created_at IS '创建时间';
COMMENT ON COLUMN sc_school.updated_at IS '更新时间';