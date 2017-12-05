package fm.dongman.presenterlib.presenter

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.contract.VideoPlayContract
import fm.dongman.animefm.contractlib.model.VideoSideTagModel
import fm.dongman.presenterlib.util.NetworkHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * 视频播放数据操作类
 * Created by shize on 2017/12/5.
 */
class VideoPlayPresenter(private val mIVideoPlayView: VideoPlayContract.IVideoPlayView,
                         private val mIVideoPlayDataSource: VideoPlayContract.IVideoPlayDataSource)
    : VideoPlayContract.IVideoPlayPresenter {

    init {
        mIVideoPlayView.setPresenter(this)
    }

    override fun startLoad() {
        if (!NetworkHelper.isNetworkAvailable(mIVideoPlayView.getViewContext())) {
            mIVideoPlayView.showNoNetwork()
            return
        }
        doAsync {
            mIVideoPlayDataSource.getData(object : BaseDataSource.LoadSourceCallback<List<VideoSideTagModel>> {
                override fun onDataLoaded(dataModel: List<VideoSideTagModel>) {
                    uiThread {
                        if (mIVideoPlayView.isActive())
                            mIVideoPlayView.showVideoSideTag(dataModel)
                    }
                }

                override fun onDataLoadFiled() {
                    uiThread {
                        if (mIVideoPlayView.isActive())
                            mIVideoPlayView.showRefreshFailed()
                    }
                }
            })
        }
    }

    override fun refreshPage() {
        mIVideoPlayDataSource.refreshData()
        startLoad()
    }
}