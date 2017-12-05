package fm.dongman.animefm.contractlib.contract

import fm.dongman.animefm.contractlib.BaseDataSource
import fm.dongman.animefm.contractlib.BasePresenter
import fm.dongman.animefm.contractlib.BaseView
import fm.dongman.animefm.contractlib.model.VideoSideTagModel

/**
 * 视频播放契约接口
 * Created by shize on 2017/12/5.
 */
interface VideoPlayContract {
    /**
     * 视频播放页面
     */
    interface IVideoPlayView:BaseView<IVideoPlayPresenter>{
        /**
         * 显示视频网站对应的控制器标签名
         * @param tags 控制器标签对象
         */
        fun showVideoSideTag(tags: List<VideoSideTagModel>)
    }

    /**
     * 视频播放数据操作
     */
    interface IVideoPlayPresenter:BasePresenter

    /**
     * 视频播放数据获取
     */
    interface IVideoPlayDataSource:BaseDataSource<List<VideoSideTagModel>>
}