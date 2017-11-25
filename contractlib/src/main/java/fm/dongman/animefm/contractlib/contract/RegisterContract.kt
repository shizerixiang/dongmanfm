package fm.dongman.animefm.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView

/**
 * 注册/忘记密码契约接口
 * Created by shize on 2017/10/28.
 */
interface RegisterContract {
    /**
     * 注册/忘记密码页面接口
     */
    interface IRegisterView : BaseView<IRegisterPresenter> {
        /**
         * 显示发送验证成功
         * 由 [IRegisterPresenter.validate] 回调
         */
        fun showValidateSucceed()

        /**
         * 显示发送验证失败
         * 由 [IRegisterPresenter.validate] 回调
         */
        fun showValidateFailed()

        /**
         * 显示注册成功
         * 由 [IRegisterPresenter.register] 回调
         */
        fun showRegisterSucceed()

        /**
         * 显示注册失败
         * 由 [IRegisterPresenter.register] 回调
         */
        fun showRegisterFailed()

        /**
         * 显示修改密码成功
         * 由 [IRegisterPresenter.modify] 回调
         */
        fun showModifyPasswordSucceed()

        /**
         * 显示修改密码失败
         * 由 [IRegisterPresenter.modify] 回调
         */
        fun showModifyPasswordFailed()

        /**
         * 显示绑定/修改手机结果
         * 由 [IRegisterPresenter.bindPhone] 回调
         * @param msg 绑定结果消息
         */
        fun showBindPhoneResult(msg: String)
    }

    /**
     * 注册/忘记密码数据操作接口
     */
    interface IRegisterPresenter : BasePresenter {
        /**
         * 验证手机号，异步调用 [IRegisterDataSource.validatePhoneNumber] 登录
         * 并在 UI 线程中通过 [IRegisterView.showValidateSucceed] [IRegisterView.showValidateFailed] 回调显示登录结果
         * @param phone 手机号 11位
         * @param type 场景类型，类型详见 [IRegisterDataSource] 的实现类
         * @return 手机号是否合法
         */
        fun validate(phone: String, type: Int): Boolean

        /**
         * 注册账号，异步调用 [IRegisterDataSource.registerAccount] 登录
         * 并在 UI 线程中通过 [IRegisterView.showRegisterSucceed] [IRegisterView.showRegisterFailed] 回调显示登录结果
         * @param phone 手机号 11位
         * @param pwd 密码
         * @param validateNum 验证码
         * @return 密码是否合法
         */
        fun register(phone: String, pwd: String, validateNum: String): Boolean

        /**
         * 修改/重置密码，异步调用 [IRegisterDataSource.modifyPassword] 登录
         * 并在 UI 线程中通过 [IRegisterView.showModifyPasswordSucceed] [IRegisterView.showModifyPasswordFailed] 回调显示登录结果
         * @param phone 手机号 11位
         * @param newPwd 新密码
         * @param validateNum 验证码
         * @param modifyType 修改类型，详见 [IRegisterDataSource] 的实现类
         * @return 新密码是否合法
         */
        fun modify(phone: String, newPwd: String, validateNum: String, modifyType: Int): Boolean

        /**
         * 绑定手机号，异步调用 [IRegisterDataSource.bindPhone] 登录
         * 并在 UI 线程中通过 [IRegisterView.showBindPhoneResult] 回调显示登录结果
         * @param phone 手机号
         * @param validateNum 验证码
         */
        fun bindPhone(phone: String, validateNum: String)
    }

    /**
     * 注册/忘记密码数据源接口
     */
    interface IRegisterDataSource : BaseDataSource<Void> {
        /**
         * 验证手机号
         * @param phone 手机号
         * @param type 发送场景类型
         * @param callback 验证状态回调
         */
        fun validatePhoneNumber(phone: String, type: Int, callback: BaseDataSource.LoadSourceCallback<String>)

        /**
         * 注册账号
         * @param phone 手机号
         * @param pwd 密码
         * @param validateNum 验证码
         * @param callback 注册状态回调
         */
        fun registerAccount(phone: String, pwd: String, validateNum: String, callback: BaseDataSource.LoadSourceCallback<String>)

        /**
         * 修改/重置密码
         * @param phone 手机号
         * @param newPwd 新密码
         * @param validateNum 验证码
         * @param modifyType 修改密码类型，详见 [IRegisterDataSource] 的实现类
         * @param callback 修改密码状态回调
         */
        fun modifyPassword(phone: String, newPwd: String, validateNum: String, modifyType: Int, callback: BaseDataSource.LoadSourceCallback<String>)

        /**
         * 绑定手机号
         * @param phone 手机号
         * @param validateNum 验证码
         * @param callback 回调接口
         */
        fun bindPhone(phone: String, validateNum: String, callback: BaseDataSource.LoadSourceCallback<String>)
    }
}