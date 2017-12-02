package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.MyContract
import fm.dongman.animefm.contractlib.contract.SheetCreateContract
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 漫单创建数据操作类
 * Created by shize on 2017/10/18.
 */
class SheetCreatePresenter constructor(private val mISheetCreateView: SheetCreateContract.ISheetCreateView,
                                       private val mISheetCreateDataSource: SheetCreateContract.ISheetCreateDataSource)
    : SheetCreateContract.ISheetCreatePresenter {

    init {
        mISheetCreateView.setPresenter(this)
    }

    override fun startLoad() {}

    override fun refreshPage() {}

    override fun createSheet(sheet: IModel.ISheetModel) {
        if (!NetworkHelper.isNetworkAvailable(mISheetCreateView.getViewContext())) {
            mISheetCreateView.showCreateFailed()
            return
        }
        create(sheet)
    }

    override fun setEditSheetId(sheetId: String) {
        if (NetworkHelper.isUnLogged(mISheetCreateView.getViewContext())) {
            mISheetCreateView.showRefreshFailed()
            return
        }
        mISheetCreateDataSource.setEditSheetId(sheetId)
        doAsync {
            mISheetCreateDataSource.getData(object : BaseDataSource.LoadSourceCallback<IModel.ISheetModel> {
                override fun onDataLoadFiled() {
                    uiThread {
                        if (mISheetCreateView.isActive())
                            mISheetCreateView.showRefreshFailed()
                    }
                }

                override fun onDataLoaded(dataModel: IModel.ISheetModel) {
                    uiThread {
                        if (mISheetCreateView.isActive())
                            mISheetCreateView.showEditSheetInfo(dataModel)
                    }
                }
            })
        }
    }

    override fun editSheet(sheet: IModel.ISheetModel) {
        if (!NetworkHelper.isNetworkAvailable(mISheetCreateView.getViewContext())) {
            mISheetCreateView.showEditState(false)
            return
        }
        edit(sheet)
    }

    /**
     * 编辑漫单信息
     */
    private fun edit(sheet: IModel.ISheetModel) {
        doAsync {
            mISheetCreateDataSource.editSheet(sheet, object : BaseDataSource.LoadSourceCallback<String?> {
                override fun onDataLoadFiled() {
                    uiThread {
                        if (mISheetCreateView.isActive())
                            mISheetCreateView.showEditState(false)
                    }
                }

                override fun onDataLoaded(dataModel: String?) {
                    uiThread {
                        if (mISheetCreateView.isActive())
                            mISheetCreateView.showEditState(true)
                    }
                }
            })
        }
    }

    /**
     * 创建漫单
     * @param sheet 创建的漫单
     */
    private fun create(sheet: IModel.ISheetModel) {
        doAsync {
            mISheetCreateDataSource.createSheet(MyContract.USER_ID!!, sheet, object : BaseDataSource.LoadSourceCallback<String?> {
                override fun onDataLoaded(dataModel: String?) {
                    uiThread {
                        if (mISheetCreateView.isActive())
                            mISheetCreateView.showCreateSucceed()
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mISheetCreateView.isActive())
                            mISheetCreateView.showCreateFailed()
                    }
                }
            })
        }
    }
}