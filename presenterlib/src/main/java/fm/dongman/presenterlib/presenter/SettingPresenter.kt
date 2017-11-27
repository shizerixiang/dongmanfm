package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.SettingContract
import fm.dongman.presenterlib.util.NetworkHelper
import fm.dongman.presenterlib.util.ValidateHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 设置数据操作类
 * Created by shize on 2017/10/18.
 */
class SettingPresenter constructor(private val mISettingView: SettingContract.ISettingView,
                                   private val mISettingDataSource: SettingContract.ISettingDataSource)
    : SettingContract.ISettingPresenter {

    init {
        mISettingView.setPresenter(this)
    }

    override fun startLoad() {
        // 检测网络
        if (!NetworkHelper.isNetworkAvailable(mISettingView.getViewContext())) {
            mISettingView.showNoNetwork()
            return
        }
        loadSource()
    }

    override fun refreshPage() {
        mISettingDataSource.refreshData()
        startLoad()
    }

    override fun about() = mISettingView.showAbout(mISettingDataSource.getAbout())

    override fun sendFeedBack(feedback: IModel.IFeedBackModel) {
        if (NetworkHelper.isUnLogged(mISettingView.getViewContext())) {
            mISettingView.showFeedBackState(NO_NETWORK)
            return
        }
        send(feedback)
    }

    override fun logout() {
        if (NetworkHelper.isUnLogged(mISettingView.getViewContext())) {
            mISettingView.showLogout(false)
            return
        }
        logoutAccount()
    }

    override fun cleanCache() {
        doAsync {
            mISettingDataSource.cleanCache(object : BaseDataSource.LoadSourceCallback<String> {
                override fun onDataLoaded(dataModel: String) {
                    uiThread {
                        if (mISettingView.isActive())
                            mISettingView.showCacheCleaned(true)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mISettingView.isActive())
                            mISettingView.showCacheCleaned(false)
                    }
                }
            })
        }
    }

    /**
     * 登出账号
     */
    private fun logoutAccount() {
        doAsync {
            mISettingDataSource.logout(object : BaseDataSource.LoadSourceCallback<String> {
                override fun onDataLoaded(dataModel: String) {
                    uiThread {
                        if (mISettingView.isActive())
                            mISettingView.showLogout(true)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mISettingView.isActive())
                            mISettingView.showLogout(false)
                    }
                }
            })
        }
    }

    /**
     * 预加载数据
     */
    private fun loadSource() {
        doAsync {
            mISettingDataSource.getData(object : BaseDataSource.LoadSourceCallback<String> {
                override fun onDataLoaded(dataModel: String) {
                    uiThread {
                        if (mISettingView.isActive())
                            showSource(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mISettingView.isActive())
                            mISettingView.showRefreshFailed()
                    }
                }
            })
        }
    }

    private fun showSource(dataModel: String) {
        if (ValidateHelper.isWebsite(dataModel)) {
            mISettingView.showVersion(dataModel)
        } else {
            mISettingView.showNewVersion()
        }
    }

    /**
     * 发送反馈
     */
    private fun send(feedback: IModel.IFeedBackModel) {
        doAsync {
            mISettingDataSource.sendFeedBack(feedback, object : BaseDataSource.LoadSourceCallback<String> {
                override fun onDataLoaded(dataModel: String) {
                    uiThread {
                        if (mISettingView.isActive())
                            mISettingView.showFeedBackState(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mISettingView.isActive())
                            mISettingView.showFeedBackState(FAILED_SEND)
                    }
                }
            })
        }
    }

    companion object {
        val NO_NETWORK: String = "没有网络！"
        val FAILED_SEND: String = "发送失败！"
    }
}