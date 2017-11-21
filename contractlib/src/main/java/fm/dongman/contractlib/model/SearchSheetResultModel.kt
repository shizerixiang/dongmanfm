package fm.dongman.contractlib.model

import fm.dongman.contractlib.IModel
import java.util.*

/**
 * 漫单/专题搜索结果模型
 * Created by shize on 2017/11/15.
 */
class SearchSheetResultModel {
    var mSheetResultList:List<IModel.ISheetModel> = ArrayList()  // 漫单结果集合
    var mTopicResultList:List<IModel.ISheetModel> = ArrayList()  // 专题结果集合
}