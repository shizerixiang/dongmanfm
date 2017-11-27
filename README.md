## 更新说明
### v1.1.3：漫单创建接口添加修改漫单信息功能、搜索接口添加清空历史记录功能
### 改动说明：

ISearchView 添加了新的接口方法：
 ```java
         /**
          * 显示清除历史搜索记录结果
          * 由 [ISearchPresenter.clearHistorySearch] 回调
          * @param boolean 是否清除成功
          */
         fun showClearHistoryState(boolean: Boolean)
 ```

ISearchPresenter 添加了新的接口方法：
 ```java
         /**
          * 清除历史搜索记录
          * 异步调用 [ISearchDataSource.clearHistorySearch] 清除数据
          * 并在主线程中调用 [ISearchView.showClearHistoryState] 显示清除结果
          */
         fun clearHistorySearch()
 ```

ISearchDataSource 添加了新的接口方法：
 ```java
         /**
          * 清除历史搜索记录
          * @param callback 清除回调
          */
         fun clearHistorySearch(callback: BaseDataSource.LoadSourceCallback<String>)
 ```

ISheetCreateView 添加了新的接口方法：
```java
        /**
         * 显示编辑漫单的信息
         * 由 [ISheetCreatePresenter.setEditSheetId] 回调
         * @param sheet 需要编辑的漫单数据
         */
        fun showEditSheetInfo(sheet:IModel.ISheetModel)

        /**
         * 显示编辑漫单状态
         * 由 [ISheetCreatePresenter.editSheet] 回调
         * @param boolean 是否成功修改漫单数据
         */
        fun showEditState(boolean: Boolean)
```

ISheetCreatePresenter 添加了新的接口方法：
```java
        /**
         * 设置编辑漫单的 id
         * 通过 设置漫单id [ISheetCreateDataSource.setEditSheetId] 再异步调用 [ISheetCreateDataSource.getData] 获取要编辑的漫单数据
         * 并在 UI 线程中调用 [ISheetCreateView.showEditSheetInfo] 显示漫单数据
         * @param sheetId 编辑漫单的 id
         */
        fun setEditSheetId(sheetId:String)

        /**
         * 编辑漫单
         * 通过异步调用 [ISheetCreateDataSource.editSheet] 获取数据
         * 并在 UI 线程中调用 [ISheetCreateView.showEditState] 显示数据
         * @param sheet 修改后的漫单
         */
        fun editSheet(sheet: IModel.ISheetModel)
```

ISheetCreateDataSource 修改了部分参数类型：
```java
    /**
     * 漫单数据源接口
     */
    interface ISheetCreateDataSource:BaseDataSource<IModel.IUserModel>{
        /**
         * 创建漫单
         * @param userId 用户 id
         * @param sheet 漫单
         * @param callback 获取信息回调接口
         */
        fun createSheet(userId: String, sheet: IModel.ISheetModel, callback: BaseDataSource.LoadSourceCallback<String?>)
```

ISheetCreateDataSource 添加了新的接口方法：
```java
        /**
         * 设置编辑漫单的 id
         * @param sheetId 漫单 id
         */
        fun setEditSheetId(sheetId: String)

        /**
         * 提交编辑漫单信息
         * @param sheet 编辑后的漫单
         * @param callback 获取信息回调接口
         */
        fun editSheet(sheet:IModel.ISheetModel, callback: BaseDataSource.LoadSourceCallback<String?>)
```
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
