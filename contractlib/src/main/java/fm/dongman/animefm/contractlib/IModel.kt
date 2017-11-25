package fm.dongman.animefm.contractlib

import android.os.Parcelable

/**
 * 模型接口
 * Created by shize on 2017/9/21.
 */
interface IModel {
    /**
     * 文章接口
     */
    interface IArticleModel:Parcelable

    /**
     * 横幅接口
     */
    interface IBannerModel:Parcelable

    /**
     * 动漫接口
     */
    interface IComicModel:Parcelable

    /**
     * 评论接口
     */
    interface ICommentModel:Parcelable

    /**
     * 漫单接口
     */
    interface ISheetModel:Parcelable

    /**
     * 用户接口
     */
    interface IUserModel:Parcelable

    /**
     * 消息接口
     */
    interface IMsgModel

    /**
     * 反馈信息接口
     */
    interface IFeedBackModel

    /**
     * 专题标签
     */
    interface ISheetLabelModel
}