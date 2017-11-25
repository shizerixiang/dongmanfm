package fm.dongman.animefm.contractlib.model

import fm.dongman.animefm.contractlib.IModel

/**
 * 动漫详情页面数据模型
 * Created by shize on 2017/10/17.
 */
class ComicInfoDataModel {
    var comicModel: IModel.IComicModel? = null // 动漫详情
    var relatedComics: List<IModel.IComicModel> = ArrayList() // 相关番剧
    var relatedArticles: List<IModel.IArticleModel> = ArrayList() // 相关漫帖
    var relatedSheets: List<IModel.ISheetModel> = ArrayList() // 相关漫单
    var relatedComment:List<IModel.ICommentModel> = ArrayList() // 相关评论
}