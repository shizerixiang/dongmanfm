package fm.dongman.presenterlib.presenter

import android.graphics.Bitmap
import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.IModel
import fm.dongman.animefm.contractlib.contract.ComicInfoContract
import fm.dongman.animefm.contractlib.contract.MyContract
import fm.dongman.animefm.contractlib.model.ComicInfoDataModel
import fm.dongman.animefm.contractlib.model.ScoreModel
import fm.dongman.presenterlib.util.NetworkHelper
import fm.dongman.presenterlib.util.NetworkHelper.isNetworkAvailable
import fm.dongman.presenterlib.util.NetworkHelper.isUnLogged
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 动漫详情数据操作类
 * Created by shize on 2017/10/18.
 */
class ComicInfoPresenter constructor(private val mComicInfoView: ComicInfoContract.IComicInfoView,
                                     private val mIComicInfoDataSource: ComicInfoContract.IComicInfoDataSource)
    : ComicInfoContract.IComicInfoPresenter {

    private var mId: String = "" // 存储 id

    init {
        mComicInfoView.setPresenter(this)
    }

    override fun startLoad() {}

    override fun refreshPage() {
        mIComicInfoDataSource.refreshData()
        getComicInfo(mId)
    }

    override fun getComicInfo(id: String) {
        mId = id
        mIComicInfoDataSource.setComicId(id)
        if (!NetworkHelper.isNetworkAvailable(mComicInfoView.getViewContext())) {
            mComicInfoView.showNoNetwork()
            return
        }
        loadSource()
    }

    override fun followComic(comicId: String) {
        // 检测是否登录
        if (isUnLogged(mComicInfoView.getViewContext())) {
            mComicInfoView.showFollowFailed()
            return
        }
        follow(comicId)
    }

    override fun scoreComic(score: ScoreModel) {
        // 检测是否登录
        if (isUnLogged(mComicInfoView.getViewContext())) {
            mComicInfoView.showScoreFailed()
            return
        }
        score(score)
    }

    override fun getMySheets() {
        // 检测是否登录
        if (isUnLogged(mComicInfoView.getViewContext())) {
            mComicInfoView.showGetMySheetsFailed()
            return
        }
        loadMySheets()
    }

    override fun joinSheet(sheetId: String) {
        if (isUnLogged(mComicInfoView.getViewContext())) {
            mComicInfoView.showJoinSheetFailed()
            return
        }
        join(sheetId)
    }

    override fun sendFeedback(comicId: String, feedback: IModel.IFeedBackModel) {
        if (!isNetworkAvailable(mComicInfoView.getViewContext())) {
            mComicInfoView.showFeedbackState(false)
            return
        }
        sendComicFeedback(comicId, feedback)
    }

    override fun updateStills(stills: Bitmap) {
        if (!isNetworkAvailable(mComicInfoView.getViewContext())) {
            mComicInfoView.showUpdateStillsState(false)
            return
        }
        updatePic(stills)
    }

    /**
     * 加载数据
     */
    private fun loadSource() {
        doAsync {
            mIComicInfoDataSource.getData(object : BaseDataSource.LoadSourceCallback<ComicInfoDataModel> {
                override fun onDataLoaded(dataModel: ComicInfoDataModel) {
                    uiThread {
                        if (mComicInfoView.isActive())
                            showSource(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mComicInfoView.isActive())
                            mComicInfoView.showRefreshFailed()
                    }
                }
            })
        }
    }

    /**
     * 关注
     */
    private fun follow(comicId: String) {
        doAsync {
            mIComicInfoDataSource.followComic(MyContract.USER_ID!!, comicId, object : BaseDataSource.LoadSourceCallback<Void> {
                override fun onDataLoaded(dataModel: Void) {
                    uiThread {
                        if (mComicInfoView.isActive())
                            mComicInfoView.showFollowSucceed()
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mComicInfoView.isActive())
                            mComicInfoView.showFollowFailed()
                    }
                }
            })
        }
    }

    /**
     * 评分
     */
    private fun score(score: ScoreModel) {
        score.userId = MyContract.USER_ID
        doAsync {
            mIComicInfoDataSource.scoreComic(score, object : BaseDataSource.LoadSourceCallback<ScoreModel> {
                override fun onDataLoaded(dataModel: ScoreModel) {
                    uiThread {
                        if (mComicInfoView.isActive())
                            mComicInfoView.showScoreSucceed(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mComicInfoView.isActive())
                            mComicInfoView.showScoreFailed()
                    }
                }
            })
        }
    }

    /**
     * 加载我的漫单数据
     */
    private fun loadMySheets() {
        doAsync {
            mIComicInfoDataSource.obtainMySheets(MyContract.USER_ID!!, object : BaseDataSource.LoadSourceCallback<List<IModel.ISheetModel>> {
                override fun onDataLoaded(dataModel: List<IModel.ISheetModel>) {
                    uiThread {
                        if (mComicInfoView.isActive())
                            mComicInfoView.showMySheets(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mComicInfoView.isActive())
                            mComicInfoView.showGetMySheetsFailed()
                    }
                }
            })
        }
    }

    /**
     * 加入漫单
     */
    private fun join(sheetId: String) {
        mIComicInfoDataSource.setComicId(mId)
        doAsync {
            mIComicInfoDataSource.joinSheet(sheetId, object : BaseDataSource.LoadSourceCallback<Void> {
                override fun onDataLoaded(dataModel: Void) {
                    uiThread {
                        if (mComicInfoView.isActive())
                            mComicInfoView.showJoinSheetSucceed()
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mComicInfoView.isActive())
                            mComicInfoView.showJoinSheetFailed()
                    }
                }
            })
        }
    }

    /**
     * 发送动漫的反馈
     */
    private fun sendComicFeedback(comicId: String, feedback: IModel.IFeedBackModel) {
        doAsync {
            mIComicInfoDataSource.sendFeedback(comicId, feedback, object : BaseDataSource.LoadSourceCallback<String> {
                override fun onDataLoaded(dataModel: String) {
                    uiThread {
                        mComicInfoView.showFeedbackState(true)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        mComicInfoView.showFeedbackState(false)
                    }
                }
            })
        }
    }

    /**
     * 上传剧照图片
     */
    private fun updatePic(stills: Bitmap) {
        doAsync {
            mIComicInfoDataSource.updateStills(stills, object : BaseDataSource.LoadSourceCallback<String> {
                override fun onDataLoadFiled() {
                    uiThread {
                        mComicInfoView.showUpdateStillsState(false)
                    }
                }

                override fun onDataLoaded(dataModel: String) {
                    uiThread {
                        mComicInfoView.showUpdateStillsState(true)
                    }
                }
            })
        }
    }

    /**
     * 显示数据
     */
    private fun showSource(dataModel: ComicInfoDataModel) {
        if (dataModel.comicModel == null) {
            mComicInfoView.showRefreshFailed()
            return
        }
        mComicInfoView.showComicInfo(dataModel.comicModel!!)
        mComicInfoView.showRelatedArticle(dataModel.relatedArticles)
        mComicInfoView.showRelatedComics(dataModel.relatedComics)
        mComicInfoView.showRelatedSheets(dataModel.relatedSheets)
        mComicInfoView.showRelatedComments(dataModel.relatedComment)
    }
}