package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.contract.RegisterContract
import fm.dongman.presenterlib.util.NetworkHelper
import fm.dongman.presenterlib.util.ValidateHelper.checkPwd
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 注册/忘记密码数据操作
 * Created by shize on 2017/10/28.
 */
class RegisterPresenter constructor(private val mIRegisterView: RegisterContract.IRegisterView,
                                    private val mIRegisterDataSource: RegisterContract.IRegisterDataSource)
    : RegisterContract.IRegisterPresenter {

    init {
        mIRegisterView.setPresenter(this)
    }

    override fun startLoad() {}

    override fun refreshPage() {}

    override fun validate(phone: String, type: Int): Boolean {
        if (!NetworkHelper.isNetworkAvailable(mIRegisterView.getViewContext())) {
            mIRegisterView.showNoNetwork()
            return false
        }
        // 手机号码正则表达式
        val regexPhone = "1(3[0-9]|47|5((?!4)[0-9])|7(0|1|[6-8])|8[0-9])\\d{8}".toRegex()
        // 检测是否为手机号
        if (phone.trim().matches(regexPhone)) {
            validatePhoneNumber(phone, type)
            return true
        }
        return false
    }

    /**
     * 检测手机号
     */
    private fun validatePhoneNumber(phone: String, type: Int) {
        doAsync {
            mIRegisterDataSource.validatePhoneNumber(phone, type, object : BaseDataSource.LoadSourceCallback<String> {
                override fun onDataLoaded(dataModel: String) {
                    uiThread {
                        if (mIRegisterView.isActive())
                            mIRegisterView.showValidateSucceed()
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIRegisterView.isActive())
                            mIRegisterView.showRegisterFailed()
                    }
                }
            })
        }
    }

    override fun register(phone: String, pwd: String, validateNum: String): Boolean {
        if (!NetworkHelper.isNetworkAvailable(mIRegisterView.getViewContext())) {
            mIRegisterView.showNoNetwork()
            return false
        }
        if (checkPwd(pwd)) {
            registerAccount(phone, pwd, validateNum)
            return true
        }
        return false
    }

    /**
     * 注册账号
     */
    private fun registerAccount(phone: String, pwd: String, validateNum: String) {
        doAsync {
            mIRegisterDataSource.registerAccount(phone, pwd, validateNum, object : BaseDataSource.LoadSourceCallback<String> {
                override fun onDataLoaded(dataModel: String) {
                    uiThread {
                        if (mIRegisterView.isActive())
                            mIRegisterView.showRegisterSucceed()
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIRegisterView.isActive())
                            mIRegisterView.showRegisterFailed()
                    }
                }
            })
        }
    }

    override fun modify(phone: String, newPwd: String, validateNum: String, modifyType: Int): Boolean {
        if (!NetworkHelper.isNetworkAvailable(mIRegisterView.getViewContext())) {
            mIRegisterView.showNoNetwork()
            return false
        }
        if (checkPwd(newPwd)) {
            modifyPwd(phone, newPwd, validateNum, modifyType)
            return true
        }
        return false
    }

    /**
     * 修改/重置密码
     */
    private fun modifyPwd(phone: String, newPwd: String, validateNum: String, modifyType: Int) {
        doAsync {
            mIRegisterDataSource.modifyPassword(phone, newPwd, validateNum, modifyType, object : BaseDataSource.LoadSourceCallback<String> {
                override fun onDataLoaded(dataModel: String) {
                    uiThread {
                        if (mIRegisterView.isActive())
                            mIRegisterView.showModifyPasswordSucceed()
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIRegisterView.isActive())
                            mIRegisterView.showModifyPasswordFailed()
                    }
                }
            })
        }
    }

    override fun bindPhone(phone: String, validateNum: String) {
        if (NetworkHelper.isUnLogged(mIRegisterView.getViewContext())){
            mIRegisterView.showBindPhoneResult("登录验证失败！")
            return
        }

    }
}