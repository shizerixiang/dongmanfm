package fm.dongman.integratedlib

import android.app.Activity
import android.content.Context
import android.content.Intent
import fm.dongman.integratedlib.qq.QQLoginHelper
import fm.dongman.integratedlib.sina.WBLoginHelper
import fm.dongman.integratedlib.weixin.WXLoginHelper

/**
 * QQ 登录工具类
 * Created by shize on 2017/11/16.
 */
object QQWBWXLoginHelper {
    val APP_KEY_QQ = "1106543380" // qq app key
    val APP_KEY_SINA = "2833745528" // 新浪 app key
    val APP_KEY_WX = "wxd4eda437638f1c7c" // 微信 app key
    val APP_SECRET_WX = "fe50e3c6c2ae688dba06450937b66f3d" // 微信 secret

    interface LoginResultCallback {
        /**
         * 完成登录
         */
        fun onCompletedLogin(user: LoginUserModel)

        /**
         * 取消或错误
         */
        fun onCancelOrError(msg: String)
    }

    /**
     * qq 登录
     */
    fun doQQLogin(activity: Activity, listener: LoginResultCallback) {
        QQLoginHelper.getQQInstance(activity.applicationContext).loginQQ(activity, listener)
    }

    /**
     * qq登出
     */

    fun doQQLogout(activity: Activity){
        QQLoginHelper.getQQInstance(activity.applicationContext).logoutQQ(activity)
    }
    /**
     * 不调用此方法则不会回调
     * 在 activity 的生命周期的 onActivityResult 中调用
     */
    fun doQQLoginResult(requestCode: Int, resultCode: Int, data: Intent?, context: Context) {
        QQLoginHelper.getQQInstance(context).loginQQResult(requestCode, resultCode, data)
    }

    /**
     * 新浪微博登录
     */
    fun doSinaWBLogin(activity: Activity, listener: LoginResultCallback) {
        WBLoginHelper.getSinaInstance(activity).loginSinaWB(WBLoginHelper.WB_AUTO, listener)
    }

    /**
     * 不调用此方法则不会回调
     * 在 activity 的生命周期的 onActivityResult 中调用
     */
    fun doSinaWBLoginResult(requestCode: Int, resultCode: Int, data: Intent?, activity: Activity) {
        WBLoginHelper.getSinaInstance(activity).loginSinaWBResult(requestCode, resultCode, data)
    }

    /**
     * 微信登录
     */
    fun doWXLogin(activity: Activity, listener: LoginResultCallback) {
        WXLoginHelper.getWXInstance(activity.applicationContext).loginWX(listener)
    }

    /**
     * 第三方登录的用户模型
     */
    class LoginUserModel {
        var mServiceName: String = "" // 平台 weibo or qq or weixin   必填
        var mUid: String = "" // 用户id，即 openId     必填
        var mGender: Int = 0 // 性别：0：女，1：男
        var mNickName: String? = null // 昵称
        var mAvatarUrl: String? = null // 头像地址
        var mDescription: String? = null // 简介

        override fun toString(): String {
            return "用户信息：\n平台：$mServiceName  用户id：$mUid  性别：$mGender  昵称：$mNickName  头像地址：$mAvatarUrl  简介：$mDescription"
        }
    }
}