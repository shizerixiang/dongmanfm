package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.ComicListContract
import fm.dongman.animefm.contractlib.contract.MyContract
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 番剧列表数据操作类
 * Created by shize on 2017/10/19.
 */
class ComicListPresenter constructor(private val mIComicListView: ComicListContract.IComicListView,
                                     private val mIComicListDataSource: ComicListContract.IComicListDataSource)
    : ComicListContract.IComicListPresenter {

    private var mType: String = "" // 类型
    private var mKey: String? = null // 关键字 or id
    private var mPage: Int = 1 // 页数

    init {
        mIComicListView.setPresenter(this)
    }

    override fun startLoad() {}

    override fun refreshPage() {
        mIComicListDataSource.refreshData()
        setKey(mType, mKey, mPage)
    }

    override fun setKey(type: String, key: String?, page: Int) {
        mType = type
        mKey = key
        mPage = page
        loadSource()
    }

    /**
     * 预加载数据
     */
    private fun loadSource() {
        if (ComicListContract.IComicListPresenter.TYPE_SEARCH == mType) {
            searchComics()
            return
        }
        loadComics()
    }

    /**
     * 加载数据
     */
    private fun loadComics() {
        if (NetworkHelper.isUnLogged(mIComicListView.getViewContext())) {
            mIComicListView.showNoNetwork()
            return
        }
        doAsync {
            mIComicListDataSource.lookedComics(MyContract.USER_ID!!, mPage, object : BaseDataSource.LoadSourceCallback<List<IModel.IComicModel>> {
                override fun onDataLoaded(dataModel: List<IModel.IComicModel>) {
                    uiThread {
                        if (mIComicListView.isActive())
                            showComics(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIComicListView.isActive())
                            mIComicListView.showRefreshFailed()
                    }
                }
            })
        }
    }

    /**
     * 搜索数据
     */
    private fun searchComics() {
        if (!NetworkHelper.isNetworkAvailable(mIComicListView.getViewContext())) {
            mIComicListView.showNoNetwork()
            return
        }
        doAsync {
            mIComicListDataSource.searchComics(mKey!!, mPage, object : BaseDataSource.LoadSourceCallback<List<IModel.IComicModel>> {
                override fun onDataLoaded(dataModel: List<IModel.IComicModel>) {
                    uiThread {
                        if (mIComicListView.isActive())
                            showComics(dataModel)
                    }
                    mIComicListDataSource.saveData(mKey!!)
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIComicListView.isActive())
                            mIComicListView.showRefreshFailed()
                    }
                }
            })
        }
    }

    private fun showComics(dataModel: List<IModel.IComicModel>) {
        if(mPage == 1){
            mIComicListView.showComicList(dataModel)
            return
        }
        mIComicListView.showMoreComicList(dataModel)
    }
}