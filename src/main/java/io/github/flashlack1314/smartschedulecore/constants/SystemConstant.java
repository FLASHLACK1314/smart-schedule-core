package io.github.flashlack1314.smartschedulecore.constants;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统常量
 *
 * @author flash
 */
@Slf4j
public class SystemConstant {

    /**
     * 私有构造函数，防止实例化
     */
    private SystemConstant() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 角色相关常量
     */
    public static class Role {
        // ========== 角色英文名称（从数据库加载）==========

        /**
         * 角色英文名称：管理员
         */
        @Getter
        @Setter
        private static String adminNameEn;

        /**
         * 角色英文名称：教师
         */
        @Getter
        @Setter
        private static String teacherNameEn;

        /**
         * 角色英文名称：学生
         */
        @Getter
        @Setter
        private static String studentNameEn;

        /**
         * 角色英文名称：教务处老师
         */
        @Getter
        @Setter
        private static String academicNameEn;

        // ========== 角色UUID（从数据库加载）==========

        /**
         * 管理员角色UUID
         */
        @Getter
        @Setter
        private static String adminUuid;

        /**
         * 教师角色UUID
         */
        @Getter
        @Setter
        private static String teacherUuid;

        /**
         * 学生角色UUID
         */
        @Getter
        @Setter
        private static String studentUuid;

        /**
         * 教务处老师角色UUID
         */
        @Getter
        @Setter
        private static String academicUuid;

        private Role() {
            throw new UnsupportedOperationException("Utility class");
        }
    }

    /**
     * 学校相关常量
     */
    public static class School {
        /**
         * 默认学校UUID
         */
        @Getter
        @Setter
        private static String defaultUuid;

        /**
         * 默认学校名称
         */
        @Getter
        @Setter
        private static String defaultName;

        private School() {
            throw new UnsupportedOperationException("Utility class");
        }
    }
}
