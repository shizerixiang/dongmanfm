package fm.dongman.presenterlib.presenter

import android.text.Editable
import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.ChatContract
import fm.dongman.animefm.contractlib.contract.MyContract
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 私信数据操作类
 * Created by shize on 2017/10/20.
 */
class ChatPresenter constructor(private val mIChatView: ChatContract.IChatView,
                                private val mIChatDataSource: ChatContract.IChatDataSource)
    : ChatContract.IChatPresenter {

    init {
        mIChatView.setPresenter(this)
    }

    override fun startLoad() {
        if (NetworkHelper.isUnLogged(mIChatView.getViewContext())) {
            mIChatView.showNoNetwork()
            return
        }
        loadSource()
    }

    override fun refreshPage() {
        mIChatDataSource.refreshData()
        startLoad()
    }

    override fun sendChat(chat: IModel.IMsgModel) {
        if (NetworkHelper.isUnLogged(mIChatView.getViewContext())) {
            mIChatView.showSendChatFailed()
            return
        }
        send(chat)
    }

    /**
     * 发送消息
     */
    private fun send(chat: IModel.IMsgModel) {
        doAsync {
            mIChatDataSource.sendChat(MyContract.USER_ID!!, chat, object : BaseDataSource.LoadSourceCallback<IModel.IMsgModel> {
                override fun onDataLoaded(dataModel: IModel.IMsgModel) {
                    uiThread {
                        if (mIChatView.isActive())
                            mIChatView.showSendChatSucceed(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIChatView.isActive())
                            mIChatView.showSendChatFailed()
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
            mIChatDataSource.getData(object : BaseDataSource.LoadSourceCallback<List<IModel.IMsgModel>> {
                override fun onDataLoaded(dataModel: List<IModel.IMsgModel>) {
                    uiThread {
                        if (mIChatView.isActive())
                            mIChatView.showHistoryChat(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIChatView.isActive())
                            mIChatView.showRefreshFailed()
                    }
                }
            })
        }
    }
}