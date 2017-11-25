package fm.dongman.integratedlib.weixin.wxapi

import android.app.Activity
import android.os.Bundle
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import fm.dongman.integratedlib.QQWBWXLoginHelper
import fm.dongman.integratedlib.weixin.WXLoginHelper
import fm.dongman.integratedlib.weixin.WXLoginHelper.Companion.APP_ID
import fm.dongman.integratedlib.weixin.WXLoginHelper.Companion.APP_SECRET
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.json.JSONObject

/**
 * 微信 API 数据接收 activity
 * Created by shize on 2017/11/24.
 */
abstract class WXEntryActivity : Activity(), WXLoginHelper.IWXEventHandler {
    private var mCallback: QQWBWXLoginHelper.LoginResultCallback? = null // 结果回调
    private val mUserInfo: QQWBWXLoginHelper.LoginUserModel = QQWBWXLoginHelper.LoginUserModel() // 用户信息
    private var mMessage:String = "登录失败！"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 回调数据接收方法
        WXLoginHelper.getWXInstance(this.applicationContext).loginWXResult(intent, this)
    }

    /**
     * 登录信息反馈
     * @param msg 登录信息
     */
    abstract fun onLoginMsg(msg:String)

    override fun setLoginResultCallback(callback: QQWBWXLoginHelper.LoginResultCallback?) {
        mCallback = callback
    }

    override fun onReq(p0: BaseReq?) {    }

    override fun onResp(p0: BaseResp?) {
        if (p0 == null) {
            return
        }
        // 检查错误码
        when (p0.errCode) {
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                // 用户取消授权
                onLoginMsg("取消登录")
                mCallback?.onCancelOrError("取消登录！")
            }
            BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                // 用户拒绝授权
                onLoginMsg("拒绝授权")
                mCallback?.onCancelOrError("拒绝授权！")
            }
            BaseResp.ErrCode.ERR_OK -> {
                // 用户同意授权
                // 向第三方请求 Token
                doAsync {
                    getAccessToken((p0 as SendAuth.Resp).code)
                }
            }
            else -> {
                // 其他未知错误
                onLoginMsg(mMessage)
                mCallback?.onCancelOrError("未知错误！")
            }
        }
    }

    /**
     * 获取access_token：
     *
     * @param code 用户或取access_token的code，仅在ErrCode为0时有效
     */
    private fun getAccessToken(code: String) {
        val url = USER_TOKEN_URL + "appid=$APP_ID&secret=$APP_SECRET&code=$code&grant_type=authorization_code"
        val json = parseGetUrl(url)
        if (json == null) {
            runOnUiThread {
                onLoginMsg(mMessage)
                mCallback?.onCancelOrError("请求Token失败！")
            }
            return
        }
        try {
            // 获取微信用户个人信息
            getWXUserInfo(json.optString("access_token"), json.optString("openid"))
        } catch (e: Exception) {
            runOnUiThread {
                onLoginMsg(mMessage)
                mCallback?.onCancelOrError("获取Token返回值失败！")
            }
            e.printStackTrace()
        }
    }

    /**
     * 获取微信登录，用户授权后的个人信息
     *
     * @param token
     * @param openId
     */
    private fun getWXUserInfo(token: String, openId: String) {
        val url = USER_INFO_URL + "access_token=$token&openid=$openId"
        val json = parseGetUrl(url)
        if (json == null) {
            runOnUiThread {
                onLoginMsg(mMessage)
                mCallback?.onCancelOrError("请求用户信息失败！")
            }
            return
        }
        try {
            mUserInfo.mServiceName = WXLoginHelper.SERVICE_NAME
            mUserInfo.mUid = json.optString("openid")
            mUserInfo.mNickName = json.optString("nickname")
            mUserInfo.mAvatarUrl = json.optString("headimgurl")
            mUserInfo.mGender = Math.abs(json.optInt("sex") - 2)
        } catch (e: Exception) {
            runOnUiThread {
                onLoginMsg(mMessage)
                mCallback?.onCancelOrError("获取用户信息返回值失败！")
            }
            e.printStackTrace()
        }
        mCallback?.onCompletedLogin(mUserInfo)
        runOnUiThread {
            onLoginMsg("登录成功")
        }
    }

    /**
     * 解析 Get 请求地址
     * @param parseUrl get请求地址
     * @return 解析失败返回 null，解析成功返回 JSONObject
     */
    fun parseGetUrl(parseUrl: String): JSONObject? {
        var getJson: JSONObject? = null
        val mOkHttpClient = OkHttpClient()
        try {
            val mRequest = Request.Builder().url(parseUrl).get().build()
            val mResponse = mOkHttpClient.newCall(mRequest).execute()
            getJson = JSONObject(mResponse.body()!!.string())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return getJson
    }

    companion object {
        val USER_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?" // 用户 Token 和 openId 获取地址前缀
        val USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?" // 用户信息获取地址前缀
    }
}