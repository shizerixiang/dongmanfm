package fm.dongman.contractlib

/**
 * 操作接口
 * Created by shize on 2017/9/21.
 */
interface BaseDataSource<T> {
    /**
     * 回调数据接口
     */
    interface LoadSourceCallback<in T> {
        /**
         * 数据加载完成
         * @param dataModel 首页数据对象
         */
        fun onDataLoaded(dataModel:T)

        /**
         * 数据加载失败
         */
        fun onDataLoadFiled()
    }

    /**
     * 获取数据
     * @param callback 获取数据回调接口
     */
    fun getData(callback: LoadSourceCallback<T>)

    /**
     * 将数据保存
     * @param dataModel 数据
     */
    fun saveData(dataModel: T)

    /**
     * 刷新数据，即：从网络重新加载新数据
     */
    fun refreshData()

    /**
     * 离线模式
     */
    fun offLine()
}