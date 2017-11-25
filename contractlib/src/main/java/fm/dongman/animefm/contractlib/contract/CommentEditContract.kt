package fm.dongman.animefm.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel

/**
 * 编辑评论契约接口
 * Created by shize on 2017/10/17.
 */
interface CommentEditContract {
    /**
     * 编辑评论界面接口
     */
    interface ICommentEditView : BaseView<ICommentEditPresenter> {
        /**
         * 显示评论提交结果
         * 由 [ICommentEditPresenter.submitComment] 回调
         */
        fun showSubmitSucceed()

        /**
         * 显示评论提交失败
         * 由 [ICommentEditPresenter.submitComment] 回调
         */
        fun showSubmitFailed()
    }

    /**
     * 编辑评论数据操作接口
     */
    interface ICommentEditPresenter:BasePresenter{
        /**
         * 提交评论
         * 通过异步调用 [ICommentEditDataSource.submitComment] 获取数据
         * 并在 UI 线程中调用 [ICommentEditView.showSubmitSucceed] 或 [ICommentEditView.showSubmitFailed] 显示结果
         * @param comment 评论信息
         */
        fun submitComment(comment: IModel.ICommentModel)
    }

    /**
     * 编辑评论数据源接口
     */
    interface ICommentEditDataSource:BaseDataSource<Void>{
        /**
         * 提交评论
         * @param comment 评论
         * @param callback 提交评论回调
         */
        fun submitComment(comment: IModel.ICommentModel, callback: BaseDataSource.LoadSourceCallback<Void>)
    }
}