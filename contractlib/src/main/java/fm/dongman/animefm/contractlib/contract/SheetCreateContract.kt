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
    }

    /**
     * 漫单数据源接口
     */
    interface ISheetCreateDataSource:BaseDataSource<Void>{
        /**
         * 创建漫单
         * @param userId 用户 id
         * @param sheet 漫单
         * @param callback 获取信息回调接口
         */
        fun createSheet(userId: String, sheet: IModel.ISheetModel, callback: BaseDataSource.LoadSourceCallback<Void>)
    }
}