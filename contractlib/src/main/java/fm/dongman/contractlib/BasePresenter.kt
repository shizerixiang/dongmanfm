package fm.dongman.contractlib

/**
 * Presenter 接口
 * Created by shize on 2017/9/21.
 */
interface BasePresenter {
    // -----------------------------------操作-----------------------------------
    /**
     * 初始化 Presenter
     */
    fun startLoad()

    /**
     * 刷新操作
     */
    fun refreshPage()
}