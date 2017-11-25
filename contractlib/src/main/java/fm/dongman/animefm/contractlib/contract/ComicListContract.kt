package fm.dongman.animefm.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel

/**
 * 番剧列表契约接口
 * Created by shize on 2017/10/19.
 */
interface ComicListContract {
    /**
     *  番剧列表界面接口
     */
    interface IComicListView : BaseView<IComicListPresenter> {
        /**
         * 显示番剧列表
         * 由 [IComicListPresenter.startLoad] 调用
         * @param comics 番剧集合
         */
        fun showComicList(comics: List<IModel.IComicModel>)

        /**
         * 显示更多番剧列表
         * 由 [IComicListPresenter.startLoad] 调用
         * @param comics 番剧集合
         */
        fun showMoreComicList(comics: List<IModel.IComicModel>)
    }

    /**
     *  番剧列表数据操作接口
     */
    interface IComicListPresenter : BasePresenter {
        /**
         * 设置关键字
         * 通过异步调用 [IComicListDataSource.searchComics]、[IComicListDataSource.lookedComics] 其中之一获取数据
         * 之后 UI 线程调用 [IComicListView.showComicList] 显示数据
         * @param type 类型 [TYPE_SEARCH] 、 [TYPE_LOOK] 之一
         * @param key 关键字 or id
         * @param page 页数
         */
        fun setKey(type: String, key: String?, page: Int)

        companion object {
            val TYPE_SEARCH: String = "search" // 搜索
            val TYPE_LOOK: String = "look" // 观看番剧记录
        }
    }

    /**
     *  番剧列表数据源接口
     */
    interface IComicListDataSource : BaseDataSource<String> {
        /**
         * 通过设置关键字，搜索番剧
         * @param key 关键字
         * @param page 页数
         * @param callback 获取数据回调
         */
        fun searchComics(key: String, page: Int, callback: BaseDataSource.LoadSourceCallback<List<IModel.IComicModel>>)

        /**
         * 通过设置关键字，搜索番剧
         * @param id 用户 id
         * @param page 页数
         * @param callback 获取数据回调
         */
        fun lookedComics(id: String, page: Int, callback: BaseDataSource.LoadSourceCallback<List<IModel.IComicModel>>)
    }
}