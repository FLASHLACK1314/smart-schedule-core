package io.github.flashlack1314.smartschedulecore.constants;

/**
 * 正则表达式常量
 * <p>
 * 使用内部静态类分类管理正则表达式模式
 * </p>
 *
 * @author flash
 */
public class StringConstant {

    /**
     * 私有构造函数，防止实例化
     */
    private StringConstant() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 用户相关正则表达式
     */
    public static class Regexp {
        /**
         * 邮箱格式：标准邮箱格式
         * <p>示例：user@example.com, test.user@domain.co.uk</p>
         */
        public static final String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        /**
         * 手机号格式：中国大陆手机号（11位，1开头，第二位为3-9）
         * <p>示例：13800138000, 19912345678</p>
         */
        public static final String PHONE = "^1[3-9]\\d{9}$";

        /**
         * 用户名格式：2-32位，支持中英文、数字、下划线
         * <p>示例：张三, user_123, John_Doe</p>
         */
        public static final String USERNAME = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]{2,32}$";

        /**
         * 密码格式：6-32位，支持字母、数字及特殊字符
         */
        public static final String PASSWORD = "^[a-zA-Z0-9_!@#$%^&*().]{6,32}$";

        /**
         * 验证码格式：6位数字
         * <p>示例：123456, 000000</p>
         */
        public static final String VERIFICATION_CODE = "^\\d{6}$";

        private Regexp() {
            throw new UnsupportedOperationException("Utility class");
        }
    }
}
