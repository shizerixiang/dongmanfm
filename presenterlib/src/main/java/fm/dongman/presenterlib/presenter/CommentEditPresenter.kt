package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.CommentEditContract
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 编辑评论数据操作类
 * Created by shize on 2017/10/18.
 */
class CommentEditPresenter constructor(private val mICommentEditView: CommentEditContract.ICommentEditView,
                                       private val mICommentEditDataSource: CommentEditContract.ICommentEditDataSource)
    : CommentEditContract.ICommentEditPresenter {

    init {
        mICommentEditView.setPresenter(this)
    }

    override fun startLoad() {}

    override fun refreshPage() {}

    override fun submitComment(comment: IModel.ICommentModel) {
        if (!NetworkHelper.isNetworkAvailable(mICommentEditView.getViewContext())) {
            mICommentEditView.showSubmitFailed()
            return
        }
        submit(comment)
    }

    /**
     * 提交评论
     */
    private fun submit(comment: IModel.ICommentModel) {
        doAsync {
            mICommentEditDataSource.submitComment(comment, object : BaseDataSource.LoadSourceCallback<String?> {
                override fun onDataLoaded(dataModel: String?) {
                    uiThread {
                        if (mICommentEditView.isActive())
                            mICommentEditView.showSubmitSucceed()
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mICommentEditView.isActive())
                            mICommentEditView.showSubmitFailed()
                    }
                }
            })
        }
    }
}