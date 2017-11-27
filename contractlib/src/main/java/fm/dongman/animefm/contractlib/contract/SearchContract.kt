package fm.dongman.animefm.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.model.SearchDataModel

/**
 * 搜索契约接口
 * Created by shize on 2017/10/12.
 */
interface SearchContract {
    /**
     * 搜索界面接口
     */
    interface ISearchView : BaseView<ISearchPresenter> {
        /**
         * 显示历史搜索
         * @param historyList 历史搜索
         */
        fun showHistorySearch(historyList: List<String>)

        /**
         * 显示热门搜索
         * @param hotList 热门搜索
         */
        fun showHotSearch(hotList: List<String>)

        /**
         * 显示清除历史搜索记录结果
         * 由 [ISearchPresenter.clearHistorySearch] 回调
         * @param boolean 是否清除成功
         */
        fun showClearHistoryState(boolean: Boolean)
    }

    /**
     * 搜索操作接口
     */
    interface ISearchPresenter : BasePresenter{
        /**
         * 清除历史搜索记录
         * 异步调用 [ISearchDataSource.clearHistorySearch] 清除数据
         * 并在主线程中调用 [ISearchView.showClearHistoryState] 显示清除结果
         */
        fun clearHistorySearch()
    }

    /**
     * 搜索数据源接口
     */
    interface ISearchDataSource : BaseDataSource<SearchDataModel>{
        /**
         * 清除历史搜索记录
         * @param callback 清除回调
         */
        fun clearHistorySearch(callback: BaseDataSource.LoadSourceCallback<String>)
    }
}