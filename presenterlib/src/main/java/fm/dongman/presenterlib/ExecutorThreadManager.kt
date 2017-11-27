package fm.dongman.presenterlib

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 线程管理器
 * Created by shize on 2017/10/10.
 */
class ExecutorThreadManager private constructor() {
    // 根据 CPU 给线程池分配线程
    private var mExecutorService: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    /**
     * 提交线程任务
     * @param runnable 线程任务
     */
    fun submitThread(runnable:Runnable){
        mExecutorService.submit(runnable)
    }

    companion object {
        private var MANAGER_INSTANCE: ExecutorThreadManager? = null // 管理器对象

        /**
         * 获取线程管理器
         * @return 线程管理器
         */
        fun getInstance(): ExecutorThreadManager {
            if (MANAGER_INSTANCE == null) {
                MANAGER_INSTANCE = ExecutorThreadManager()
            }
            return MANAGER_INSTANCE!!
        }
    }
}