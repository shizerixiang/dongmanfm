package fm.dongman.animefm.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.model.SearchSheetResultModel

/**
 * 漫单列表契约接口
 * Created by shize on 2017/10/18.
 */
interface SheetListContract {
    /**
     * 漫单列表界面接口
     */
    interface ISheetListView : BaseView<ISheetListPresenter> {
        /**
         * 显示漫单/专题列表
         * 由 [ISheetListPresenter.getSheets] 回调
         * @param sheets 漫单集合
         */
        fun showSheets(sheets: List<IModel.ISheetModel>)

        /**
         * 显示更多漫单/专题
         * 由 [ISheetListPresenter.getSheets] 回调
         * @param sheets 更多漫单
         */
        fun showMoreSheets(sheets: List<IModel.ISheetModel>)

        /**
         * 显示搜索的专题列表
         * @param topics 搜索的专题
         */
        fun showSearchTopicResult(topics:List<IModel.ISheetModel>)

        /**
         * 显示更多搜索的专题
         * @param topics 搜索的专题
         */
        fun showMoreSearchTopicResult(topics:List<IModel.ISheetModel>)
    }

    /**
     * 漫单列表数据操作接口
     */
    interface ISheetListPresenter : BasePresenter {
        /**
         * 获取漫单列表
         * 通过 [ISheetListDataSource.setSheetListInfo] 再异步调用 [ISheetListDataSource.getData] 获取数据
         * 并在 UI 线程中调用 [ISheetListView.showSheets] 或 [ISheetListView.showMoreSheets] 显示结果
         * @param page 页数（从 1 开始，每页 10 个数据）
         * @param type 漫单列表类型
         * @param id 若是特殊类型则需要 id（如：用户创建的漫单，需要用户 id），不是则为 null
         */
        fun getSheets(page: Int, type: String, id: String?)

        companion object{
            val TYPE_SEARCH:String = "search" // 搜索
        }
    }

    /**
     * 漫单列表数据源接口
     */
    interface ISheetListDataSource : BaseDataSource<List<IModel.ISheetModel>> {
        /**
         * 获取漫单列表
         * @param order 列表排序
         * @param page 页数 （从 1 开始，每次加载 10 个数据）
         * @param type 漫单列表类型
         * @param id 若是特殊类型则需要 id（如：用户创建的漫单，需要用户 id），不是则为 null
         */
        fun setSheetListInfo(order: Int, page: Int, type: String, id: String?)

        /**
         * 搜索漫单
         * @param key 关键字
         * @param callback 获取数据回调
         */
        fun searchSheets(key:String, callback: BaseDataSource.LoadSourceCallback<SearchSheetResultModel>)
    }
}