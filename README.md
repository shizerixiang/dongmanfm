## 更新说明
### v1.1.2：动漫详情添加了发送反馈和上传剧照功能
### 改动说明：
IComicInfoView 添加了新的两个接口方法：
```java
        /**
         * 显示反馈状态
         * 由 [IComicInfoPresenter.sendFeedback] 回调
         * @param boolean 是否发送反馈成功
         */
        fun showFeedbackState(boolean: Boolean)

        /**
         * 显示上传剧照状态
         * 由 [IComicInfoPresenter.updateStills] 回调
         * @param boolean 是否上传成功
         */
        fun showUpdateStillsState(boolean: Boolean)
```

IComicInfoPresenter 添加了新的两个接口方法：
```java
        /**
         * 发送反馈
         * 通过异步调用 [IComicInfoDataSource.sendFeedback] 获取数据
         * 并在 UI 线程中调用 [IComicInfoView.showFeedbackState] 显示数据
         * @param comicId 动漫id
         * @param feedback 反馈信息
         */
        fun sendFeedback(comicId: String, feedback:IModel.IFeedBackModel)

        /**
         * 上传剧照
         * 通过异步调用 [IComicInfoDataSource.updateStills] 获取数据
         * 并在 UI 线程中调用 [IComicInfoView.showUpdateStillsState] 显示数据
         * @param stills 剧照
         */
        fun updateStills(stills:Bitmap)
```

IComicInfoDataSource 添加了新的两个接口方法：
```java 
        /**
         * 发送反馈
         * @param comicId 动漫 id
         * @param feedback 反馈信息
         * @param callback 回调
         */
        fun sendFeedback(comicId:String, feedback: IModel.IFeedBackModel,callback: BaseDataSource.LoadSourceCallback<String>)

        /**
         * 上传剧照
         * @param stills 剧照
         * @param callback 回调
         */
        fun updateStills(stills: Bitmap,callback: BaseDataSource.LoadSourceCallback<String>)
```
