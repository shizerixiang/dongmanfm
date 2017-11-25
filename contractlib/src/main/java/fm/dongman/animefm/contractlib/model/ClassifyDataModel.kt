package fm.dongman.animefm.contractlib.model

/**
 * 分类番剧模型
 * Created by shize on 2017/10/19.
 */
class ClassifyDataModel {
    var countries: ArrayList<TypeData> = ArrayList() // 国家
    var type: ArrayList<TypeData> = ArrayList() // 类型
    var time: ArrayList<TypeData> = ArrayList() // 时间
    var state: ArrayList<TypeData> = ArrayList() // 状态
    var size: ArrayList<TypeData> = ArrayList() // 篇幅

    /**
     * 分类过滤器
     */
    class ClassifyFilter {
        /**
         * 排序，默认为[ORDER_NEW]
         * 其他包括 [ORDER_HOT] 以及 [ORDER_GOOD] 排序方式
         */
        var order: Int = ORDER_HOT
        var count:Int = DEFAULT_COUNT // 每页数据个数
        var country: String = "" // 国家
        var type: String = "" // 类型
        var time: String = "" // 时间
        var state: String = "" // 状态
        var size: String = "" // 篇幅

        companion object {
            val ORDER_NEW: Int = 1 // 最新
            val ORDER_HOT: Int = 2 // 最热
            val ORDER_GOOD: Int = 3 // 好评

            val DEFAULT_COUNT:Int = 12 // 每页数据默认个数
        }
    }

    /**
     * 类型对象
     */
    class TypeData {
        constructor()
        /**
         * @param id id
         * @param name 名称
         */
        constructor(id: String, name: String) {
            this.id = id
            this.name = name
        }

        var id: String = "" // id
        var name: String = "全部" // 类型名
    }
}