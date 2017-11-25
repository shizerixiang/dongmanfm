package fm.dongman.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.contractlib.model.HomeDataModel
import fm.dongman.animefm.contractlib.IModel.*

/**
 * 首页的契约接口
 * Created by shize on 2017/9/21.
 */
interface HomeContract {
    /**
     * 首页界面接口
     * 启动界面后调用 [BasePresenter.startLoad] 加载数据
     */
    interface IHomeView : BaseView<IHomePresenter> {
        /**
         * 显示横幅
         * 在调用 [BasePresenter.startLoad] 获取到数据之后，回调该方法显示数据
         * @param banners 横幅
         */
        fun showHomeBanners(banners: List<IBannerModel>)

        /**
         * 显示FM独家推荐列表
         * 在调用 [BasePresenter.startLoad] 获取到数据之后，回调该方法显示数据
         * @param fmList 独家推荐动漫
         */
        fun showHomeFMRecommendList(fmList: List<IComicModel>)

        /**
         * 显示漫单推荐列表
         * 在调用 [BasePresenter.startLoad] 获取到数据之后，回调该方法显示数据
         * @param sheetList 推荐漫单
         */
        fun showHomeSheetRecommendList(sheetList: List<ISheetModel>)

        /**
         * 显示人气番剧列表
         * 在调用 [BasePresenter.startLoad] 获取到数据之后，回调该方法显示数据
         * @param comicList 人气番剧
         */
        fun showHotRankComicList(comicList:List<IComicModel>)

        /**
         * 显示最新文章列表
         * 在调用 [BasePresenter.startLoad] 获取到数据之后，回调该方法显示数据
         * @param articleList 最新文章
         */
        fun showHomeNewArticleList(articleList: List<IArticleModel>)

        /**
         * 显示最新留言列表
         * 在调用 [BasePresenter.startLoad] 获取到数据之后，回调该方法显示数据
         * @param commentList 最新评论
         */
        fun showHomeNewCommentList(commentList: List<ICommentModel>)

//        /**
//         * 显示用户推荐列表 (正在开发)
//         * 在调用 [BasePresenter.startLoad] 获取到数据之后，回调该方法显示数据
//         * @param userRecommendList 用户推荐动漫
//         */
//        fun showHomeUserRecommendList(userRecommendList: List<IComicModel>)
    }

    /**
     * 首页操作接口
     */
    interface IHomePresenter : BasePresenter

    /**
     * 首页数据接口
     * 需要三个实现，一个实现本地 Local 和另一个实现网络 Remote 获取数据
     * 除此之外还需要实现一个负责判断调用的 Repository 来判断何时调用
     */
    interface IHomeDataSource : BaseDataSource<HomeDataModel>
}
