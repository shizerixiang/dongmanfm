package fm.dongman.contractlib.model

import fm.dongman.contractlib.IModel
import java.util.*

/**
 * 动漫详情页面数据模型
 * Created by shize on 2017/10/17.
 */
class ComicInfoDataModel {
    var comicModel: IModel.IComicModel? = null // 动漫详情
    var relatedComics: List<IModel.IComicModel> = ArrayList() // 相关番剧
    var relatedArticles: List<IModel.IArticleModel> = ArrayList() // 相关漫帖
    var relatedSheets: List<IModel.ISheetModel> = ArrayList() // 相关漫单
}