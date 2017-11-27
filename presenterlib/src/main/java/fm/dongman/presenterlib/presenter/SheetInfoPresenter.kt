package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.SheetInfoContract
import fm.dongman.animefm.contractlib.contract.SheetInfoContract.ISheetInfoPresenter.Companion.TYPE_MANDAN
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 漫单信息数据操作类
 * Created by shize on 2017/10/18.
 */
class SheetInfoPresenter constructor(private val mISheetInfoView: SheetInfoContract.ISheetInfoView,
                                     private val mISheetInfoDataSource: SheetInfoContract.ISheetInfoDataSource)
    : SheetInfoContract.ISheetInfoPresenter {

    private var mId: String = "" // 漫单 id
    private var mPage: Int = 1 // 页数
    private var mType: String = TYPE_MANDAN

    init {
        mISheetInfoView.setPresenter(this)
    }

    override fun startLoad() {}

    override fun refreshPage() {
        mISheetInfoDataSource.refreshData()
        getSheetInfo(mType, mId, mPage)
    }

    override fun getSheetInfo(type: String, id: String, page: Int) {
        mType = type
        mId = id
        mPage = page
        if (!NetworkHelper.isNetworkAvailable(mISheetInfoView.getViewContext())) {
            mISheetInfoView.showNoNetwork()
            return
        }
        loadSource()
    }

    override fun collectSheet(type: String, sheetId: String, follow: Int) {
        if (NetworkHelper.isUnLogged(mISheetInfoView.getViewContext())) {
            mISheetInfoView.showCollectFailed()
            return
        }
        collect(sheetId, follow)
    }

    override fun deleteComics(sheetId: String, comics: List<IModel.IComicModel>) {
        if (NetworkHelper.isUnLogged(mISheetInfoView.getViewContext())) {
            mISheetInfoView.showDeleteComicsState(false)
            return
        }
        deleteComic(sheetId, comics)
    }

    /**
     * 删除漫单中的动漫
     */
    private fun deleteComic(sheetId: String, comics: List<IModel.IComicModel>) {
        doAsync {
            mISheetInfoDataSource.deleteComics(sheetId, comics, object : BaseDataSource.LoadSourceCallback<String?> {
                override fun onDataLoadFiled() {
                    uiThread {
                        if (mISheetInfoView.isActive())
                            mISheetInfoView.showDeleteComicsState(false)
                    }
                }

                override fun onDataLoaded(dataModel: String?) {
                    uiThread {
                        if (mISheetInfoView.isActive())
                            mISheetInfoView.showDeleteComicsState(true)
                    }
                }
            })
        }
    }

    /**
     * 预加载数据
     */
    private fun loadSource() {
        mISheetInfoDataSource.setSheetId(mType, mId, mPage)
        doAsync {
            mISheetInfoDataSource.getData(object : BaseDataSource.LoadSourceCallback<IModel.ISheetModel> {
                override fun onDataLoaded(dataModel: IModel.ISheetModel) {
                    uiThread {
                        if (mISheetInfoView.isActive())
                            loadSheetInfo(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mISheetInfoView.isActive())
                            mISheetInfoView.showRefreshFailed()
                    }
                }
            })
        }
    }

    private fun loadSheetInfo(dataModel: IModel.ISheetModel) {
        if (mPage == 1) {
            mISheetInfoView.showSheetInfo(dataModel)
            return
        }
        mISheetInfoView.showMoreSheetInfo(dataModel)
    }

    /**
     * 收藏漫单
     */
    private fun collect(sheetId: String, follow: Int) {
        doAsync {
            mISheetInfoDataSource.collectSheet(mType, sheetId, follow, object : BaseDataSource.LoadSourceCallback<String?> {
                override fun onDataLoaded(dataModel: String?) {
                    uiThread {
                        if (mISheetInfoView.isActive())
                            mISheetInfoView.showCollectSucceed()
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mISheetInfoView.isActive())
                            mISheetInfoView.showCollectFailed()
                    }
                }
            })
        }
    }
}