package fm.dongman.animefm.contractlib.model

import fm.dongman.animefm.contractlib.IModel

/**
 * 漫单/专题搜索结果模型
 * Created by shize on 2017/11/15.
 */
class SearchSheetResultModel {
    var mSheetResultList:List<IModel.ISheetModel> = ArrayList()  // 漫单结果集合
    var mTopicResultList:List<IModel.ISheetModel> = ArrayList()  // 专题结果集合
}