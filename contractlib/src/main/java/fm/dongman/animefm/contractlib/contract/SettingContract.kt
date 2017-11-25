package fm.dongman.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel

/**
 * 设置契约接口
 * Created by shize on 2017/10/17.
 */
interface SettingContract {
    /**
     * 设置界面接口
     */
    interface ISettingView : BaseView<ISettingPresenter> {
        /**
         * 显示可更新版本号
         * 由 [ISettingPresenter.startLoad] 回调
         * @param oldVersion 现版本号
         * @param newVersion 最新版本号
         */
        fun showVersion(oldVersion: String, newVersion: String)

        /**
         * 显示已经是最新版本
         * 由 [ISettingPresenter.startLoad] 回调
         */
        fun showNewVersion()

        /**
         * 显示关于
         * 由 [ISettingPresenter.about] 回调
         * @param url 关于页面地址
         */
        fun showAbout(url: String)

        /**
         * 显示发送反馈的状态
         * 由 [ISettingPresenter.sendFeedBack] 回调
         * @param msg 状态消息
         */
        fun showFeedBackState(msg: String)

        /**
         * 显示登出结果
         * 由 [ISettingPresenter.logout] 回调
         * @param isLogout 是否登出
         */
        fun showLogout(isLogout: Boolean)

        /**
         * 显示缓存清除结果
         * 由 [ISettingPresenter.cleanCache] 回调
         * @param isCleaned 是否清除
         */
        fun showCacheCleaned(isCleaned: Boolean)
    }

    /**
     * 设置数据操作接口
     */
    interface ISettingPresenter : BasePresenter {
        /**
         * 加载关于信息
         * 调用 [ISettingDataSource.getAbout] 获取关于数据，并调用 [ISettingView.showAbout] 显示关于信息
         */
        fun about()

        /**
         * 发送反馈信息
         * 在子线程中调用 [ISettingDataSource.sendFeedBack] 获取发送状态
         * 在主线程中调用 [ISettingView.showFeedBackState] 显示数据发送状态
         * @param feedback 反馈信息
         */
        fun sendFeedBack(feedback: IModel.IFeedBackModel)

        /**
         * 登出账号
         * 在子线程中调用 [ISettingDataSource.logout] 清除数据
         * 在主线程中调用 [ISettingView.showLogout] 显示清除状态
         */
        fun logout()

        /**
         * 清除缓存
         * 在子线程中调用 [ISettingDataSource.cleanCache] 清除数据
         * 在主线程中调用 [ISettingView.showCacheCleaned] 显示清除状态
         */
        fun cleanCache()
    }

    /**
     * 设置数据源接口
     * 预加载需要获取版本号
     */
    interface ISettingDataSource : BaseDataSource<String> {
        /**
         * 解析关于，固定地址
         * @return 关于页面地址
         */
        fun getAbout(): String

        /**
         * 发送并解析反馈状态
         * @param feedback 反馈信息
         * @return 反馈发送状态
         */
        fun sendFeedBack(feedback: IModel.IFeedBackModel, callback: BaseDataSource.LoadSourceCallback<String>)

        /**
         * 清除本地登录信息
         * @param callback 清除状态回调
         */
        fun logout(callback: BaseDataSource.LoadSourceCallback<String>)

        /**
         * 清除缓存
         * @param callback 状态回调
         */
        fun cleanCache(callback: BaseDataSource.LoadSourceCallback<String>)
    }
}