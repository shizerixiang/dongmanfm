package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.ArticleInfoContract
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 文章详情数据操作类
 * Created by shize on 2017/10/18.
 */
class ArticleInfoPresenter constructor(private val mIArticleInfoView: ArticleInfoContract.IArticleInfoView,
                                       private val mIArticleInfoDataSource: ArticleInfoContract.IArticleInfoDataSource)
    : ArticleInfoContract.IArticleInfoPresenter {

    private var mId: String = "" // 存储 id

    init {
        mIArticleInfoView.setPresenter(this)
    }

    override fun startLoad() {}

    override fun refreshPage() {
        mIArticleInfoDataSource.refreshData()
        getArticleInfo(mId)
    }

    override fun getArticleInfo(id: String) {
        if (!NetworkHelper.isNetworkAvailable(mIArticleInfoView.getViewContext())) {
            mIArticleInfoView.showNoNetwork()
            return
        }
        loadSource()
    }

    /**
     * 预加载数据
     */
    private fun loadSource() {
        doAsync {
            mIArticleInfoDataSource.getData(object : BaseDataSource.LoadSourceCallback<IModel.IArticleModel> {
                override fun onDataLoaded(dataModel: IModel.IArticleModel) {
                    uiThread {
                        if (mIArticleInfoView.isActive()) {
                            mIArticleInfoView.showArticleInfo(dataModel)
                        }
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIArticleInfoView.isActive()){
                            mIArticleInfoView.showRefreshFailed()
                        }
                    }
                }
            })
        }
    }
}