package fm.dongman.animefm.contractlib.contract

import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.model.ComicDataModel

/**
 * 番剧的契约接口
 * Created by shize on 2017/9/22.
 */
interface ComicContract {
    /**
     * 番剧界面接口，onStart 时调用 [IComicPresenter.startLoad]
     */
    interface IComicView : BaseView<IComicPresenter> {
        /**
         * 显示本季度
         * @param quarter 本季度
         */
        fun showQuarter(quarter: String)

        /**
         * 显示最近新番
         * @param newComics 最近新番
         */
        fun showNewComics(newComics: List<IModel.IComicModel>)

        /**
         * 显示推荐番剧
         * @param recommendComics 推荐番剧
         */
        fun showRecommendComics(recommendComics: List<IModel.IComicModel>)

        /**
         * 显示最新槽点
         * @param newComment 最新槽点
         */
        fun showNewComments(newComment: List<IModel.ICommentModel>)

        /**
         * 显示经典老梗（暂时预留）
         * @param
         */
        //        fun showClassicNeta()

        /**
         * 显示猜你喜欢
         * @param guessLikeComics 猜你喜欢的番剧
         */
        fun showGuessLikeComics(guessLikeComics: List<IModel.IComicModel>)
    }

    /**
     * 番剧操作接口
     */
    interface IComicPresenter : BasePresenter {
        /**
         * 换一换
         * 在异步中调用 [IComicDataSource.getComicGuessLike] ，获取新的推荐
         * 在 UI 线程中调用 [IComicView.showGuessLikeComics] 再次显示数据
         */
        fun changeGuessList()
    }

    /**
     * 番剧数据接口
     */
    interface IComicDataSource : BaseDataSource<ComicDataModel> {
        /**
         * 获取猜你喜欢的番剧
         * @param callback 数据回调
         */
        fun getComicGuessLike(callback: BaseDataSource.LoadSourceCallback<List<IModel.IComicModel>>)
    }
}