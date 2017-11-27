package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.contract.SearchContract
import fm.dongman.animefm.contractlib.model.SearchDataModel
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 漫圈数据操作
 * Created by shize on 2017/10/15.
 */
class SearchPresenter constructor(private val mSearchView: SearchContract.ISearchView,
                                  private val mSearchSource: SearchContract.ISearchDataSource)
    : SearchContract.ISearchPresenter {

    init {
        mSearchView.setPresenter(this)
    }

    override fun startLoad() {
        // 检测网络
        if (!NetworkHelper.isNetworkAvailable(mSearchView.getViewContext())) {
            // 离线加载
            loadOffLineSource()
            return
        }
        // 正常加载
        loadSource()
    }

    override fun refreshPage() {
        mSearchSource.refreshData()
        startLoad()
    }

    override fun clearHistorySearch() {
        doAsync {
            mSearchSource.clearHistorySearch(object : BaseDataSource.LoadSourceCallback<String?> {
                override fun onDataLoadFiled() {
                    uiThread {
                        if (mSearchView.isActive())
                            mSearchView.showClearHistoryState(false)
                    }
                }

                override fun onDataLoaded(dataModel: String?) {
                    uiThread {
                        if (mSearchView.isActive())
                            mSearchView.showClearHistoryState(true)
                    }
                }
            })
        }
    }

    /**
     * 离线加载数据
     */
    private fun loadOffLineSource() {
        doAsync {
            mSearchSource.getData(object : BaseDataSource.LoadSourceCallback<SearchDataModel> {
                override fun onDataLoaded(dataModel: SearchDataModel) {
                    uiThread {
                        if (mSearchView.isActive()) {
                            showSource(dataModel)
                            mSearchView.showRefreshFailed()
                        }
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mSearchView.isActive())
                            mSearchView.showNoNetwork()
                    }
                }
            })
        }
    }

    /**
     * 加载数据
     */
    private fun loadSource() {
        doAsync {
            mSearchSource.getData(object : BaseDataSource.LoadSourceCallback<SearchDataModel> {
                override fun onDataLoaded(dataModel: SearchDataModel) {
                    uiThread {
                        if (mSearchView.isActive())
                            showSource(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mSearchView.isActive())
                            mSearchView.showRefreshFailed()
                    }
                    // 离线加载数据
                    loadOffLineSource()
                }
            })
        }
    }

    /**
     * 显示数据
     */
    private fun showSource(dataModel: SearchDataModel) {
        mSearchView.showHistorySearch(dataModel.historySearchList)
        mSearchView.showHotSearch(dataModel.hotSearchList)
    }
}