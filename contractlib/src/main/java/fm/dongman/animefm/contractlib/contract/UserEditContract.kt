package fm.dongman.animefm.contractlib.contract

import android.graphics.Bitmap
import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel

/**
 * 用户编辑资料契约接口
 * Created by shize on 2017/11/24.
 */
interface UserEditContract {
    /**
     * 用户编辑资料页面
     */
    interface IUserEditView : BaseView<IUserEditPresent> {
        /**
         * 显示用户信息
         * @param userInfo 用户信息
         */
        fun showUserInfo(userInfo: IModel.IUserModel)

        /**
         * 显示编辑成功
         */
        fun showEditSuccess()

        /**
         * 显示编辑失败
         * 由 [IUserEditPresent.editUserInfo]、[IUserEditPresent.editPicture] 回调
         */
        fun showEditFailed()
    }

    /**
     * 用户编辑资料数据操作
     * [startLoad] 负责获取用户基本信息，通过[IUserEditView.showUserInfo] 显示用户信息
     */
    interface IUserEditPresent : BasePresenter {
        /**
         * 编辑个人信息
         * 异步提交调用 [IUserEditDataSource.submitUserInfo]
         * 在主线程调用 [IUserEditView.showEditSuccess]、[IUserEditView.showEditFailed]
         * @param userInfo 个人资料
         */
        fun editUserInfo(userInfo: IModel.IUserModel)

        /**
         * 修改头像
         * 异步提交调用 [IUserEditDataSource.submitUserPicture]
         * 在主线程调用 [IUserEditView.showEditSuccess]、[IUserEditView.showEditFailed]
         * @param picture 头像图片
         */
        fun editPicture(picture:Bitmap)
    }

    /**
     * 用户编辑资料数据获取
     */
    interface IUserEditDataSource : BaseDataSource<IModel.IUserModel> {
        /**
         * 提交用户信息
         * @param userInfo 用户信息
         * @param callback 结果回调
         */
        fun submitUserInfo(userInfo: IModel.IUserModel, callback: BaseDataSource.LoadSourceCallback<String>)

        /**
         * 提交用户头像
         * @param picture 用户头像
         * @param callback 结果回调
         */
        fun submitUserPicture(picture: Bitmap, callback: BaseDataSource.LoadSourceCallback<String>)
    }
}