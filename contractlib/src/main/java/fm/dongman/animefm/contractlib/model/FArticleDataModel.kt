package fm.dongman.contractlib.model

import fm.dongman.animefm.contractlib.IModel
import java.util.*

/**
 * 发现->文章数据模型
 * Created by shize on 2017/10/10.
 */
class FArticleDataModel {
    var dynamicArticles: List<IModel.IArticleModel> = ArrayList() // 漫圈动态
    var hotArticles: List<IModel.IArticleModel> = ArrayList() // 本周最火
    var newArticles: List<IModel.IArticleModel> = ArrayList() // 最新文章
}