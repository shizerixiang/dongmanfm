package fm.dongman.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel

/**
 * 文章信息契约接口
 * Created by shize on 2017/10/18.
 */
interface ArticleInfoContract {
    /**
     * 文章信息界面接口
     */
    interface IArticleInfoView : BaseView<IArticleInfoPresenter> {
        /**
         * 显示文章
         * 由 [IArticleInfoPresenter.getArticleInfo] 回调
         * @param articleInfo 文章信息
         */
        fun showArticleInfo(articleInfo: IModel.IArticleModel)
    }

    /**
     * 文章信息数据操作接口
     */
    interface IArticleInfoPresenter : BasePresenter {
        /**
         * 获取文章信息
         * 通过调用 [IArticleInfoDataSource.setArticleId] 后 异步 [IArticleInfoDataSource.getData] 获取数据
         * 并在 UI 线程中调用 [IArticleInfoView.showArticleInfo] 显示数据
         * @param id 文章 id
         */
        fun getArticleInfo(id: String)
    }

    /**
     * 文章信息数据源接口
     */
    interface IArticleInfoDataSource : BaseDataSource<IModel.IArticleModel> {
        /**
         * 获取文章信息
         * @param id 文章 id
         */
        fun setArticleId(id: String)
    }
}