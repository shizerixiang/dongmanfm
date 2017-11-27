package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.ComicTimeContract
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 时间表数据操作类
 * Created by shize on 2017/10/24.
 */
class ComicTimePresenter constructor(private val mIComicTimeView: ComicTimeContract.IComicTimeView,
                                     private val mIComicTimeDataSource: ComicTimeContract.IComicTimeDataSource)
    : ComicTimeContract.IComicTimePresenter {
    private var mWeekday: String = "" // 平日

    init {
        mIComicTimeView.setPresenter(this)
    }

    override fun startLoad() {
        if (!NetworkHelper.isNetworkAvailable(mIComicTimeView.getViewContext())) {
            mIComicTimeView.showNoNetwork()
            return
        }
        loadSource()
    }

    override fun refreshPage() {
        mIComicTimeDataSource.refreshData()
        getWeekDayComics(mWeekday)
    }

    override fun getWeekDayComics(weekday: String) {
        if (!NetworkHelper.isNetworkAvailable(mIComicTimeView.getViewContext())) {
            mIComicTimeView.showNoNetwork()
            return
        }
        getComics(weekday)
    }

    /**
     * 预加载数据
     */
    private fun loadSource() {
        doAsync {
            mIComicTimeDataSource.getData(object : BaseDataSource.LoadSourceCallback<List<Map<String,String>>> {
                override fun onDataLoaded(dataModel: List<Map<String,String>>) {
                    uiThread {
                        if (mIComicTimeView.isActive())
                            mIComicTimeView.showWeekDays(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIComicTimeView.isActive())
                            mIComicTimeView.showRefreshFailed()
                    }
                }
            })
        }
    }

    /**
     * 加载新番列表
     */
    private fun getComics(weekday: String) {
        doAsync {
            mIComicTimeDataSource.getWeekDayComics(weekday, object : BaseDataSource.LoadSourceCallback<List<IModel.IComicModel>> {
                override fun onDataLoaded(dataModel: List<IModel.IComicModel>) {
                    uiThread {
                        if (mIComicTimeView.isActive())
                            mIComicTimeView.showComicList(dataModel, weekday)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIComicTimeView.isActive())
                            mIComicTimeView.showLoadComicsFailed()
                    }
                }
            })
        }
    }
}