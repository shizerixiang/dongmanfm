package fm.dongman.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel

/**
 * 榜单信息契约接口
 * Created by shize on 2017/10/14.
 */
interface RankInfoContract {
    /**
     * 榜单信息界面接口
     */
    interface IRankInfoView : BaseView<IRankInfoPresenter> {
        /**
         * 显示排行信息
         * 由 [IRankInfoPresenter.getRankInfo] 回调
         * @param comics 排行动漫列表（第一页）
         */
        fun showRankList(comics: List<IModel.IComicModel>)

        /**
         * 显示更多信息
         * 由 [IRankInfoPresenter.getRankInfo] 回调
         * @param comics 更多动漫列表（除了第一页的其他页）
         */
        fun showMoreRankList(comics: List<IModel.IComicModel>)
    }

    /**
     * 榜单信息数据操作接口
     */
    interface IRankInfoPresenter : BasePresenter {
        /**
         * 获取排行动漫列表
         * 通过 [IRankInfoDataSource.setRankInfo] 再异步调用 [IRankInfoDataSource.getData] 获取数据
         * 并在 UI 线程中调用 [IRankInfoView.showRankList] 或 [IRankInfoView.showMoreRankList] 显示数据
         * @param page 页数（从 1 开始，每页 10 个数据）
         * @param type 榜单类型 (由数据层指定类型)
         */
        fun getRankInfo(page: Int, type: String)

        companion object{

//    获取漫榜详情：tag（榜单类型）包括： 'week_hot'=>'本周最火', 'xinfan'=>'新番动漫', 'month_movie_hot'=>'电影动漫', 'hougong'=>'后宫动漫', 'lieqi'=>'猎奇动漫'

            val TYPE_WEEK_HOT = "week_hot"
            val TYPE_XINFAN = "xinfan"
            val TYPE_GUOCHAN = "guochan"
            val TYPE_HOUGONG = "hougong"
        }
    }

    /**
     * 榜单信息数据源接口
     */
    interface IRankInfoDataSource : BaseDataSource<List<IModel.IComicModel>> {
        /**
         * 获取榜单信息
         * @param page 页数 （从 1 开始，每次加载 10 个数据）
         * @param type 榜单 type (由数据层指定类型)
         */
        fun setRankInfo(page: Int, type: String)
    }
}