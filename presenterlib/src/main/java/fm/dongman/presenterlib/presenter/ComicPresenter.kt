package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.ComicContract
import fm.dongman.animefm.contractlib.model.ComicDataModel
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 漫圈数据操作
 * Created by shize on 2017/10/15.
 */
class ComicPresenter constructor(private val mComicView: ComicContract.IComicView,
                                 private val mComicSource: ComicContract.IComicDataSource)
    : ComicContract.IComicPresenter {

    init {
        mComicView.setPresenter(this)
    }

    override fun startLoad() {
        // 检测网络
        if (!NetworkHelper.isNetworkAvailable(mComicView.getViewContext())) {
            // 离线加载
            loadOffLineSource()
            return
        }
        // 正常加载
        loadSource()
    }

    override fun refreshPage() {
        mComicSource.refreshData()
        startLoad()
    }

    override fun changeGuessList() {
        if (!NetworkHelper.isNetworkAvailable(mComicView.getViewContext())) {
            mComicView.showRefreshFailed()
            return
        }
        loadGuessList()
    }

    /**
     * 加载数据
     */
    private fun loadSource() {
        doAsync {
            Thread.sleep(HomePresenter.SLEEP_TIME)
            mComicSource.getData(object : BaseDataSource.LoadSourceCallback<ComicDataModel> {
                override fun onDataLoaded(dataModel: ComicDataModel) {
                    uiThread {
                        if (mComicView.isActive())
                            showSource(dataModel)
                    }
                    // 保存数据
                    mComicSource.saveData(dataModel)
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mComicView.isActive())
                            mComicView.showRefreshFailed()
                    }
                    // 离线加载数据
                    loadOffLineSource()
                }
            })
        }
    }

    /**
     * 离线加载数据
     */
    private fun loadOffLineSource() {
        doAsync {
            Thread.sleep(HomePresenter.SLEEP_TIME)
            mComicSource.getData(object : BaseDataSource.LoadSourceCallback<ComicDataModel> {
                override fun onDataLoaded(dataModel: ComicDataModel) {
                    uiThread {
                        if (mComicView.isActive()) {
                            showSource(dataModel)
                            mComicView.showRefreshFailed()
                        }
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mComicView.isActive())
                            mComicView.showNoNetwork()
                    }
                }
            })
        }
    }

    /**
     * 加载猜你喜欢
     */
    private fun loadGuessList() {
        doAsync {
            mComicSource.getComicGuessLike(object : BaseDataSource.LoadSourceCallback<List<IModel.IComicModel>> {
                override fun onDataLoaded(dataModel: List<IModel.IComicModel>) {
                    uiThread {
                        if (mComicView.isActive())
                            mComicView.showGuessLikeComics(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mComicView.isActive())
                            mComicView.showRefreshFailed()
                    }
                }
            })
        }
    }

    /**
     * 显示数据
     */
    private fun showSource(dataModel: ComicDataModel) {
        mComicView.showQuarter(dataModel.quarter)
        mComicView.showNewComics(dataModel.newComics)
        mComicView.showNewComments(dataModel.newComment)
        mComicView.showRecommendComics(dataModel.recommendComics)
        mComicView.showGuessLikeComics(dataModel.guessLikeComics)
    }
}