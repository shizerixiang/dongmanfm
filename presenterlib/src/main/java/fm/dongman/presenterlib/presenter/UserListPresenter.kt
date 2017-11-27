package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.UserListContract
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 用户列表数据操作类
 * Created by shize on 2017/10/19.
 */
class UserListPresenter constructor(private val mIUserListView: UserListContract.IUserListView,
                                    private val mIUserListDataSource: UserListContract.IUserListDataSource)
    : UserListContract.IUserListPresenter {

    private var mType: String = UserListContract.IUserListPresenter.TYPE_SEARCH // 类型
    private var mKey: String = "" // 关键字
    private var mPage: Int = 1 // 页数

    init {
        mIUserListView.setPresenter(this)
    }

    override fun startLoad() {}

    override fun refreshPage() {
        mIUserListDataSource.refreshData()
        setKey(mPage, mType, mKey)
    }

    override fun setKey(page: Int, type: String, key: String) {
        mPage = page
        mType = type
        mKey = key
        loadSource()
    }

    /**
     * 预加载数据
     */
    private fun loadSource() {
        when (mType) {
            UserListContract.IUserListPresenter.TYPE_SEARCH -> {
                searchUsers()
            }
            UserListContract.IUserListPresenter.TYPE_FOLLOW -> {
                if (mPage == 1)
                    loadFollow()
            }
            UserListContract.IUserListPresenter.TYPE_FAN -> {
                if (mPage == 1)
                    loadFan()
            }
        }
    }

    /**
     * 搜索用户
     */
    private fun searchUsers() {
        if (!NetworkHelper.isNetworkAvailable(mIUserListView.getViewContext())) {
            mIUserListView.showNoNetwork()
            return
        }
        doAsync {
            mIUserListDataSource.searchUsers(mPage, mKey, object : BaseDataSource.LoadSourceCallback<List<IModel.IUserModel>> {
                override fun onDataLoaded(dataModel: List<IModel.IUserModel>) {
                    uiThread {
                        if (mIUserListView.isActive())
                            showUserList(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIUserListView.isActive())
                            mIUserListView.showRefreshFailed()
                    }
                }
            })
        }
    }

    /**
     * 加载关注
     */
    private fun loadFollow() {
        if (NetworkHelper.isUnLogged(mIUserListView.getViewContext())) {
            mIUserListView.showNoNetwork()
            return
        }
        doAsync {
            mIUserListDataSource.followUsers(mPage, mKey, object : BaseDataSource.LoadSourceCallback<List<IModel.IUserModel>> {
                override fun onDataLoaded(dataModel: List<IModel.IUserModel>) {
                    uiThread {
                        if (mIUserListView.isActive())
                            showUserList(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIUserListView.isActive())
                            mIUserListView.showRefreshFailed()
                    }
                }
            })
        }
    }

    /**
     * 加载粉丝
     */
    private fun loadFan() {
        if (NetworkHelper.isUnLogged(mIUserListView.getViewContext())) {
            mIUserListView.showNoNetwork()
            return
        }
        doAsync {
            mIUserListDataSource.fanUsers(mPage, mKey, object : BaseDataSource.LoadSourceCallback<List<IModel.IUserModel>> {
                override fun onDataLoaded(dataModel: List<IModel.IUserModel>) {
                    uiThread {
                        if (mIUserListView.isActive())
                            showUserList(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIUserListView.isActive())
                            mIUserListView.showRefreshFailed()
                    }
                }
            })
        }
    }

    /**
     * 显示用户列表
     */
    private fun showUserList(dataModel: List<IModel.IUserModel>) {
        if (mPage == 1) {
            mIUserListView.showUserList(dataModel)
            return
        }
        mIUserListView.showMoreUserList(dataModel)

    }
}