package fm.dongman.animefm.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel

/**
 * 漫单创建契约接口
 * Created by shize on 2017/10/17.
 */
interface SheetCreateContract {
    /**
     * 创建漫单界面接口
     */
    interface ISheetCreateView : BaseView<ISheetCreatePresenter> {
        /**
         * 显示创建漫单成功
         * 由 [ISheetCreatePresenter.createSheet] 回调
         */
        fun showCreateSucceed()

        /**
         * 显示创建漫单失败
         * 由 [ISheetCreatePresenter.createSheet] 回调
         */
        fun showCreateFailed()

        /**
         * 显示编辑漫单的信息
         * 由 [ISheetCreatePresenter.setEditSheetId] 回调
         * @param sheet 需要编辑的漫单数据
         */
        fun showEditSheetInfo(sheet:IModel.ISheetModel)

        /**
         * 显示编辑漫单状态
         * 由 [ISheetCreatePresenter.editSheet] 回调
         * @param boolean 是否成功修改漫单数据
         */
        fun showEditState(boolean: Boolean)
    }

    /**
     * 漫单创建数据操作接口
     */
    interface ISheetCreatePresenter:BasePresenter{
        /**
         * 创建漫单
         * 通过异步调用 [ISheetCreateDataSource.createSheet] 获取数据
         * 并在 UI 线程中调用 [ISheetCreateView.showCreateSucceed] 或 [ISheetCreateView.showCreateFailed] 显示数据
         * @param sheet 漫单
         */
        fun createSheet(sheet: IModel.ISheetModel)

        /**
         * 设置编辑漫单的 id
         * 通过 设置漫单id [ISheetCreateDataSource.setEditSheetId] 再异步调用 [ISheetCreateDataSource.getData] 获取要编辑的漫单数据
         * 并在 UI 线程中调用 [ISheetCreateView.showEditSheetInfo] 显示漫单数据
         * @param sheetId 编辑漫单的 id
         */
        fun setEditSheetId(sheetId:String)

        /**
         * 编辑漫单
         * 通过异步调用 [ISheetCreateDataSource.editSheet] 获取数据
         * 并在 UI 线程中调用 [ISheetCreateView.showEditState] 显示数据
         * @param sheet 修改后的漫单
         */
        fun editSheet(sheet: IModel.ISheetModel)
    }

    /**
     * 漫单数据源接口
     */
    interface ISheetCreateDataSource:BaseDataSource<IModel.IUserModel>{
        /**
         * 创建漫单
         * @param userId 用户 id
         * @param sheet 漫单
         * @param callback 获取信息回调接口
         */
        fun createSheet(userId: String, sheet: IModel.ISheetModel, callback: BaseDataSource.LoadSourceCallback<String?>)

        /**
         * 设置编辑漫单的 id
         * @param sheetId 漫单 id
         */
        fun setEditSheetId(sheetId: String)

        /**
         * 提交编辑漫单信息
         * @param sheet 编辑后的漫单
         * @param callback 获取信息回调接口
         */
        fun editSheet(sheet:IModel.ISheetModel, callback: BaseDataSource.LoadSourceCallback<String?>)
    }
}