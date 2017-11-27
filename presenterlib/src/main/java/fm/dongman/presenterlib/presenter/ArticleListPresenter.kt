package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.ArticleListContract
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 文章列表数据操作类
 * Created by shize on 2017/10/18.
 */
class ArticleListPresenter (private val mIArticleListView: ArticleListContract.IArticleListView,
                            private val mIArticleListDataSource: ArticleListContract.IArticleListDataSource)
    : ArticleListContract.IArticleListPresenter {

    private var mPage: Int = 0 // 页数
    private var mType: String = "cosplay" // 类型
    private var mKey: String? = null // 搜索关键字

    init {
        mIArticleListView.setPresenter(this)
    }

    override fun startLoad() {}

    override fun refreshPage() {
        mIArticleListDataSource.refreshData()
        mPage = 1
        getArticles(mPage, mType, mKey)
    }

    override fun getArticles(page: Int, type: String, key: String?) {
        mPage = page
        mType = type
        mKey = key
        if (!NetworkHelper.isNetworkAvailable(mIArticleListView.getViewContext())) {
            mIArticleListView.showNoNetwork()
            return
        }
        loadSource()
    }

    /**
     * 预加载数据
     */
    private fun loadSource() {
        mIArticleListDataSource.setArticleListInfo(mPage, mType, mKey)
        // 判断是否是搜索文章
        if (ArticleListContract.IArticleListPresenter.TYPE_SEARCH == mType) {
            searchArticles()
            return
        }
        loadArticles()
    }

    /**
     * 搜索文章
     */
    private fun searchArticles() {
        if (mKey==null){
            mIArticleListView.showRefreshFailed()
            return
        }
        doAsync {
            mIArticleListDataSource.searchArticles(mKey!!, object : BaseDataSource.LoadSourceCallback<List<IModel.IArticleModel>> {
                override fun onDataLoaded(dataModel: List<IModel.IArticleModel>) {
                    uiThread {
                        if (mIArticleListView.isActive()) {
                            showArticleSource(dataModel)
                        }
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIArticleListView.isActive()) {
                            mIArticleListView.showRefreshFailed()
                        }
                    }
                }
            })
        }
    }

    /**
     * 加载文章
     */
    private fun loadArticles() {
        doAsync {
            mIArticleListDataSource.getData(object : BaseDataSource.LoadSourceCallback<List<IModel.IArticleModel>> {
                override fun onDataLoaded(dataModel: List<IModel.IArticleModel>) {
                    uiThread {
                        if (mIArticleListView.isActive()) {
                            showArticleSource(dataModel)
                        }
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIArticleListView.isActive()) {
                            mIArticleListView.showRefreshFailed()
                        }
                    }
                }
            })
        }
    }

    private fun showArticleSource(dataModel: List<IModel.IArticleModel>) {
        if (mPage == 1) {
            mIArticleListView.showArticles(dataModel)
            return
        }
        mIArticleListView.showMoreArticles(dataModel)
    }
}