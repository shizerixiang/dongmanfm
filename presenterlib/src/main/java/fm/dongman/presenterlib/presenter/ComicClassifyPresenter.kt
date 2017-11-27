package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.ComicClassifyContract
import fm.dongman.animefm.contractlib.model.ClassifyDataModel
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 番剧分类数据操作类
 * Created by shize on 2017/10/20.
 */
class ComicClassifyPresenter constructor(private val mIComicClassifyView: ComicClassifyContract.IComicClassifyView,
                                         private val mIComicClassifyDataSource: ComicClassifyContract.IComicClassifyDataSource)
    : ComicClassifyContract.IComicClassifyPresenter {

    private var mPage: Int = 0 // 页数
    private var mFilter: ClassifyDataModel.ClassifyFilter? = null // 过滤条件

    init {
        mIComicClassifyView.setPresenter(this)
    }

    override fun startLoad() {
        if (!NetworkHelper.isNetworkAvailable(mIComicClassifyView.getViewContext())) {
            mIComicClassifyView.showNoNetwork()
            return
        }
        loadSource()
    }

    override fun refreshPage() {
        mIComicClassifyDataSource.refreshData()
        getComicList(mPage, mFilter!!)
    }

    override fun getComicList(page: Int, filter: ClassifyDataModel.ClassifyFilter) {
        mPage = page
        mFilter = filter
        if (!NetworkHelper.isNetworkAvailable(mIComicClassifyView.getViewContext())) {
            mIComicClassifyView.showNoNetwork()
            return
        }
        getComics()
    }

    /**
     * 获取番剧数据
     */
    private fun getComics() {
        doAsync {
            mIComicClassifyDataSource.getComicList(mPage, mFilter!!, object : BaseDataSource.LoadSourceCallback<List<IModel.IComicModel>> {
                override fun onDataLoaded(dataModel: List<IModel.IComicModel>) {
                    uiThread {
                        if (mIComicClassifyView.isActive())
                            showComicsSource(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIComicClassifyView.isActive())
                            mIComicClassifyView.showRefreshFailed()
                    }
                }
            })
        }
    }

    /**
     * 预加载数据
     */
    private fun loadSource() {
        doAsync {
            mIComicClassifyDataSource.getData(object : BaseDataSource.LoadSourceCallback<ClassifyDataModel> {
                override fun onDataLoaded(dataModel: ClassifyDataModel) {
                    uiThread {
                        if (mIComicClassifyView.isActive())
                            showSource(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIComicClassifyView.isActive())
                            mIComicClassifyView.showRefreshFailed()
                    }
                }
            })
        }
    }

    /**
     * 显示数据
     */
    private fun showSource(dataModel: ClassifyDataModel) {
        mIComicClassifyView.showClassifyCountry(dataModel.countries)
        mIComicClassifyView.showClassifyType(dataModel.type)
        mIComicClassifyView.showClassifyTime(dataModel.time)
        mIComicClassifyView.showClassifyState(dataModel.state)
        mIComicClassifyView.showClassifySize(dataModel.size)
    }

    private fun showComicsSource(dataModel: List<IModel.IComicModel>) {
        if (mPage > 1) {
            mIComicClassifyView.showMoreComicList(dataModel)
            return
        }
        mIComicClassifyView.showComicList(dataModel)
    }
}