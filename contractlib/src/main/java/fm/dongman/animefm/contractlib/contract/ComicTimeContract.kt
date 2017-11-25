package fm.dongman.animefm.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel

/**
 * 时间表契约接口
 * Created by shize on 2017/10/24.
 */
interface ComicTimeContract {
    /**
     * 时间表界面接口
     */
    interface IComicTimeView : BaseView<IComicTimePresenter> {
        /**
         * 显示一周每日时间
         * 由 [IComicTimePresenter.startLoad] 回调
         * @param weekdays 每日集合
         */
        fun showWeekDays(weekdays: List<Map<String, String>>)

        /**
         * 显示新番失败
         * 由 [IComicTimePresenter.getWeekDayComics] 回调
         */
        fun showLoadComicsFailed()

        /**
         * 显示新番列表
         * 由 [IComicTimePresenter.getWeekDayComics] 回调
         * @param comics 新番集合
         */
        fun showComicList(comics: List<IModel.IComicModel>, weekday: String)
    }

    /**
     * 时间表数据操作接口
     * [startLoad] 获取时间表 [IComicTimeView.showWeekDays] 显示时间表
     */
    interface IComicTimePresenter : BasePresenter {
        /**
         * 获取平日新番
         * 通过异步调用 [IComicTimeDataSource.getData]获取数据
         * 之后 UI 线程调用 [IComicTimeView.showComicList] 或 [IComicTimeView.showLoadComicsFailed] 显示数据
         * @param weekday 平日
         */
        fun getWeekDayComics(weekday: String)
    }

    /**
     * 时间表数据源接口
     */
    interface IComicTimeDataSource : BaseDataSource<List<Map<String,String>>> {
        /**
         * 获取平日新番
         * @param weekday 平日
         * @param callback 获取数据回调
         */
        fun getWeekDayComics(weekday: String, callback: BaseDataSource.LoadSourceCallback<List<IModel.IComicModel>>)
    }
}