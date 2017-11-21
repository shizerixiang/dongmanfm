package fm.dongman.contractlib.contract

import fm.dongman.contractlib.BasePresenter
import fm.dongman.contractlib.BaseDataSource
import fm.dongman.contractlib.BaseView
import fm.dongman.contractlib.IModel

/**
 * 发现契约接口
 * Created by shize on 2017/9/21.
 */
interface MyContract {
    /**
     * 发现界面，启动调用 [IMyPresenter.startLoad] 加载数据
     */
    interface IMyView : BaseView<IMyPresenter> {
        /**
         * 显示没有登录
         * [IMyPresenter.startLoad] 加载登录信息失败回调
         * 或者登出账号，由 [IMyPresenter.logoutAccount] 回调该方法
         */
        fun showMyLogout()

        /**
         * 显示登录后的个人信息
         * [IMyPresenter.startLoad] 加载登录信息成功回调
         * 或者登录成功，从广播接收到了用户信息
         * @param userInfo
         */
        fun showMyLogin(userInfo: IModel.IUserModel)
    }

    /**
     * 发现操作， [startLoad] 回调中根据成功失败调用 [IMyView.showMyLogin] 或 [IMyView.showMyLogout]
     */
    interface IMyPresenter : BasePresenter {
        /**
         * 登出账号，调用 [IMyDataSource.deleteAccount] 删除本地数据
         * 并调用 [IMyView.showMyLogout] 更新界面为未登录状态
         */
        fun logoutAccount()
    }

    /**
     * 发现数据源
     * 从本地获取用户信息，没有则直接返回获取失败
     */
    interface IMyDataSource : BaseDataSource<IModel.IUserModel> {
        /**
         * 删除账号信息
         */
        fun deleteAccount()
    }

    companion object{
        var USER_ID:String? = null // 当前登录的用户 id，未登录为 null，登录后为用户 id
        var SESSION_ID:String? = null // 登录后，必定会有 sessionId
    }
}