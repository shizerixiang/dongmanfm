package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.ComicCourierContract
import fm.dongman.modellib.source.ComicCourierRepository
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 新番速递数据操作类
 * Created by shize on 2017/10/19.
 */
class ComicCourierPresenter constructor(private val mIComicCourierView: ComicCourierContract.IComicCourierView,
                                        private val mIComicCourierDataSource: ComicCourierContract.IComicCourierDataSource)
    : ComicCourierContract.IComicCourierPresenter {

    private var mQuarter: String = "" // 季度
    private var mOrder = ComicCourierRepository.ORDER_HOT // 排序方式，默认最热排序

    init {
        mIComicCourierView.setPresenter(this)
    }

    override fun startLoad() {
        if (!NetworkHelper.isNetworkAvailable(mIComicCourierView.getViewContext())) {
            mIComicCourierView.showNoNetwork()
            return
        }
        loadSource()
    }

    override fun refreshPage() {
        mIComicCourierDataSource.refreshData()
        quarterComic(mQuarter, mOrder)
    }

    override fun quarterComic(quarter: String, order: String) {
        mQuarter = quarter
        mOrder = order
        if (!NetworkHelper.isNetworkAvailable(mIComicCourierView.getViewContext())) {
            mIComicCourierView.showNoNetwork()
            return
        }
        getQuarterComics()
    }

    /**
     * 预加载数据
     */
    private fun loadSource() {
        doAsync {
            mIComicCourierDataSource.getData(object : BaseDataSource.LoadSourceCallback<List<Map<String,String>>> {
                override fun onDataLoaded(dataModel: List<Map<String,String>>) {
                    uiThread {
                        if (mIComicCourierView.isActive())
                            mIComicCourierView.showQuarters(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIComicCourierView.isActive())
                            mIComicCourierView.showRefreshFailed()
                    }
                }
            })
        }
    }

    /**
     * 获取季度新番
     */
    private fun getQuarterComics() {
        doAsync {
            val quarter = mQuarter
            mIComicCourierDataSource.getComicList(mQuarter, mOrder, object : BaseDataSource.LoadSourceCallback<List<IModel.IComicModel>> {
                override fun onDataLoaded(dataModel: List<IModel.IComicModel>) {
                    uiThread {
                        if (mIComicCourierView.isActive())
                            mIComicCourierView.showComicList(dataModel, quarter)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIComicCourierView.isActive())
                            mIComicCourierView.showRefreshFailed()
                    }
                }
            })
        }
    }
}