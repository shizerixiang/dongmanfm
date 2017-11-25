package fm.dongman.integratedlib.sina

import android.app.Activity
import android.content.Context
import android.content.Intent

import com.sina.weibo.sdk.auth.AccessTokenKeeper
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler

import fm.dongman.integratedlib.QQWBWXLoginHelper
import org.jetbrains.anko.doAsync

/**
 * 集成第三方微博登录接口
 * Created by KUIGE on 2017/11/17.
 */

class WBLoginHelper(activity: Activity) {
    private val mSsoHandler: SsoHandler = SsoHandler(activity)
    private val mAuthListener: AuthListener = AuthListener(activity.applicationContext)
    private var mCallback: QQWBWXLoginHelper.LoginResultCallback? = null // 数据回调

    /**
     * 登录新浪
     * @param pattern 设置授权模式
     */
    fun loginSinaWB(pattern: Int, callback: QQWBWXLoginHelper.LoginResultCallback) {
        mCallback = callback
        when (pattern) {
            WB_AUTO -> mSsoHandler.authorize(mAuthListener)
            WB_WEB -> mSsoHandler.authorizeWeb(mAuthListener)
            WB_CLIENT -> mSsoHandler.authorizeClientSso(mAuthListener)
        }
    }

    /**
     * 此方法在SingleActivity中调用
     */
    fun loginSinaWBResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mSsoHandler.authorizeCallBack(requestCode, resultCode, data)
    }

    /**
     * 微博登录数据监听器
     */
    private inner class AuthListener internal constructor(private val appContext: Context)
        : WbAuthListener {
        override fun onSuccess(oauth2AccessToken: Oauth2AccessToken) {
            try {
                AccessTokenKeeper.writeAccessToken(appContext, oauth2AccessToken)
                val userModel = QQWBWXLoginHelper.LoginUserModel()
                userModel.mServiceName = SERVICE_NAME
                userModel.mUid = oauth2AccessToken.uid
                doAsync {
                    mCallback!!.onCompletedLogin(userModel)
                }
            } catch (e: Exception) {
                mCallback!!.onCancelOrError("登录成功，但数据转换错误！数据为：$oauth2AccessToken")
                e.printStackTrace()
            }
        }

        override fun cancel() {
            mCallback!!.onCancelOrError("登录取消！")
        }

        override fun onFailure(wbConnectErrorMessage: WbConnectErrorMessage) {
            mCallback!!.onCancelOrError("登录错误！Code：${wbConnectErrorMessage.errorCode}\n   msg：${wbConnectErrorMessage.errorMessage}")
        }
    }

    companion object {
        val APK_ID = QQWBWXLoginHelper.APP_KEY_SINA // 新浪 app key
        val REDIRECT_URL = "https://api.weibo.com/oauth2/default.html"
        val SCOPE: String? = null
        private val SERVICE_NAME = "weibo" // 第三方平台
        val WB_WEB = 1 // 新浪网页登录
        val WB_AUTO = 0 // 新浪自动登录
        val WB_CLIENT = 2 // 新浪客户端登录
        private var SINGLETON: WBLoginHelper? = null

        fun getSinaInstance(activity: Activity): WBLoginHelper {
            if (SINGLETON == null) {
                SINGLETON = WBLoginHelper(activity)
            }
            return SINGLETON!!
        }
    }

}
