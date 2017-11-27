package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.UserInfoContract
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 用户信息数据操作类
 * Created by shize on 2017/10/19.
 */
class UserInfoPresenter constructor(private val mIUserInfoView: UserInfoContract.IUserInfoView,
                                    private val mIUserInfoDataSource: UserInfoContract.IUserInfoDataSource)
    : UserInfoContract.IUserInfoPresenter {

    private var mId: String = "" // 用户 id

    init {
        mIUserInfoView.setPresenter(this)
    }

    override fun startLoad() {}

    override fun refreshPage() {
        mIUserInfoDataSource.refreshData()
        getUserInfo(mId)
    }

    override fun getUserInfo(id: String) {
        mId = id
        if (!NetworkHelper.isNetworkAvailable(mIUserInfoView.getViewContext())) {
            mIUserInfoView.showNoNetwork()
            return
        }
        loadSource()
    }

    override fun followUser(id: String, follow: Int) {
        if (NetworkHelper.isUnLogged(mIUserInfoView.getViewContext())) {
            mIUserInfoView.showFollowFailed()
            return
        }
        follow(id, follow)
    }

    /**
     * 预加载数据
     */
    private fun loadSource() {
        mIUserInfoDataSource.setUserId(mId)
        doAsync {
            mIUserInfoDataSource.getData(object : BaseDataSource.LoadSourceCallback<IModel.IUserModel> {
                override fun onDataLoaded(dataModel: IModel.IUserModel) {
                    uiThread {
                        if (mIUserInfoView.isActive())
                            mIUserInfoView.showUserInfo(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIUserInfoView.isActive())
                            mIUserInfoView.showRefreshFailed()
                    }
                }
            })
        }
    }

    /**
     * 关注用户
     */
    private fun follow(id: String, follow: Int) {
        doAsync {
            mIUserInfoDataSource.followUser(follow, id, object : BaseDataSource.LoadSourceCallback<Void> {
                override fun onDataLoaded(dataModel: Void) {
                    uiThread {
                        if (mIUserInfoView.isActive())
                            mIUserInfoView.showFollowSucceed()
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIUserInfoView.isActive())
                            mIUserInfoView.showFollowFailed()
                    }
                }
            })
        }
    }
}