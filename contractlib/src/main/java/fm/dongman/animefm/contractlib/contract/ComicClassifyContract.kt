package fm.dongman.animefm.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.model.ClassifyDataModel

/**
 * 番剧分类契约接口
 * Created by shize on 2017/10/19.
 */
interface ComicClassifyContract {
    /**
     * 番剧分类界面接口
     */
    interface IComicClassifyView : BaseView<IComicClassifyPresenter> {
        /**
         * 显示国家分类
         * 由 [IComicClassifyPresenter.startLoad] 回调
         * @param country 国家
         */
        fun showClassifyCountry(country: List<ClassifyDataModel.TypeData>)

        /**
         * 显示类型分类
         * 由 [IComicClassifyPresenter.startLoad] 回调
         * @param types 类型
         */
        fun showClassifyType(types: List<ClassifyDataModel.TypeData>)

        /**
         * 显示时间分类
         * 由 [IComicClassifyPresenter.startLoad] 回调
         * @param time 时间
         */
        fun showClassifyTime(time: List<ClassifyDataModel.TypeData>)

        /**
         * 显示状态分类
         * 由 [IComicClassifyPresenter.startLoad] 回调
         * @param states 状态
         */
        fun showClassifyState(states: List<ClassifyDataModel.TypeData>)

        /**
         * 显示篇幅分类
         * 由 [IComicClassifyPresenter.startLoad] 回调
         * @param sizes 篇幅
         */
        fun showClassifySize(sizes: List<ClassifyDataModel.TypeData>)

        /**
         * 显示番剧列表
         * 由 [IComicClassifyPresenter.getComicList] 回调
         * @param comics 番剧
         */
        fun showComicList(comics: List<IModel.IComicModel>)

        /**
         * 显示更多番剧
         * 由 [IComicClassifyPresenter.getComicList] 回调
         * @param comics 番剧
         */
        fun showMoreComicList(comics: List<IModel.IComicModel>)
    }

    /**
     * 番剧分类数据操作接口
     * 启动在 [startLoad] 中调用 [IComicClassifyDataSource.getData] 获取过滤信息，并显示在界面
     */
    interface IComicClassifyPresenter : BasePresenter {
        /**
         * 获取番剧列表
         * 异步调用 [IComicClassifyDataSource.getComicList] 获取数据
         * UI 线程调用 [IComicClassifyView.showComicList] 或 [IComicClassifyView.showMoreComicList] 显示数据
         * @param page 页数
         * @param filter 过滤器
         */
        fun getComicList(page: Int, filter: ClassifyDataModel.ClassifyFilter)
    }

    /**
     * 番剧分类数据源接口
     */
    interface IComicClassifyDataSource : BaseDataSource<ClassifyDataModel> {
        /**
         * 根据分类条件获取番剧
         * 过滤器条件是在 [getData] 中获取的条件信息
         * @param page 页数
         * @param filter 过滤器
         * @param callback 数据回调接口
         */
        fun getComicList(page: Int, filter: ClassifyDataModel.ClassifyFilter, callback: BaseDataSource.LoadSourceCallback<List<IModel.IComicModel>>)
    }
}
