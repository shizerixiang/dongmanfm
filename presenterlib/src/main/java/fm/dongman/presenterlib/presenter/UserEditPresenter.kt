package fm.dongman.presenterlib.presenter

import android.graphics.Bitmap
import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.UserEditContract
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 用户编辑数据操作类
 * Created by shize on 2017/11/24.
 */
class UserEditPresenter(private val mIUserEditView: UserEditContract.IUserEditView,
                        private val mIUserEditDataSource: UserEditContract.IUserEditDataSource)
    : UserEditContract.IUserEditPresent {
    private var mUserData: IModel.IUserModel? = null // 用户数据模型

    init {
        mIUserEditView.setPresenter(this)
    }

    override fun startLoad() {
        if (!NetworkHelper.isNetworkAvailable(mIUserEditView.getViewContext())) {
            mIUserEditView.showNoNetwork()
            return
        }
        doAsync {
            mIUserEditDataSource.getData(object : BaseDataSource.LoadSourceCallback<IModel.IUserModel> {
                override fun onDataLoaded(dataModel: IModel.IUserModel) {
                    uiThread {
                        if (mIUserEditView.isActive())
                            mIUserEditView.showUserInfo(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIUserEditView.isActive())
                            mIUserEditView.showEditFailed()
                    }
                }
            })
        }
    }

    override fun refreshPage() {
        mIUserEditDataSource.refreshData()
        startLoad()
    }

    override fun editUserInfo(userInfo: IModel.IUserModel) {
        if (!NetworkHelper.isNetworkAvailable(mIUserEditView.getViewContext())) {
            mIUserEditView.showNoNetwork()
            return
        }
        mUserData = userInfo
        doAsync {
            mIUserEditDataSource.submitUserInfo(userInfo, object : BaseDataSource.LoadSourceCallback<String> {
                override fun onDataLoaded(dataModel: String) {
                    uiThread {
                        if (mIUserEditView.isActive())
                            mIUserEditView.showUserInfo(mUserData!!)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIUserEditView.isActive())
                            mIUserEditView.showEditFailed()
                    }
                }
            })
        }
    }

    override fun editPicture(picture: Bitmap) {
        if (!NetworkHelper.isNetworkAvailable(mIUserEditView.getViewContext())) {
            mIUserEditView.showNoNetwork()
            return
        }
        doAsync {
            mIUserEditDataSource.submitUserPicture(picture, object : BaseDataSource.LoadSourceCallback<String> {
                override fun onDataLoaded(dataModel: String) {
                    uiThread {
                        if (mIUserEditView.isActive())
                            mIUserEditView.showEditPictureSuccess(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIUserEditView.isActive())
                            mIUserEditView.showEditFailed()
                    }
                }
            })
        }
    }
}