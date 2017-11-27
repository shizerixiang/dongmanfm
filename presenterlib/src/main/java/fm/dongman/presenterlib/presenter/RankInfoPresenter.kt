package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.RankInfoContract
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 漫榜信息数据操作类
 * Created by shize on 2017/10/18.
 */
class RankInfoPresenter constructor(private val mIRankInfoView: RankInfoContract.IRankInfoView,
                                    private val mIRankInfoDataSource: RankInfoContract.IRankInfoDataSource)
    : RankInfoContract.IRankInfoPresenter {

    private var mPage: Int = 0 // 页数
    private var mType: String = "" // 榜单类型

    init {
        mIRankInfoView.setPresenter(this)
    }

    override fun startLoad() {}

    override fun refreshPage() {
        mIRankInfoDataSource.refreshData()
        mPage = 1
        getRankInfo(mPage, mType)
    }

    override fun getRankInfo(page: Int, type: String) {
        mPage = page
        mType = type
        if (!NetworkHelper.isNetworkAvailable(mIRankInfoView.getViewContext())) {
            mIRankInfoView.showNoNetwork()
            return
        }
        loadSource()
    }

    /**
     * 预加载数据
     */
    private fun loadSource() {
        mIRankInfoDataSource.setRankInfo(mPage, mType)
        doAsync {
            mIRankInfoDataSource.getData(object : BaseDataSource.LoadSourceCallback<List<IModel.IComicModel>> {
                override fun onDataLoaded(dataModel: List<IModel.IComicModel>) {
                    uiThread {
                        if (mIRankInfoView.isActive())
                            showSource(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIRankInfoView.isActive())
                            mIRankInfoView.showRefreshFailed()
                    }
                }
            })
        }
    }

    private fun showSource(dataModel: List<IModel.IComicModel>) {
        if (mPage > 1) {
            mIRankInfoView.showMoreRankList(dataModel)
            return
        }
        mIRankInfoView.showRankList(dataModel)
    }
}