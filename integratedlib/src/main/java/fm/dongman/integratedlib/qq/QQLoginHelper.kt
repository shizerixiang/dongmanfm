package fm.dongman.integratedlib.qq

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.tencent.connect.UserInfo
import com.tencent.connect.common.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import fm.dongman.integratedlib.QQWBWXLoginHelper
import org.jetbrains.anko.doAsync
import org.json.JSONObject

/**
 * QQ 登录类，需要导入 QQ 官方 SDK
 * 配置AndroidManifest
 * 在应用的AndroidManifest.xml增加配置的<application>节点下增加以下配置（注：不配置将会导致无法调用API）；
 * <p>
 *  <uses-permission android:name="android.permission.INTERNET" />
 *  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 *  <application>
 *      <activity
 *          android:name="com.tencent.tauth.AuthActivity"
 *          android:noHistory="true"
 *          android:launchMode="singleTask" >
 *              <intent-filter>
 *                  <action android:name="android.intent.action.VIEW" />
 *                  <category android:name="android.intent.category.DEFAULT" />
 *                  <category android:name="android.intent.category.BROWSABLE" />
 *                  <data android:scheme="tencent你的AppId" />
 *              </intent-filter>
 *      </activity>
 * <application>
 * </p>
 *
 * SDK_V2.0引入了AssistActivity，开发者需在androidManifest.xml中注册。代码如下：
 * <p>
 * <activity android:name="com.tencent.connect.common.AssistActivity"
 *      android:theme="@android:style/Theme.Translucent.NoTitleBar"
 *      android:configChanges="orientation|keyboardHidden|screenSize"/>
 * </p>
 * Created by shize on 2017/11/17.
 */
class QQLoginHelper private constructor(appContext: Context) {
    // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI
    private val mTencent: Tencent = Tencent.createInstance(APP_ID, appContext)
    private var mJSONObject: JSONObject? = null // 返回的 JSONObject 数据
    private var mResultListener: QQWBWXLoginHelper.LoginResultCallback? = null // 结果监听器
    private var mUserModel: QQWBWXLoginHelper.LoginUserModel = QQWBWXLoginHelper.LoginUserModel() // 用户数据模型

    /**
     * 登录回调监听器
     */
    private var mListener: IUiListener = object : IUiListener {
        override fun onComplete(p0: Any?) {
            try {
                mJSONObject = p0 as JSONObject
                val openId = mJSONObject!!.optString(Constants.PARAM_OPEN_ID)
                mUserModel.mServiceName = SERVICE_NAME
                mUserModel.mUid = openId
                mTencent.openId = openId
                mTencent.setAccessToken(mJSONObject!!.optString(Constants.PARAM_ACCESS_TOKEN),
                        "" + (System.currentTimeMillis() + (mJSONObject!!.optString(Constants.PARAM_EXPIRES_IN)).toLong() * 1000))
                getUserInfo(appContext)
            } catch (e: Exception) {
                mResultListener?.onCancelOrError("登录成功，但数据转换错误！数据：\n" + p0.toString())
                e.printStackTrace()
            }
        }

        override fun onCancel() {
            mResultListener?.onCancelOrError("登录取消！")
        }

        override fun onError(p0: UiError?) {
            mResultListener?.onCancelOrError("登录错误！Code：${p0?.errorCode}\n   msg：${p0?.errorMessage}")
        }
    }

    /**
     * 获取用户信息
     * @param appContext 上下文
     */
    private fun getUserInfo(appContext: Context) {
        val userInfo = UserInfo(appContext, mTencent.qqToken)
        userInfo.getUserInfo(object : IUiListener {
            override fun onComplete(p1: Any?) {
                doAsync {
                    try {
                        val jsonObject = p1 as JSONObject
                        parseUserInfo(jsonObject)
                        mResultListener?.onCompletedLogin(mUserModel)
                    } catch (e: Exception) {
                        mResultListener?.onCancelOrError("信息获取成功，但数据转换错误！数据为：\n" + p1.toString())
                        e.printStackTrace()
                    }
                }
            }

            override fun onCancel() {
                mResultListener?.onCancelOrError("信息获取取消！")
            }

            override fun onError(p0: UiError?) {
                mResultListener?.onCancelOrError("信息获取错误！Code：${p0?.errorCode}\n   msg：${p0?.errorMessage}")
            }
        })
    }

    /**
     * 解析用户信息
     * @param jsonObject json
     */
    private fun parseUserInfo(jsonObject: JSONObject) {
        if (jsonObject.has(AVATAR_URL))
            mUserModel.mAvatarUrl = jsonObject.optString(AVATAR_URL)
        if (jsonObject.has(GENDER)) {
            mUserModel.mGender = 1
            if (jsonObject.optString(GENDER) == "女") {
                mUserModel.mGender = 0
            }
        }
        if (jsonObject.has(NICK_NAME))
            mUserModel.mNickName = jsonObject.optString(NICK_NAME)
    }

    /**
     * 登录 QQ
     * 特别注意
     * 应用调用Android_SDK接口时，如果要成功接收到回调，需要在调用接口的Activity的onActivityResult方法中增加如下代码：
     * Tencent.onActivityResultData(requestCode,resultCode,data,listener);
     * 第三方登录主要获取其 OpenId 用于识别用户
     * @param activity activity
     */
    fun loginQQ(activity: Activity, listener: QQWBWXLoginHelper.LoginResultCallback) {
        mResultListener = listener
        if (!mTencent.isSessionValid) {
            mTencent.login(activity, SCOPE, mListener)
        }
    }

    /**
     * 不调用此方法则不会回调
     */
    fun loginQQResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mListener)
    }

    /**
     * 登出 QQ
     * @param activity activity
     */
     fun logoutQQ(activity: Activity) {
        mTencent.logout(activity)
    }

    companion object {
        /**
         * 修改 app id 时，需要同时修改 AndroidManifest.xml 中的
         * <data android:scheme="tencent+APP_ID" />
         */
        val APP_ID = QQWBWXLoginHelper.APP_KEY_QQ
        val SCOPE = "all" // 应用需要哪些 API 的权限
        val SERVICE_NAME = "qq" // 登录平台
        val AVATAR_URL = "figureurl_2" // 头像
        val GENDER = "gender" // 性别
        val NICK_NAME = "nickname" // 昵称

        var instance: QQLoginHelper? = null

        fun getQQInstance(appContext: Context): QQLoginHelper {
            if (instance == null) {
                instance = QQLoginHelper(appContext)
            }
            return instance!!
        }
    }
}