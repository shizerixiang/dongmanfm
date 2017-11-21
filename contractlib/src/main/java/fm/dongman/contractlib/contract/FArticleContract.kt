package fm.dongman.contractlib.contract

import fm.dongman.contractlib.BasePresenter
import fm.dongman.contractlib.BaseDataSource
import fm.dongman.contractlib.BaseView
import fm.dongman.contractlib.IModel
import fm.dongman.contractlib.model.FArticleDataModel

/**
 * 发现->漫圈的契约接口
 * Created by shize on 2017/9/25.
 */
interface FArticleContract {
    /**
     * 漫圈界面接口，启动页面调用 [IFArticlePresenter.startLoad] 加载数据
     */
    interface IFArticleView : BaseView<IFArticlePresenter> {
        /**
         * 显示漫圈动态
         * @param dynamicArticles 动态文章
         */
        fun showFArticleDynamic(dynamicArticles: List<IModel.IArticleModel>)

        /**
         * 显示本周最火
         * @param hotArticles 最火文章
         */
        fun showFArticleHot(hotArticles: List<IModel.IArticleModel>)

        /**
         * 显示最新文章
         * @param newArticles 最新文章
         */
        fun showFArticleNew(newArticles: List<IModel.IArticleModel>)
    }

    /**
     * 漫圈操作接口，在 [startLoad] 中调用各种 load 方法
     */
    interface IFArticlePresenter : BasePresenter

    /**
     * 漫圈数据接口
     */
    interface IFArticleDataSource : BaseDataSource<FArticleDataModel>

}
