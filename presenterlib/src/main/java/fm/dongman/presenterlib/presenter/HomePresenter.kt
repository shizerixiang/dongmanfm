package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.HomeContract
import fm.dongman.animefm.contractlib.model.HomeDataModel
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 主页数据操作
 * Created by shize on 9/28/17.
 */

class HomePresenter(private val mHomeView: HomeContract.IHomeView,
                    private val mHomeSource: HomeContract.IHomeDataSource)
    : HomeContract.IHomePresenter {

    var bannerLimit: Int = 5

    init {
        mHomeView.setPresenter(this)
    }

    override fun startLoad() {
        // 检测是否有网络
        if (!NetworkHelper.isNetworkAvailable(mHomeView.getViewContext())) {
            // 在没有网络下，加载本地数据
            loadOffLineSource()
            return
        }
        // 处于有网络的状态下，异步加载服务器数据
        loadSource()
    }

    override fun refreshPage() {
        // 刷新数据，并且加载数据
        mHomeSource.refreshData()
        startLoad()
    }

    /**
     * 加载数据
     */
    private fun loadSource() {
        doAsync {
            // 获取数据
            Thread.sleep(SLEEP_TIME)
            mHomeSource.getData(object : BaseDataSource.LoadSourceCallback<HomeDataModel> {
                override fun onDataLoaded(dataModel: HomeDataModel) {
                    uiThread {
                        if (mHomeView.isActive())
                            showSource(dataModel)
                    }
                    // 保存数据到数据库
                    mHomeSource.saveData(dataModel)
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        // 加载数据失败，并显示在主界面
                        if (mHomeView.isActive())
                            mHomeView.showRefreshFailed()
                    }
                    loadOffLineSource()
                }
            })
        }
    }

    /**
     * 离线加载本地数据
     */
    private fun loadOffLineSource() {
        // 开启离线模式
        mHomeSource.offLine()
        doAsync {
            // 获取数据
            Thread.sleep(SLEEP_TIME)
            mHomeSource.getData(object : BaseDataSource.LoadSourceCallback<HomeDataModel> {
                override fun onDataLoaded(dataModel: HomeDataModel) {
                    uiThread {
                        // 在 UI 中显示数据
                        if (mHomeView.isActive()) {
                            showSource(dataModel)
                            mHomeView.showRefreshFailed()
                        }
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        // 第一次加载数据，并且没有加载成功的状态下，显示没有数据
                        if (mHomeView.isActive())
                            mHomeView.showNoNetwork()
                    }
                }
            })
        }
    }

    /**
     * 显示数据
     */
    private fun showSource(dataModel: HomeDataModel) {
        // 在 UI 中显示数据
        mHomeView.showHomeBanners(limitBanner(dataModel.banners))
        mHomeView.showHomeFMRecommendList(dataModel.FMRecommends)
        mHomeView.showHotRankComicList(dataModel.rankComic)
        mHomeView.showHomeNewArticleList(dataModel.newArticles)
        mHomeView.showHomeNewCommentList(dataModel.newComments)
        mHomeView.showHomeSheetRecommendList(dataModel.sheets)
    }

    /**
     * 对 Banner 的数量进行限制
     */
    private fun limitBanner(dataModel: List<IModel.IBannerModel>): List<IModel.IBannerModel> {
        if (dataModel.size > bannerLimit) {
            return dataModel.subList(0, bannerLimit)
        }
        return dataModel
    }

    companion object{
        val SLEEP_TIME = 120L // 延迟时间
    }
}
