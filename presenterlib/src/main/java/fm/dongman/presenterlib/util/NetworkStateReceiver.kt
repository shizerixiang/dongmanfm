package fm.dongman.presenterlib.util

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast

/**
 * 监听网络状态变化
 * Created by shize on 2017/10/12.
 */
class NetworkStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        System.out.println("网络状态发生变化")
        //检测API是不是小于21，因为到了API21之后getNetworkInfo(int networkType)方法被弃用
        networkStateChanged21(context)
    }

    /**
     * 网络状态发生变化 21 系统以上
     */
    @SuppressLint("MissingPermission")
    private fun networkStateChanged21(context: Context?) {
        //API大于21时使用下面的方式进行网络监听
        System.out.println("API level 大于21")
        //获得ConnectivityManager对象
        val connMgr = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //获取所有网络连接的信息
        val networks = connMgr.allNetworks
        //用于存放网络连接信息
        var sb = "网络已断开！"
        //通过循环将网络信息逐个取出来
        networks.forEach {
            val networkInfo = connMgr.getNetworkInfo(it)
            sb = "${networkInfo.typeName} 已连接！"
        }
        Toast.makeText(context, sb, Toast.LENGTH_SHORT).show()
    }
}