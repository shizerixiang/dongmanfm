package fm.dongman.contractlib.contract

import android.app.Activity
import fm.dongman.contractlib.BaseDataSource
import fm.dongman.contractlib.BasePresenter
import fm.dongman.contractlib.BaseView
import fm.dongman.contractlib.IModel

/**
 * 登录、注册契约接口
 * Created by shize on 2017/10/12.
 */
interface LoginContract {
    /**
     * 登录、注册界面接口
     */
    interface ILoginView : BaseView<ILoginPresenter> {
        /**
         * 显示登录成功
         * @param userInfo 用户信息
         */
        fun showLoginSucceed(userInfo: IModel.IUserModel)

        /**
         * 显示登录失败
         */
        fun showLoginFailed()
    }

    /**
     * 登录、注册操作接口
     */
    interface ILoginPresenter : BasePresenter {
        /**
         * 登录操作，异步调用 [ILoginDataSource.login] 登录
         * 并在 UI 线程中通过 [ILoginView.showLoginSucceed] [ILoginView.showLoginFailed] 回调显示登录结果
         * @param userName 用户名
         * @param pwd 密码
         * @return 是否符合格式
         */
        fun login(userName: String, pwd: String): Boolean

        /**
         * 社会化登录，异步调用 [ILoginDataSource.loginSocial] 登录
         * 并在 UI 线程中通过 [ILoginView.showLoginSucceed] [ILoginView.showLoginFailed] 回调显示登录结果
         * @param type 登录类型 [LOGIN_QQ]、[LOGIN_SINA]
         */
        fun loginSocial(type:Int)

        companion object {
            val LOGIN_QQ = 1 // 腾讯 qq
            val LOGIN_SINA = 2 // 新浪微博
        }
    }

    /**
     * 登录、注册数据接口
     */
    interface ILoginDataSource : BaseDataSource<IModel.IUserModel> {
        /**
         * 登录
         * @param userName 用户名
         * @param pwd 密码
         * @param callback 数据回调
         */
        fun login(userName: String, pwd: String, callback: BaseDataSource.LoadSourceCallback<IModel.IUserModel>)

        /**
         * 社会化登录
         * @param type 登录类型
         */
        fun loginSocial(activity: Activity, type: Int, callback: BaseDataSource.LoadSourceCallback<IModel.IUserModel>)
    }
}