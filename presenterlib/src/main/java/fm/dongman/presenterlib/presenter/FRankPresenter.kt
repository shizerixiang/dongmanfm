package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.contract.FRankContract
import fm.dongman.animefm.contractlib.model.FRankDataModel
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 榜单数据操作
 * Created by shize on 2017/10/15.
 */
class FRankPresenter constructor(private val mFRankView: FRankContract.IFRankView,
                                 private val mFRankSource: FRankContract.IFRankDataSource)
    : FRankContract.IFRankPresenter {

    init {
        mFRankView.setPresenter(this)
    }

    override fun startLoad() {
        // 检测网络
        if (!NetworkHelper.isNetworkAvailable(mFRankView.getViewContext())) {
            // 离线加载
            loadOffLineSource()
            return
        }
        // 正常加载
        loadSource()
    }

    override fun refreshPage() {
        mFRankSource.refreshData()
        startLoad()
    }

    override fun getMoreRank() = mFRankView.showFRankMore(mFRankSource.getMoreRank())

    /**
     * 加载数据
     */
    private fun loadSource() {
        doAsync {
            Thread.sleep(HomePresenter.SLEEP_TIME)
            mFRankSource.getData(object : BaseDataSource.LoadSourceCallback<FRankDataModel> {
                override fun onDataLoaded(dataModel: FRankDataModel) {
                    uiThread {
                        if (mFRankView.isActive())
                            showSource(dataModel)
                    }
                    // 保存数据
                    mFRankSource.saveData(dataModel)
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mFRankView.isActive())
                            mFRankView.showRefreshFailed()
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
            mFRankSource.getData(object : BaseDataSource.LoadSourceCallback<FRankDataModel> {
                override fun onDataLoaded(dataModel: FRankDataModel) {
                    uiThread {
                        if (mFRankView.isActive()) {
                            showSource(dataModel)
                            mFRankView.showRefreshFailed()
                        }
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mFRankView.isActive())
                            mFRankView.showNoNetwork()
                    }
                }
            })
        }
    }

    /**
     * 显示数据
     */
    private fun showSource(dataModel: FRankDataModel) {
        mFRankView.showFRankHot(dataModel.weekHotComicRank)
        mFRankView.showFRankTop(dataModel.comicTopRank)
        mFRankView.showFRankDomestic(dataModel.domesticComicRank)
        mFRankView.showFRankHarem(dataModel.haremComicRank)
    }
}