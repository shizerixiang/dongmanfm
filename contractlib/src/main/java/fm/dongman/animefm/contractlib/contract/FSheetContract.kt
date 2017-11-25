package fm.dongman.contractlib.contract

import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.contractlib.model.FSheetDataModel

/**
 * 发现->漫单的契约接口
 * Created by shize on 2017/9/25.
 */
interface FSheetContract {
    /**
     * 漫单界面接口，启动页面后调用 [IFSheetPresenter.startLoad] 加载页面数据
     */
    interface IFSheetView : BaseView<IFSheetPresenter> {
        /**
         * 显示分类标签
         * @param labels 分类标签
         */
        fun showFSheetLabels(labels: List<IModel.ISheetLabelModel>)

        /**
         * 显示专题列表
         * @param specials 专题列表
         */
        fun showFSheetSpecials(specials: List<IModel.ISheetModel>)

        /**
         * 显示漫单达人
         * @param masters 漫单达人
         */
        fun showFSheetMasters(masters: List<IModel.IUserModel>)

        /**
         * 显示漫单列表
         * @param sheets 漫单列表
         */
        fun showFSheets(sheets: List<IModel.ISheetModel>)
    }

    /**
     * 漫单操作接口
     */
    interface IFSheetPresenter : BasePresenter

    /**
     * 漫单数据接口
     */
    interface IFSheetDataSource : BaseDataSource<FSheetDataModel>

}
