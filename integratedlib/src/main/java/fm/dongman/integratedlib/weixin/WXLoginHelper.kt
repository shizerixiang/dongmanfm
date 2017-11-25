package fm.dongman.integratedlib.weixin

import android.content.Context
import android.content.Intent
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import fm.dongman.integratedlib.QQWBWXLoginHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 微信登录
 * Created by shize on 2017/11/21.
 */
class WXLoginHelper(appContext: Context) {
    private var mApi: IWXAPI = WXAPIFactory.createWXAPI(appContext, APP_ID, true)
    private var mListener:QQWBWXLoginHelper.LoginResultCallback? = null

    /**
     * 微信登录
     */
    fun loginWX(listener: QQWBWXLoginHelper.LoginResultCallback) {
        mApi.registerApp(APP_ID)
        if (!mApi.isWXAppInstalled) {
            doAsync {
                uiThread {
                    listener.onCancelOrError("未检测到微信客户端或签名错误！")
                }
            }
            return
        }
        // 存入回调
        mListener = listener
        val req = SendAuth.Req()
        req.scope = SCOPE
        req.state = STATE
        mApi.sendReq(req)
    }

    /**
     * 微信登录结果
     * 在WXEntryActivity中将接收到的intent及实现了IWXAPIEventHandler接口的对象传递给IWXAPI接口的handleIntent方法
     */
    fun loginWXResult(intent: Intent, handler: IWXEventHandler) {
        // 将回调给负责处理接收的数据的 activity
        handler.setLoginResultCallback(mListener)
        mApi.handleIntent(intent, handler)
    }

    companion object {
        val SERVICE_NAME = "weixin" // 平台
        val APP_ID = QQWBWXLoginHelper.APP_KEY_WX // 微信 app key
        val APP_SECRET = QQWBWXLoginHelper.APP_SECRET_WX // 微信 secret
        val SCOPE = "snsapi_userinfo" // 微信用户信息授权域
        val STATE = "csrf_defence" // 防御 csrf 攻击参数
        var INSTANCE: WXLoginHelper? = null

        fun getWXInstance(appContext: Context): WXLoginHelper {
            if (INSTANCE == null) {
                INSTANCE = WXLoginHelper(appContext)
            }
            return INSTANCE!!
        }
    }

    /**
     * 扩展微信API接口
     */
    interface IWXEventHandler :IWXAPIEventHandler{
        /**
         * 设置结果监听事件
         * @param callback 登录结果回调接口
         */
        fun setLoginResultCallback(callback: QQWBWXLoginHelper.LoginResultCallback?)
    }
}