package io.github.flashlack1314.smartschedulecore.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户表实体类
 * <p>
 * 对应数据库表：`sc_user`
 * 主键采用 UUID 自动生成。
 * </p>
 *
 * @author flash
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@TableName(value = "sc_user")
@Accessors(chain = true)
public class UserDO {

    /**
     * 用户主键，采用 UUID 自动生成
     */
    @TableId(value = "user_uuid", type = IdType.ASSIGN_UUID)
    private String userUuid;

    /**
     * 用户角色 UUID（关联 sc_role 表）
     */
    private String userRoleUuid;

    /**
     * 用户学校 UUID（关联 sc_school 表）
     */
    private String userSchoolUuid;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户邮箱地址
     */
    private String userEmail;

    /**
     * 用户手机号码
     */
    private String userPhoneNum;

    /**
     * 用户密码（加密存储）
     */
    private String userPassword;

    /**
     * 用户封禁状态：false-正常，true-封禁
     */
    private Boolean ban;
}
