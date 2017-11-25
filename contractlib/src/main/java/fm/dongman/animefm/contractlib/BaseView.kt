package fm.dongman.animefm.contractlib

import android.content.Context

/**
 * View 层接口
 * Created by shize on 2017/9/21.
 */
interface BaseView<in T> {
    /**
     * 设置 Presenter
     */
    fun setPresenter(presenter: T)

    /**
     * 检测是否操作安全
     */
    fun isActive(): Boolean

    /**
     * 获取上下文
     */
    fun getViewContext():Context

    /**
     * 显示没有网络
     */
    fun showNoNetwork()

    /**
     * 刷新失败
     */
    fun showRefreshFailed()
}