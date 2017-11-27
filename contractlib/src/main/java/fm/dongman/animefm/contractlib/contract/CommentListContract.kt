package fm.dongman.animefm.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel

/**
 * 评论列表契约接口
 * Created by shize on 2017/10/18.
 */
interface CommentListContract {
    /**
     * 评论列表界面接口
     */
    interface ICommentListView : BaseView<ICommentListPresenter> {
        /**
         * 显示评论列表
         * 由 [ICommentListPresenter.getComments] 回调
         * @param comments 评论集合
         */
        fun showCommentList(comments: List<IModel.ICommentModel>)

        /**
         * 显示更多评论
         * 由 [ICommentListPresenter.getComments] 回调
         * @param comments 更多评论
         */
        fun showMoreComments(comments: List<IModel.ICommentModel>)

        /**
         * 显示支持成功
         * 由 [ICommentListPresenter.supportComment] 回调
         */
        fun showSupportSucceed()

        /**
         * 显示支持失败
         * 由 [ICommentListPresenter.supportComment] 回调
         */
        fun showSupportFailed()

        /**
         * 显示删除状态
         * 由 [ICommentListPresenter.deleteComment] 回调
         * @param state 是否删除成功
         */
        fun showDeleteCommentState(state: Boolean)
    }

    /**
     * 评论列表数据操作接口
     */
    interface ICommentListPresenter : BasePresenter {
        /**
         * 获取评论列表
         * 通过 [ICommentListDataSource.setCommentListInfo] 再异步调用 [ICommentListDataSource.getData] 获取数据
         * 并在 UI 线程中调用 [ICommentListView.showCommentList] 或 [ICommentListView.showMoreComments] 显示结果
         * @param page 页数（从 1 开始，每页 10 个数据）
         * @param type 评论类型（长评 or 短评）[TYPE_SHORT] or [TYPE_LONG] 可为空，默认短评
         * @param ascriptionType 归属类型 （动漫 or 文章 or more） 具体引用请参考数据层实现 [ICommentListDataSource] 接口的类
         * @param ascriptionId 归属 id （动漫 id or 文章 id or more id） 具体引用请参考数据层实现 [ICommentListDataSource] 接口的类，可为空，为空时显示自己的评论
         */
        fun getComments(page: Int, type: String?, ascriptionType: String, ascriptionId: String?)

        /**
         * 支持评论
         * 通过异步调用 [ICommentListDataSource.supportComment] 获取数据
         * 并在 UI 线程中调用 [ICommentListView.showSupportSucceed] 或 [ICommentListView.showSupportFailed] 显示结果
         * @param commentId 评论 id
         */
        fun supportComment(commentId: String)

        /**
         * 删除评论
         * 通过异步调用 [ICommentListDataSource.deleteComment] 获取数据
         * 并在 UI 线程中调用 [ICommentListView.showDeleteCommentState] 显示结果
         * @param commentId 评论 id
         */
        fun deleteComment(commentId: String)

        companion object {
            // 评论类型
            val TYPE_SHORT: String = "短评"
            val TYPE_LONG: String = "长评"
        }
    }

    /**
     * 评论列表数据源接口
     */
    interface ICommentListDataSource : BaseDataSource<List<IModel.ICommentModel>> {
        /**
         * 获取评论列表
         * @param page 页数 （从 1 开始，每次加载 10 个数据）
         * @param type 评论类型（长评 or 短评）
         * @param ascriptionType 归属类型 （动漫 or 文章 or more）
         * @param ascriptionId 归属 id （动漫 id or 文章 id or more id）
         */
        fun setCommentListInfo(page: Int, type: String?, ascriptionType: String, ascriptionId: String?)

        /**
         * 支持评论
         * @param commentId 评论 id
         * @param userId 用户 id
         * @param callback 支持操作回调
         */
        fun supportComment(commentId: String, userId: String, callback: BaseDataSource.LoadSourceCallback<String?>)

        /**
         * 删除自己的评论
         * @param commentId 评论 id
         * @param callback 回调
         */
        fun deleteComment(commentId: String, callback: BaseDataSource.LoadSourceCallback<String?>)
    }
}