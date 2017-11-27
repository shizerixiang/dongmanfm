package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.contract.FSheetContract
import fm.dongman.animefm.contractlib.model.FSheetDataModel
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 漫圈数据操作
 * Created by shize on 2017/10/15.
 */
class FSheetPresenter constructor(private val mFSheetView: FSheetContract.IFSheetView,
                                  private val mFSheetSource: FSheetContract.IFSheetDataSource)
    : FSheetContract.IFSheetPresenter {

    init {
        mFSheetView.setPresenter(this)
    }

    override fun startLoad() {
        // 检测网络
        if (!NetworkHelper.isNetworkAvailable(mFSheetView.getViewContext())) {
            // 离线加载
            loadOffLineSource()
            return
        }
        // 正常加载
        loadSource()
    }

    override fun refreshPage() {
        mFSheetSource.refreshData()
        startLoad()
    }

    /**
     * 加载数据
     */
    private fun loadSource() {
        doAsync {
            Thread.sleep(HomePresenter.SLEEP_TIME)
            mFSheetSource.getData(object : BaseDataSource.LoadSourceCallback<FSheetDataModel> {
                override fun onDataLoaded(dataModel: FSheetDataModel) {
                    uiThread {
                        if (mFSheetView.isActive())
                            showSource(dataModel)
                    }
                    // 保存数据
                    mFSheetSource.saveData(dataModel)
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mFSheetView.isActive())
                            mFSheetView.showRefreshFailed()
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
            mFSheetSource.getData(object : BaseDataSource.LoadSourceCallback<FSheetDataModel> {
                override fun onDataLoaded(dataModel: FSheetDataModel) {
                    uiThread {
                        if (mFSheetView.isActive()) {
                            showSource(dataModel)
                            mFSheetView.showRefreshFailed()
                        }
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mFSheetView.isActive())
                            mFSheetView.showNoNetwork()
                    }
                }
            })
        }
    }

    /**
     * 显示数据
     */
    private fun showSource(dataModel: FSheetDataModel) {
        mFSheetView.showFSheetLabels(dataModel.mLabels)
        mFSheetView.showFSheetSpecials(dataModel.specials)
        mFSheetView.showFSheetMasters(dataModel.masters)
        mFSheetView.showFSheets(dataModel.sheets)
    }
}