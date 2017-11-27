package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.CommentListContract
import fm.dongman.animefm.contractlib.contract.CommentListContract.ICommentListPresenter.Companion.TYPE_SHORT
import fm.dongman.animefm.contractlib.contract.MyContract
import fm.dongman.modellib.source.CommentListRepository
import fm.dongman.presenterlib.util.NetworkHelper
import fm.dongman.presenterlib.util.NetworkHelper.isUnLogged
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 评论列表数据操作类
 * Created by shize on 2017/10/18.
 */
class CommentListPresenter constructor(private val mICommentListView: CommentListContract.ICommentListView,
                                       private val mICommentListDataSource: CommentListContract.ICommentListDataSource)
    : CommentListContract.ICommentListPresenter {

    private var mPage: Int = 0 // 页数
    private var mType: String? = TYPE_SHORT // 评论类型，默认短评
    private var mAscriptionType: String = CommentListRepository.SUBJECT // 归属对象类型
    private var mAscriptionId: String? = null // 归属对象 id

    init {
        mICommentListView.setPresenter(this)
    }

    override fun startLoad() {}

    override fun refreshPage() {
        mICommentListDataSource.refreshData()
        mPage = 1
        getComments(mPage, mType, mAscriptionType, mAscriptionId)
    }

    override fun getComments(page: Int, type: String?, ascriptionType: String, ascriptionId: String?) {
        mPage = page
        mType = type
        mAscriptionType = ascriptionType
        mAscriptionId = ascriptionId
        // 检测网络
        if (!NetworkHelper.isNetworkAvailable(mICommentListView.getViewContext())) {
            mICommentListView.showNoNetwork()
            return
        }
        loadSource()
    }

    override fun supportComment(commentId: String) {
        if (isUnLogged(mICommentListView.getViewContext())) {
            mICommentListView.showSupportFailed()
            return
        }
        support(commentId)
    }

    override fun deleteComment(commentId: String) {
        if (isUnLogged(mICommentListView.getViewContext())) {
            mICommentListView.showDeleteCommentState(false)
            return
        }
        delete(commentId)
    }

    /**
     * 删除我的评论
     */
    private fun delete(commentId: String) {
        doAsync {
            mICommentListDataSource.deleteComment(commentId, object : BaseDataSource.LoadSourceCallback<String?> {
                override fun onDataLoadFiled() {
                    uiThread {
                        if (mICommentListView.isActive())
                            mICommentListView.showDeleteCommentState(false)
                    }
                }

                override fun onDataLoaded(dataModel: String?) {
                    uiThread {
                        if (mICommentListView.isActive())
                            mICommentListView.showDeleteCommentState(true)
                    }
                }
            })
        }
    }

    /**
     * 加载数据
     */
    private fun loadSource() {
        mICommentListDataSource.setCommentListInfo(mPage, mType, mAscriptionType, mAscriptionId)
        doAsync {
            mICommentListDataSource.getData(object : BaseDataSource.LoadSourceCallback<List<IModel.ICommentModel>> {
                override fun onDataLoaded(dataModel: List<IModel.ICommentModel>) {
                    uiThread {
                        if (mICommentListView.isActive())
                            showSource(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mICommentListView.isActive()) {
                            mICommentListView.showNoNetwork()
                            mICommentListView.showRefreshFailed()
                        }
                    }
                }

            })
        }
    }

    private fun showSource(dataModel: List<IModel.ICommentModel>) {
        if (mPage == 1) {
            mICommentListView.showCommentList(dataModel)
            return
        }
        // 用户的评论没有页数
        if (dataModel.isEmpty()) {
            return
        }
        mICommentListView.showMoreComments(dataModel)
    }

    /**
     * 支持
     */
    private fun support(commentId: String) {
        doAsync {
            mICommentListDataSource.supportComment(commentId, MyContract.USER_ID!!, object : BaseDataSource.LoadSourceCallback<String?> {
                override fun onDataLoaded(dataModel: String?) {
                    uiThread {
                        if (mICommentListView.isActive())
                            mICommentListView.showSupportSucceed()
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mICommentListView.isActive())
                            mICommentListView.showSupportFailed()
                    }
                }
            })
        }
    }
}