package fm.dongman.presenterlib.presenter

import android.app.Activity
import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.LoginContract
import fm.dongman.presenterlib.util.NetworkHelper
import fm.dongman.presenterlib.util.ValidateHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 登录数据操作
 * Created by shize on 10/15/17.
 */
class LoginPresenter(private val mLoginView: LoginContract.ILoginView,
                     private val mLoginSource: LoginContract.ILoginDataSource)
    : LoginContract.ILoginPresenter {

    init {
        mLoginView.setPresenter(this)
    }

    override fun startLoad() {}

    override fun refreshPage() {}

    override fun login(userName: String, pwd: String): Boolean {
        if (!NetworkHelper.isNetworkAvailable(mLoginView.getViewContext())) {
            mLoginView.showNoNetwork()
            return false
        }
        // 检测登录信息是否合法
        if ((ValidateHelper.checkEmail(userName) || ValidateHelper.checkPhoneNum(userName)) && pwd.trim().isNotEmpty()) {
            loginUser(userName, pwd)
            return true
        }
        return false
    }

    override fun loginSocial(type: Int) {
        if (!NetworkHelper.isNetworkAvailable(mLoginView.getViewContext())) {
            mLoginView.showNoNetwork()
            return
        }
        doAsync {
            mLoginSource.loginSocial(mLoginView.getViewContext() as Activity, type, object : BaseDataSource.LoadSourceCallback<IModel.IUserModel> {
                override fun onDataLoaded(dataModel: IModel.IUserModel) {
                    uiThread {
                        if (mLoginView.isActive())
                            mLoginView.showLoginSucceed(dataModel)
                    }
                    mLoginSource.saveData(dataModel)
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        mLoginView.showLoginFailed()
                    }
                }
            })
        }
    }

    /**
     * 登录操作
     */
    private fun loginUser(userName: String, pwd: String) {
        doAsync {
            val mLoginCallback: BaseDataSource.LoadSourceCallback<IModel.IUserModel> = object
                : BaseDataSource.LoadSourceCallback<IModel.IUserModel> {
                override fun onDataLoaded(dataModel: IModel.IUserModel) {
                    uiThread {
                        if (mLoginView.isActive())
                            mLoginView.showLoginSucceed(dataModel)
                    }
                    mLoginSource.saveData(dataModel)
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mLoginView.isActive())
                            mLoginView.showLoginFailed()
                    }
                }
            }
            mLoginSource.login(userName, pwd, mLoginCallback)
        }
    }
}
