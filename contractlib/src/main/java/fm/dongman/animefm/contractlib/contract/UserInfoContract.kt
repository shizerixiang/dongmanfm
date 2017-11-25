package fm.dongman.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel

/**
 * 用户信息契约接口
 * Created by shize on 2017/10/17.
 */
interface UserInfoContract {
    /**
     * 用户信息界面接口
     */
    interface IUserInfoView : BaseView<IUserInfoPresenter> {
        /**
         * 显示用户信息
         * 由 [IUserInfoPresenter.getUserInfo] 回调
         * @param userInfo 用户信息
         */
        fun showUserInfo(userInfo: IModel.IUserModel)

        /**
         * 显示关注用户成功
         * 由 [IUserInfoPresenter.followUser] 回调
         */
        fun showFollowSucceed()

        /**
         * 显示关注用户失败
         * 由 [IUserInfoPresenter.followUser] 回调
         */
        fun showFollowFailed()
    }

    /**
     * 用户信息操作接口
     */
    interface IUserInfoPresenter : BasePresenter {
        /**
         * 获取用户信息
         * 通过 [IUserInfoDataSource.setUserId] 设置 id 后，再异步调用 [IUserInfoDataSource.getData] 获取数据
         * 并在 UI 线程中调用 [IUserInfoView.showUserInfo] 显示数据
         * @param id 用户 id
         */
        fun getUserInfo(id: String)

        /**
         * 关注用户
         * 通过异步调用 [IUserInfoDataSource.followUser] 获取数据
         * 并在 UI 线程中调用 [IUserInfoView.showFollowSucceed] 或 [IUserInfoView.showFollowFailed] 显示数据
         * @param id 关注对象用户 id
         * @param follow 关注/取消关注 [IS_FOLLOWED] or [NOT_FOLLOWED]
         */
        fun followUser(id: String, follow: Int)

        companion object {
            val IS_FOLLOWED = 1 // 关注
            val NOT_FOLLOWED = 0 // 取消关注
        }
    }

    /**
     * 用户信息数据源接口
     */
    interface IUserInfoDataSource : BaseDataSource<IModel.IUserModel> {
        /**
         * 设置用户 id
         * 只有设置了用户 id 才能获取数据
         * @param id 用户 id
         */
        fun setUserId(id: String)

        /**
         * 关注用户
         * @param follow 关注/取消关注
         * @param followedId 被关注用户的 id
         * @param callback 关注回调
         */
        fun followUser(follow: Int, followedId: String, callback: BaseDataSource.LoadSourceCallback<Void>)
    }
}