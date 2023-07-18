package com.anso.lib.analytics.thread

import com.anso.lib.analytics.utils.ALogger
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class DefaultThreadFactory : ThreadFactory {
    private val poolNumber = AtomicInteger(1)
    private val threadNumber = AtomicInteger(1)

    private val group: ThreadGroup? = Thread.currentThread().threadGroup
    private val namePrefix = "AEvent pool No. ${poolNumber.getAndIncrement()}"

    override fun newThread(r: Runnable?): Thread {
        val threadName = namePrefix + threadNumber.getAndIncrement()
        val thread = Thread(group, r, threadName, 0)
        if (thread.isDaemon) {   //设为非后台线程
            thread.isDaemon = false
        }
        if (thread.priority != Thread.NORM_PRIORITY) { //优先级为normal
            thread.priority = Thread.NORM_PRIORITY
        }
        // 捕获多线程处理中的异常
        thread.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { thread, ex ->
            ALogger.logError(
                "Running task appeared exception! Thread [" + thread.name + "], because [" + ex.message + "]"
            )
        }
        return thread
    }
}