package fm.dongman.animefm.contractlib.model

import fm.dongman.animefm.contractlib.IModel

/**
 * 发现->榜单数据模型
 * Created by shize on 2017/10/10.
 */
class FRankDataModel {
    var weekHotComicRank: List<IModel.IComicModel> = ArrayList() // 本周最火
    var comicTopRank: List<IModel.IComicModel> = ArrayList() // 新番top
    var domesticComicRank: List<IModel.IComicModel> = ArrayList() // 国产排行
    var haremComicRank: List<IModel.IComicModel> = ArrayList() // 后宫排行
}