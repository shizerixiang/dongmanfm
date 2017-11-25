package fm.dongman.animefm.contractlib.model

import fm.dongman.animefm.contractlib.IModel

/**
 * 主页数据模型
 * Created by shize on 2017/10/10.
 */
class HomeDataModel() {
    var banners: List<IModel.IBannerModel> = ArrayList() // 横幅
    var FMRecommends: List<IModel.IComicModel> = ArrayList()// FM独家推荐
    var rankComic: List<IModel.IComicModel> = ArrayList() // 人气番剧
    var sheets: List<IModel.ISheetModel> = ArrayList() // 漫单推荐
    var newArticles: List<IModel.IArticleModel> = ArrayList() // 最新文章
    var newComments: List<IModel.ICommentModel> = ArrayList() // 最新留言
//    var userRecommends: List<IModel.ICommentModel> = ArrayList() // 用户推荐
}