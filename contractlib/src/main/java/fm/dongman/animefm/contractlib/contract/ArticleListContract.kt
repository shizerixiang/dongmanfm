package fm.dongman.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel

/**
 * 文章列表契约接口
 * Created by shize on 2017/10/18.
 */
interface ArticleListContract {
    /**
     * 文章列表界面接口
     */
    interface IArticleListView : BaseView<IArticleListPresenter> {
        /**
         * 显示文章列表
         * 由 [IArticleListPresenter.getArticles] 回调
         * @param articles 文章集合
         */
        fun showArticles(articles: List<IModel.IArticleModel>)

        /**
         * 显示更多文章
         * 由 [IArticleListPresenter.getArticles] 回调
         * @param articles 更多文章
         */
        fun showMoreArticles(articles: List<IModel.IArticleModel>)
    }

    /**
     * 文章列表数据操作接口
     */
    interface IArticleListPresenter : BasePresenter {
        /**
         * 获取文章列表
         * 通过 [IArticleListDataSource.setArticleListInfo] 再异步调用 [IArticleListDataSource.getData] 获取数据
         * 并在 UI 线程中调用 [IArticleListView.showArticles] 或 [IArticleListView.showMoreArticles] 显示结果
         * @param page 页数（从 1 开始，每页 10 个数据）
         * @param type 文章列表类型 [TYPE_COSPLAY] or [TYPE_CARD] or [TYPE_INVENTORY] or [TYPE_COMIC] or [TYPE_INFO] or [TYPE_CREATE] or [TYPE_SEARCH]
         * @param key 若是特殊类型则需要 key（如：搜索相关文章，需要 key；用户发表的文章则需要 id），不是则为 null
         */
        fun getArticles(page: Int, type: String, key: String?)

        companion object {
            val TYPE_COSPLAY: String = "cosplay"
            val TYPE_CARD: String = "漫贴"
            val TYPE_INVENTORY: String = "盘点"
            val TYPE_COMIC: String = "漫画"
            val TYPE_INFO: String = "情报"
            val TYPE_CREATE: String = "用户发表的文章"
            val TYPE_SEARCH: String = "search" // 搜索
        }
    }

    /**
     * 文章列表数据源接口
     */
    interface IArticleListDataSource : BaseDataSource<List<IModel.IArticleModel>> {
        /**
         * 获取文章列表
         * @param page 页数 （从 1 开始，每次加载 10 个数据）
         * @param type 文章列表类型
         * @param id 若是特殊类型则需要 id（如：动漫相关文章，需要动漫 id），不是则为 null
         */
        fun setArticleListInfo(page: Int, type: String, id: String?)

        /**
         * 搜索文章
         * @param key 关键字
         * @param callback 获取数据回调
         */
        fun searchArticles(key: String, callback: BaseDataSource.LoadSourceCallback<List<IModel.IArticleModel>>)
    }
}