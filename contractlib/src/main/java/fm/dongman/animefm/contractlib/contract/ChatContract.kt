package fm.dongman.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel

/**
 * 私聊契约接口
 * Created by shize on 2017/10/19.
 */
interface ChatContract {
    /**
     * 私聊界面接口
     */
    interface IChatView : BaseView<IChatPresenter> {
        /**
         * 显示历史聊天记录
         * 由 [IChatPresenter.startLoad] 回调
         * @param chats 聊天记录
         */
        fun showHistoryChat(chats: List<IModel.IMsgModel>)

        /**
         * 显示发送的聊天
         * 由 [IChatPresenter.sendChat] 回调
         * @param chat 发送的消息
         */
        fun showSendChatSucceed(chat: IModel.IMsgModel)

        /**
         * 显示发送失败
         * 由 [IChatPresenter.sendChat] 回调
         */
        fun showSendChatFailed()
    }

    /**
     * 私聊数据操作接口
     * 在获取历史记录之前，需要先设置我方和对方的 id [IChatDataSource.setUserId]
     */
    interface IChatPresenter : BasePresenter {
        /**
         * 发送私聊
         * 异步调用 [IChatDataSource.sendChat] 获取数据
         * UI 线程调用 [IChatView.showSendChatSucceed] 或 [IChatView.showSendChatFailed] 显示数据
         * @param chat 私聊内容
         */
        fun sendChat(chat: IModel.IMsgModel)
    }

    /**
     * 私聊数据源接口
     */
    interface IChatDataSource : BaseDataSource<List<IModel.IMsgModel>> {
        /**
         * 设置用户 id
         * @param myId 我的 id
         * @param hisId 对方 id
         */
        fun setUserId(myId: String, hisId: String)

        /**
         * 发送私聊
         * @param userId 用户 id
         * @param chat 聊天信息
         * @param callback 发送状态回调
         */
        fun sendChat(userId: String, chat: IModel.IMsgModel, callback: BaseDataSource.LoadSourceCallback<IModel.IMsgModel>)
    }
}