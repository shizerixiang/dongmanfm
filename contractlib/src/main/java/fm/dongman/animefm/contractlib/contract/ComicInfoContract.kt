package fm.dongman.animefm.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.model.ComicInfoDataModel
import fm.dongman.animefm.contractlib.model.ScoreModel

/**
 * 动漫详情契约接口
 * Created by shize on 2017/10/17.
 */
interface ComicInfoContract {
    /**
     * 动漫信息界面接口
     */
    interface IComicInfoView : BaseView<IComicInfoPresenter> {
        /**
         * 显示动漫
         * 由 [IComicInfoPresenter.getComicInfo] 回调
         * @param comicInfo 动漫信息
         */
        fun showComicInfo(comicInfo: IModel.IComicModel)

        /**
         * 显示相关番剧
         * @param comics 番剧集合
         */
        fun showRelatedComics(comics: List<IModel.IComicModel>)

        /**
         * 显示相关漫帖
         * @param articles 漫帖集合
         */
        fun showRelatedArticle(articles: List<IModel.IArticleModel>)

        /**
         * 显示相关漫单
         * @param sheets 漫单集合
         */
        fun showRelatedSheets(sheets: List<IModel.ISheetModel>)

        /**
         * 显示相关评论
         * @param comments 评论集合
         */
        fun showRelatedComments(comments:List<IModel.ICommentModel>)

        /**
         * 显示加入漫单成功
         * 由 [IComicInfoPresenter.joinSheet] 回调
         */
        fun showJoinSheetSucceed()

        /**
         * 显示加入漫单失败
         * 由 [IComicInfoPresenter.joinSheet] 回调
         */
        fun showJoinSheetFailed()

        /**
         * 显示关注操作成功
         * 由 [IComicInfoPresenter.followComic] 回调
         */
        fun showFollowSucceed()

        /**
         * 显示关注操作失败
         * 由 [IComicInfoPresenter.followComic] 回调
         */
        fun showFollowFailed()

        /**
         * 显示评分成功
         * 由 [IComicInfoPresenter.scoreComic] 回调
         * @param score 分数模型
         */
        fun showScoreSucceed(score: ScoreModel)

        /**
         * 显示评分失败
         * 由 [IComicInfoPresenter.scoreComic] 回调
         */
        fun showScoreFailed()

        /**
         * 显示我的漫单
         * 由 [IComicInfoPresenter.getMySheets] 回调
         * @param sheets 我的漫单
         */
        fun showMySheets(sheets: List<IModel.ISheetModel>)

        /**
         * 显示获取我的漫单失败
         * 由 [IComicInfoPresenter.getMySheets] 回调
         */
        fun showGetMySheetsFailed()
    }

    /**
     * 动漫详情数据操作接口
     */
    interface IComicInfoPresenter : BasePresenter {
        /**
         * 获取动漫信息
         * 通过异步调用 [IComicInfoDataSource.setComicId] 之后 通过 [IComicInfoDataSource.getData] 获取数据
         * 并在 UI 线程中调用对应 show 方法显示数据
         * @param id 动漫 id
         */
        fun getComicInfo(id: String)

        /**
         * 关注动漫
         * 通过异步调用 [IComicInfoDataSource.followComic] 获取数据
         * 并在 UI 线程中调用 [IComicInfoView.showFollowSucceed] 或 [IComicInfoView.showFollowFailed] 显示数据
         * @param comicId 动漫 id
         */
        fun followComic(comicId: String)

        /**
         * 动漫评分
         * 通过异步调用 [IComicInfoDataSource.scoreComic] 获取数据
         * 并在 UI 线程中调用 [IComicInfoView.showScoreSucceed] 或 [IComicInfoView.showScoreFailed] 显示数据
         * @param score 分数模型
         */
        fun scoreComic(score: ScoreModel)

        /**
         * 获取我的漫单
         * 通过异步调用 [IComicInfoDataSource.obtainMySheets] 获取数据
         * 并在 UI 线程中调用 [IComicInfoView.showMySheets] 或 [IComicInfoView.showGetMySheetsFailed] 显示数据
         */
        fun getMySheets()

        /**
         * 加入我的漫单
         * 通过异步调用 [IComicInfoDataSource.joinSheet] 获取数据
         * 并在 UI 线程中调用 [IComicInfoView.showJoinSheetSucceed] 或 [IComicInfoView.showJoinSheetFailed] 显示数据
         * @param sheetId 漫单 id
         */
        fun joinSheet(sheetId: String)
    }

    /**
     * 动漫详情数据接口
     */
    interface IComicInfoDataSource : BaseDataSource<ComicInfoDataModel> {
        /**
         * 设置动漫 id
         * 只有设置了 id 才能获取到动漫详情数据
         * @param id 动漫 id
         */
        fun setComicId(id: String)

        /**
         * 关注动漫
         * @param userId 用户 id
         * @param comicId 动漫 id
         * @param callback 获取信息回调接口
         */
        fun followComic(userId: String, comicId: String, callback: BaseDataSource.LoadSourceCallback<Void>)

        /**
         * 动漫评分
         * @param score 分数模型
         * @param callback 获取信息回调接口
         */
        fun scoreComic(score: ScoreModel, callback: BaseDataSource.LoadSourceCallback<ScoreModel>)

        /**
         * 获取我的漫单
         * @param userId 用户 id
         * @param callback 获取信息回调接口
         */
        fun obtainMySheets(userId: String, callback: BaseDataSource.LoadSourceCallback<List<IModel.ISheetModel>>)

        /**
         * 加入漫单
         * @param sheetId 漫单 id
         * @param callback 获取信息回调接口
         */
        fun joinSheet(sheetId: String, callback: BaseDataSource.LoadSourceCallback<Void>)
    }
}