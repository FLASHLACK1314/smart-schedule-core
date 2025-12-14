-- SC User Table
-- 用户表：存储系统中用户的基本信息

DROP TABLE IF EXISTS sc_user CASCADE;

CREATE TABLE sc_user (
                         user_uuid        VARCHAR(64)  NOT NULL,
                         user_role_uuid   VARCHAR(64)  NOT NULL,
                         user_school_uuid VARCHAR(64)  NOT NULL,
    user_name VARCHAR(32) NOT NULL,
                         user_email       VARCHAR(64)  NOT NULL,
    user_phone_num VARCHAR(32) NOT NULL,
                         user_password    VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_uuid)
);

-- Comment
COMMENT ON TABLE sc_user IS '用户表：存储系统中用户的基本信息';
COMMENT ON COLUMN sc_user.user_uuid IS '用户唯一标识符';
COMMENT ON COLUMN sc_user.user_role_uuid IS '用户角色唯一标识符（关联sc_role表）';
COMMENT ON COLUMN sc_user.user_school_uuid IS '学校唯一标识符（关联sc_school表）';
COMMENT ON COLUMN sc_user.user_name IS '用户姓名';
COMMENT ON COLUMN sc_user.user_email IS '用户邮箱地址';
COMMENT ON COLUMN sc_user.user_phone_num IS '用户手机号码';
COMMENT ON COLUMN sc_user.user_password IS '用户密码（加密存储）';

-- Index for performance
CREATE INDEX idx_user_school ON sc_user (user_school_uuid);

-- Foreign Key Constraints
-- 注意：由于表创建顺序，外键约束可能需要在数据迁移时添加
-- ALTER TABLE sc_user ADD CONSTRAINT fk_user_role
-- FOREIGN KEY (user_role_uuid) REFERENCES sc_role(role_uuid);
-- ALTER TABLE sc_user ADD CONSTRAINT fk_user_school
-- FOREIGN KEY (user_school_uuid) REFERENCES sc_school(school_uuid);