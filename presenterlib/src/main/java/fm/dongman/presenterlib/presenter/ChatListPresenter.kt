package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.ChatListContract
import fm.dongman.animefm.contractlib.contract.MyContract
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 私信列表数据操作类
 * Created by shize on 2017/10/22.
 */
class ChatListPresenter constructor(private val mIChatListView: ChatListContract.IChatListView,
                                    private val mIChatListDataSource: ChatListContract.IChatListDataSource)
    : ChatListContract.IChatListPresenter {

    init {
        mIChatListView.setPresenter(this)
    }

    override fun startLoad() {
        if (NetworkHelper.isUnLogged(mIChatListView.getViewContext())) {
            mIChatListView.showNoNetwork()
            return
        }
        loadSource()
    }

    /**
     * 预加载数据
     */
    private fun loadSource() {
        mIChatListDataSource.setUserId(MyContract.USER_ID!!)
        doAsync {
            mIChatListDataSource.getData(object : BaseDataSource.LoadSourceCallback<List<IModel.IMsgModel>> {
                override fun onDataLoaded(dataModel: List<IModel.IMsgModel>) {
                    uiThread {
                        if (mIChatListView.isActive())
                            mIChatListView.showChatList(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIChatListView.isActive())
                            mIChatListView.showRefreshFailed()
                    }
                }
            })
        }
    }

    override fun refreshPage() {
        mIChatListDataSource.refreshData()
        startLoad()
    }
}