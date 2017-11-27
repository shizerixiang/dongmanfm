package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.SheetListContract
import fm.dongman.animefm.contractlib.model.SearchSheetResultModel
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 漫单列表数据操作类
 * 可以设置列表排序方式 order
 * Created by shize on 2017/10/19.
 */
class SheetListPresenter constructor(private val mISheetListView: SheetListContract.ISheetListView,
                                     private val mISheetListDataSource: SheetListContract.ISheetListDataSource)
    : SheetListContract.ISheetListPresenter {

    private var mPage: Int = 0 // 页数，起始页为 1
    private var mType: String = "" // 类型
    private var mId: String? = "" // 该类型的 id

    var order: Int = 0 // 排序，默认值为 0

    init {
        mISheetListView.setPresenter(this)
    }

    override fun startLoad() {}

    override fun refreshPage() {
        mISheetListDataSource.refreshData()
        mPage = 1
        getSheets(mPage, mType, mId)
    }

    override fun getSheets(page: Int, type: String, id: String?) {
        mPage = page
        mType = type
        mId = id
        if (!NetworkHelper.isNetworkAvailable(mISheetListView.getViewContext())) {
            mISheetListView.showNoNetwork()
            return
        }
        loadSource()
    }

    override fun deleteSheet(sheetId: String) {
        if (NetworkHelper.isUnLogged(mISheetListView.getViewContext())) {
            mISheetListView.showDeleteState(false)
            return
        }
        delete(sheetId)
    }

    /**
     * 删除我创建的漫单
     */
    private fun delete(sheetId: String) {
        doAsync {
            mISheetListDataSource.deleteSheet(sheetId, object : BaseDataSource.LoadSourceCallback<String?> {
                override fun onDataLoadFiled() {
                    uiThread {
                        if (mISheetListView.isActive())
                            mISheetListView.showDeleteState(false)
                    }
                }

                override fun onDataLoaded(dataModel: String?) {
                    uiThread {
                        if (mISheetListView.isActive())
                            mISheetListView.showDeleteState(true)
                    }
                }
            })
        }
    }

    /**
     * 加载数据
     */
    private fun loadSource() {
        mISheetListDataSource.setSheetListInfo(order, mPage, mType, mId)
        // 判断是否为搜索
        if (SheetListContract.ISheetListPresenter.TYPE_SEARCH == mType) {
            searchSheet()
            return
        }
        loadSheet()
    }

    /**
     * 加载数据
     */
    private fun loadSheet() {
        doAsync {
            mISheetListDataSource.getData(object : BaseDataSource.LoadSourceCallback<List<IModel.ISheetModel>> {
                override fun onDataLoaded(dataModel: List<IModel.ISheetModel>) {
                    uiThread {
                        if (mISheetListView.isActive())
                            showSheets(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mISheetListView.isActive())
                            mISheetListView.showRefreshFailed()
                    }
                }
            })
        }
    }

    /**
     * 搜索数据
     */
    private fun searchSheet() {
        doAsync {
            mISheetListDataSource.searchSheets(mId!!, object : BaseDataSource.LoadSourceCallback<SearchSheetResultModel> {
                override fun onDataLoaded(dataModel: SearchSheetResultModel) {
                    uiThread {
                        if (mISheetListView.isActive())
                            showSearchSheets(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mISheetListView.isActive())
                            mISheetListView.showRefreshFailed()
                    }
                }
            })
        }
    }

    /**
     * 显示各类型的漫单/专题结果
     */
    private fun showSheets(dataModel: List<IModel.ISheetModel>) {
        if (mPage == 1) {
            mISheetListView.showSheets(dataModel)
            return
        }
        // 用户关注和收藏的漫单/专题没有页数
        if (dataModel.isEmpty()) {
            return
        }
        mISheetListView.showMoreSheets(dataModel)
    }

    /**
     * 显示搜索结果的漫单/专题列表
     */
    private fun showSearchSheets(dataModel: SearchSheetResultModel) {
        if (mPage == 1) {
            mISheetListView.showSheets(dataModel.mSheetResultList)
            mISheetListView.showSearchTopicResult(dataModel.mTopicResultList)
            return
        }
        mISheetListView.showMoreSheets(dataModel.mSheetResultList)
        mISheetListView.showMoreSearchTopicResult(dataModel.mTopicResultList)
    }
}