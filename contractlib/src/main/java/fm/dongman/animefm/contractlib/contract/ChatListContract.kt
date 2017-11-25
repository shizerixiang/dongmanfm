package fm.dongman.animefm.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel

/**
 * 私信列表契约接口
 * Created by shize on 2017/10/22.
 */
interface ChatListContract {
    /**
     * 私信列表界面接口
     */
    interface IChatListView : BaseView<IChatListPresenter> {
        /**
         * 显示私信列表
         * 由 [IChatListPresenter.startLoad] 回调
         * @param chats 私信集合
         */
        fun showChatList(chats: List<IModel.IMsgModel>)
    }

    /**
     * 私信列表数据操作接口
     * 在获取数据之前，需要先设置用户 id [IChatListDataSource.setUserId]
     */
    interface IChatListPresenter : BasePresenter

    /**
     * 私信列表数据源接口
     */
    interface IChatListDataSource : BaseDataSource<List<IModel.IMsgModel>> {
        /**
         * 设置用户id
         * @param id 用户 id
         */
        fun setUserId(id: String)
    }
}