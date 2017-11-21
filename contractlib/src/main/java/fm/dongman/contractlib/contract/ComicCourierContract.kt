package fm.dongman.contractlib.contract

import fm.dongman.contractlib.BaseDataSource
import fm.dongman.contractlib.BasePresenter
import fm.dongman.contractlib.BaseView
import fm.dongman.contractlib.IModel

/**
 * 番剧速递契约接口
 * Created by shize on 2017/10/19.
 */
interface ComicCourierContract {
    /**
     * 番剧速递页面接口
     */
    interface IComicCourierView : BaseView<IComicCourierPresenter> {
        /**
         * 显示季度
         * 由 [IComicCourierPresenter.startLoad] 回调
         * @param quarters 季度集合
         */
        fun showQuarters(quarters: List<Map<String, String>>)

        /**
         * 显示新番列表
         * 需要先设置 [IComicCourierPresenter.quarterComic] 季度才可以调用 [IComicCourierPresenter.startLoad] 后回调
         * @param comics 新番
         * @param quarter 季度
         */
        fun showComicList(comics: List<IModel.IComicModel>, quarter: String)
    }

    /**
     * 番剧速递数据操作接口
     * [startLoad] 后 [IComicCourierView.showQuarters] 显示季度
     */
    interface IComicCourierPresenter : BasePresenter {
        /**
         * 季度新番
         * 通过异步调用 [IComicCourierDataSource.getComicList] 获取数据
         * UI 线程调用 [IComicCourierView.showComicList] 显示数据
         * @param quarter 季度
         * @param order 排序，具体参考 [IComicCourierDataSource] 的实现类
         */
        fun quarterComic(quarter: String, order: String)
    }

    /**
     * 番剧速递数据源接口
     * 泛型为 季度 集合
     */
    interface IComicCourierDataSource : BaseDataSource<List<Map<String, String>>> {
        /**
         * 获取番剧
         * 设置季度之后在 [getData] 中应用
         * @param quarter 季度
         * @param order 排序，具体参考 [IComicCourierDataSource] 的实现类
         * @param callback 获取数据回调
         */
        fun getComicList(quarter: String, order: String, callback: BaseDataSource.LoadSourceCallback<List<IModel.IComicModel>>)
    }
}