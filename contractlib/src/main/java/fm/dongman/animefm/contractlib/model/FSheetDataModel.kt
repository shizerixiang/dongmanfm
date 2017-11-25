package fm.dongman.animefm.contractlib.model

import fm.dongman.animefm.contractlib.IModel

/**
 * 发现->漫单数据模型
 * Created by shize on 2017/10/10.
 */
class FSheetDataModel {
    var mLabels: List<IModel.ISheetLabelModel> = ArrayList() // 专题分类标签
    var specials: List<IModel.ISheetModel> = ArrayList() // 专题列表
    var masters: List<IModel.IUserModel> = ArrayList() // 漫单达人
    var sheets: List<IModel.ISheetModel> = ArrayList() // 漫单列表
}