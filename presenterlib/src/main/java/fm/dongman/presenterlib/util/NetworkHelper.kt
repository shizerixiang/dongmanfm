package fm.dongman.presenterlib.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import fm.dongman.animefm.contractlib.contract.MyContract

object NetworkHelper {
    /**
     * 检测是否有可用网络活动
     * @param context 上下文
     * @return 网络是否可用
     */
    @SuppressLint("MissingPermission")
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networks = connectivityManager.allNetworks
        networks.filter { connectivityManager.getNetworkInfo(it).state == NetworkInfo.State.CONNECTED }.forEach { return true }
        return false
    }

    /**
     * 检测用户是否登录以及网络状态
     * @param context 上下文
     * @return 是否无法操作
     */
    fun isUnLogged(context: Context) = !NetworkHelper.isNetworkAvailable(context) || MyContract.USER_ID == null
}