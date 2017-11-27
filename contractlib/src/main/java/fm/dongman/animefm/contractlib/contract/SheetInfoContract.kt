package fm.dongman.animefm.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel

/**
 * 漫单信息契约接口
 * Created by shize on 2017/10/18.
 */
interface SheetInfoContract {
    /**
     * 漫单信息界面接口
     */
    interface ISheetInfoView : BaseView<ISheetInfoPresenter> {
        /**
         * 显示漫单
         * 由 [ISheetInfoPresenter.getSheetInfo] 回调
         * @param sheetInfo 漫单信息
         */
        fun showSheetInfo(sheetInfo: IModel.ISheetModel)

        /**
         * 显示更多漫单动漫列表
         * 由 [ISheetInfoPresenter.getSheetInfo] 回调
         * @param sheetInfo 漫单信息
         */
        fun showMoreSheetInfo(sheetInfo: IModel.ISheetModel)

        /**
         * 显示收藏成功
         * 由 [ISheetInfoPresenter.collectSheet] 回调
         */
        fun showCollectSucceed()

        /**
         * 显示收藏失败
         * 由 [ISheetInfoPresenter.collectSheet] 回调
         */
        fun showCollectFailed()

        /**
         * 显示删除动漫状态
         * 由 [ISheetInfoPresenter.deleteComics] 回调
         * @param state 是否删除成功
         */
        fun showDeleteComicsState(state:Boolean)
    }

    /**
     * 漫单信息数据操作接口
     */
    interface ISheetInfoPresenter : BasePresenter {
        /**
         * 获取漫单信息
         * 通过 [ISheetInfoDataSource.setSheetId] 再异步调用 [ISheetInfoDataSource.getData] 获取数据
         * 并在 UI 线程中调用 [ISheetInfoView.showSheetInfo] or [ISheetInfoView.showMoreSheetInfo] 显示数据
         * @param type 类型 漫单 或 专题
         * @param id 漫单 id
         */
        fun getSheetInfo(type: String, id: String, page: Int)

        /**
         * 收藏漫单
         * 通过异步调用 [ISheetInfoDataSource.collectSheet] 获取数据
         * 并在 UI 线程中调用 [ISheetInfoView.showCollectSucceed] 或 [ISheetInfoView.showCollectFailed] 显示数据
         * @param type 类型 漫单 或 专题
         * @param sheetId 漫单 id
         * @param follow 关注操作，关注 or 取消关注 [IS_FOLLOWED] or [NOT_FOLLOWED]
         */
        fun collectSheet(type: String, sheetId: String, follow: Int)

        /**
         * 删除漫单的动漫
         * 通过异步调用 [ISheetInfoDataSource.deleteComics] 获取数据
         * 并在 UI 线程中调用 [ISheetInfoView.showDeleteComicsState] 显示数据
         * @param sheetId 漫单 id
         * @param comics 动漫集合
         */
        fun deleteComics(sheetId: String,comics:List<IModel.IComicModel>)

        companion object {
            val TYPE_MANDAN: String = "mandan" // 漫单
            val TYPE_TOPIC: String = "topic" // 专题

            val IS_FOLLOWED: Int = 1 // 关注/收藏
            val NOT_FOLLOWED: Int = 0 // 取消关注/收藏
        }
    }

    /**
     * 漫单信息数据源接口
     */
    interface ISheetInfoDataSource : BaseDataSource<IModel.ISheetModel> {
        /**
         * 设置漫单 id
         * @param type 类型 漫单 或 专题
         * @param id 漫单 id
         * @param page 页数
         */
        fun setSheetId(type: String, id: String, page: Int)

        /**
         * 收藏漫单
         * @param type 类型 漫单 或 专题
         * @param sheetId 漫单 id
         * @param follow 是否关注
         * @param callback 收藏状态回调
         */
        fun collectSheet(type: String, sheetId: String, follow: Int, callback: BaseDataSource.LoadSourceCallback<String?>)

        /**
         * 删除漫单中的动漫
         * @param sheetId 漫单 id
         * @param comics 动漫集合
         * @param callback 回调
         */
        fun deleteComics(sheetId: String, comics: List<IModel.IComicModel>, callback: BaseDataSource.LoadSourceCallback<String?>)
    }
}