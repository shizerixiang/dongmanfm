package fm.dongman.presenterlib.util

/**
 * 验证工具类
 * Created by shize on 2017/11/16.
 */
object ValidateHelper {
    /**
     * 验证密码格式
     */
    fun checkPwd(pwd: String): Boolean {
        // 必须至少有一个字母，一个数字，8~30位
        val regexPwd = "^(?=.*\\d)(?=.*([a-zA-Z])).{8,30}$".toRegex()
        // 检测密码是否安全合法
        return pwd.matches(regexPwd)
    }

    /**
     * 验证手机号格式
     */
    fun checkPhoneNum(phoneNum: String): Boolean {
        // 手机号码正则表达式
        val regexPhone = "1(3[0-9]|47|5((?!4)[0-9])|7(0|1|[6-8])|8[0-9])\\d{8}".toRegex()
        // 检测是否为手机号
        return phoneNum.trim().matches(regexPhone)
    }

    /**
     * 验证邮箱格式
     */
    fun checkEmail(email: String): Boolean {
        // 邮箱的正则表达式
        val regexEmail = "\\w+(\\.\\w)*@\\w+(\\.\\w{2,3}){1,3}".toRegex()
        // 检测是否为邮箱
        return email.trim().matches(regexEmail)
    }

    /**
     * 是否为数组
     */
    fun isNumber(str: String?): Boolean {
        if (str == null) {
            return false
        }
        return str.toIntOrNull() != null
    }

    /**
     * 是否为网址
     */
    fun isWebsite(str: String): Boolean {
        val regex = "^((http|https)://)?".toRegex()
        return regex.matches(str)
    }
}