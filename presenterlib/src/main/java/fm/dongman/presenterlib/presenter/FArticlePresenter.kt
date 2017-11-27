package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.FArticleContract
import fm.dongman.animefm.contractlib.model.FArticleDataModel
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 漫圈数据操作
 * Created by shize on 2017/10/15.
 */
class FArticlePresenter constructor(private val mFArticleView: FArticleContract.IFArticleView,
                                    private val mFArticleSource: FArticleContract.IFArticleDataSource)
    : FArticleContract.IFArticlePresenter {

    var bannerLimit: Int = 5 // 限制 Banner 数量

    init {
        mFArticleView.setPresenter(this)
    }

    override fun startLoad() {
        // 检测网络
        if (!NetworkHelper.isNetworkAvailable(mFArticleView.getViewContext())) {
            // 离线加载
            loadOffLineSource()
            return
        }
        // 正常加载
        loadSource()
    }

    override fun refreshPage() {
        mFArticleSource.refreshData()
        startLoad()
    }

    /**
     * 加载数据
     */
    private fun loadSource() {
        doAsync {
            Thread.sleep(HomePresenter.SLEEP_TIME)
            mFArticleSource.getData(object : BaseDataSource.LoadSourceCallback<FArticleDataModel> {
                override fun onDataLoaded(dataModel: FArticleDataModel) {
                    uiThread {
                        if (mFArticleView.isActive())
                            showSource(dataModel)
                    }
                    // 保存数据
                    mFArticleSource.saveData(dataModel)
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mFArticleView.isActive())
                            mFArticleView.showRefreshFailed()
                    }
                    // 离线加载数据
                    loadOffLineSource()
                }
            })
        }
    }

    /**
     * 离线加载数据
     */
    private fun loadOffLineSource() {
        doAsync {
            Thread.sleep(HomePresenter.SLEEP_TIME)
            mFArticleSource.getData(object : BaseDataSource.LoadSourceCallback<FArticleDataModel> {
                override fun onDataLoaded(dataModel: FArticleDataModel) {
                    uiThread {
                        if (mFArticleView.isActive()) {
                            showSource(dataModel)
                            mFArticleView.showRefreshFailed()
                        }
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mFArticleView.isActive())
                            mFArticleView.showNoNetwork()
                    }
                }
            })
        }
    }

    /**
     * 显示数据
     */
    private fun showSource(dataModel: FArticleDataModel) {
        mFArticleView.showFArticleDynamic(limitBanner(dataModel.dynamicArticles))
        mFArticleView.showFArticleHot(dataModel.hotArticles)
        mFArticleView.showFArticleNew(dataModel.newArticles)
    }

    /**
     * 限制 Banner 数量
     */
    private fun limitBanner(dataModel: List<IModel.IArticleModel>): List<IModel.IArticleModel> {
        if (dataModel.size > bannerLimit) {
            return dataModel.subList(0, bannerLimit)
        }
        return dataModel
    }
}