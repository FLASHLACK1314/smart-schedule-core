-- SC Role Table
-- 角色表：存储系统中不同角色的信息

DROP TABLE IF EXISTS sc_role CASCADE;

CREATE TABLE sc_role (
                         role_uuid VARCHAR(32) NOT NULL,
    role_name VARCHAR(32) NOT NULL,
    role_name_en VARCHAR(32),
                         role_permissions VARCHAR(1000),
    PRIMARY KEY (role_uuid)
);

-- Comment
COMMENT ON TABLE sc_role IS '角色表：存储系统中不同角色的信息';
COMMENT ON COLUMN sc_role.role_uuid IS '角色唯一标识符';
COMMENT ON COLUMN sc_role.role_name IS '角色名称（中文）';
COMMENT ON COLUMN sc_role.role_name_en IS '角色名称（英文）';
COMMENT ON COLUMN sc_role.role_permissions IS '角色权限配置（文本格式）';