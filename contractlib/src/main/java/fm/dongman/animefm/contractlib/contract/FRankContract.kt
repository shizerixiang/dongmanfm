package fm.dongman.contractlib.contract

import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.contractlib.model.FRankDataModel

/**
 * 发现->漫榜的契约接口
 * Created by shize on 2017/9/25.
 */
interface FRankContract {
    /**
     * 漫榜界面接口，启动页面后使用 [IFRankPresenter.startLoad] 加载数据
     */
    interface IFRankView : BaseView<IFRankPresenter> {
        /**
         * 显示本周最火
         * @param weekHotComicRank 本周最火
         */
        fun showFRankHot(weekHotComicRank: List<IModel.IComicModel>)

        /**
         * 显示新番top
         * @param comicTopRank 新番 top
         */
        fun showFRankTop(comicTopRank: List<IModel.IComicModel>)

        /**
         * 显示国产排行
         * @param domesticComicRank 国产排行
         */
        fun showFRankDomestic(domesticComicRank: List<IModel.IComicModel>)

        /**
         * 显示后宫排行
         * @param haremComicRank 后宫排行
         */
        fun showFRankHarem(haremComicRank: List<IModel.IComicModel>)

        /**
         * 显示更多排行，点击直接跳转 web 页面
         * @param url 网页地址
         */
        fun showFRankMore(url:String)
    }

    /**
     * 漫榜操作接口
     */
    interface IFRankPresenter : BasePresenter {
        /**
         * 获取更多排行
         */
        fun getMoreRank()
    }

    /**
     * 漫榜数据接口
     */
    interface IFRankDataSource : BaseDataSource<FRankDataModel> {
        /**
         * 获取更多排行
         * @return 返回地址（本地常量）
         */
        fun getMoreRank():String
    }

}
