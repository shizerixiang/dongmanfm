package fm.dongman.animefm.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel

/**
 * 用户列表契约接口
 * Created by shize on 2017/10/19.
 */
interface UserListContract {
    /**
     * 用户列表界面接口
     */
    interface IUserListView : BaseView<IUserListPresenter> {
        /**
         * 显示用户列表
         * 由 [IUserListPresenter.setKey] 回调
         * @param users 用户集合
         */
        fun showUserList(users: List<IModel.IUserModel>)

        /**
         * 显示更多用户列表
         * 由 [IUserListPresenter.setKey] 回调
         * @param users 用户集合
         */
        fun showMoreUserList(users:List<IModel.IUserModel>)
    }

    /**
     * 用户列表数据操作接口
     */
    interface IUserListPresenter : BasePresenter {
        /**
         * 设置关键字
         * 通过异步调用 [IUserListDataSource.searchUsers]、[IUserListDataSource.followUsers]、[IUserListDataSource.fanUsers] 其中之一获取数据
         * 之后 UI 线程调用 [IUserListView.showUserList] 显示数据
         * @param type 类型 [TYPE_SEARCH] 、 [TYPE_FOLLOW] 、 [TYPE_FAN] 之一
         * @param key 关键字 or id
         */
        fun setKey(page: Int, type: String, key: String)

        companion object {
            val TYPE_SEARCH: String = "search" // 搜索
            val TYPE_FOLLOW: String = "follow" // 关注
            val TYPE_FAN: String = "fan" // 粉丝
        }
    }

    /**
     * 用户列表数据源接口
     */
    interface IUserListDataSource : BaseDataSource<Void> {
        /**
         * 通过设置关键字，搜索用户
         * @param page 页数
         * @param key 关键字
         * @param callback 获取数据回调
         */
        fun searchUsers(page: Int, key: String, callback: BaseDataSource.LoadSourceCallback<List<IModel.IUserModel>>)

        /**
         * 通过用户 id ，获取其关注的人
         * @param page 页数
         * @param id 用户 id
         * @param callback 获取数据回调
         */
        fun followUsers(page: Int, id: String, callback: BaseDataSource.LoadSourceCallback<List<IModel.IUserModel>>)

        /**
         * 通过用户 id，获取其粉丝的人
         * @param page 页数
         * @param id 用户 id
         * @param callback 获取数据回调
         */
        fun fanUsers(page: Int, id: String, callback: BaseDataSource.LoadSourceCallback<List<IModel.IUserModel>>)
    }
}