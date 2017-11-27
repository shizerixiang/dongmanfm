package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.MyContract
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 漫圈数据操作
 * Created by shize on 2017/10/15.
 */
class MyPresenter constructor(private val mMyView: MyContract.IMyView,
                              private val mMySource: MyContract.IMyDataSource)
    : MyContract.IMyPresenter {

    init {
        mMyView.setPresenter(this)
    }

    override fun startLoad() {
        // 检测网络
        if (!NetworkHelper.isNetworkAvailable(mMyView.getViewContext())) {
            // 离线加载
            loadOffLineSource()
            return
        }
        // 正常加载
        loadSource()
    }

    override fun refreshPage() {
        mMySource.refreshData()
        startLoad()
    }

    override fun logoutAccount() {
        // 清除内存中的登录信息
        MyContract.USER_ID = null
        MyContract.SESSION_ID = null
        // 清除存储中的登录信息
        doAsync {
            mMySource.deleteAccount()
        }
    }

    /**
     * 加载数据
     */
    private fun loadSource() {
        doAsync {
            mMySource.getData(object : BaseDataSource.LoadSourceCallback<IModel.IUserModel> {
                override fun onDataLoaded(dataModel: IModel.IUserModel) {
                    uiThread {
                        if (mMyView.isActive())
                            showSource(dataModel)
                    }
                    // 保存数据
                    mMySource.saveData(dataModel)
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mMyView.isActive())
                            mMyView.showRefreshFailed()
                    }
                    // 离线加载数据
                    loadOffLineSource()
                }
            })
        }
    }

    /**
     * 离线加载数据
     */
    private fun loadOffLineSource() {
        doAsync {
            mMySource.getData(object : BaseDataSource.LoadSourceCallback<IModel.IUserModel> {
                override fun onDataLoaded(dataModel: IModel.IUserModel) {
                    uiThread {
                        if (mMyView.isActive())
                            showSource(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mMyView.isActive())
                            mMyView.showNoNetwork()
                    }
                }
            })
        }
    }

    /**
     * 显示数据
     */
    private fun showSource(dataModel: IModel.IUserModel) {
        mMyView.showMyLogin(dataModel)
    }
}