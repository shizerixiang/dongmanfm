package fm.dongman.animefm.contractlib.model

import fm.dongman.animefm.contractlib.IModel

/**
 * 新番数据模型
 * Created by shize on 2017/10/10.
 */
class ComicDataModel {
    var quarter: String = ""
    var newComics: List<IModel.IComicModel> = ArrayList() // 最近更新番剧
    var recommendComics: List<IModel.IComicModel> = ArrayList() // 推荐番剧
    var newComment: List<IModel.ICommentModel> = ArrayList() // 最新槽点
    var guessLikeComics: List<IModel.IComicModel> = ArrayList() // 猜你喜欢的番剧
}