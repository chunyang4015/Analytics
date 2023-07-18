package com.anso.lib.analytics.thread

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


/**
 * 默认线程池
 */
internal object PoolExecutor : ThreadPoolExecutor(
    2, Runtime.getRuntime().availableProcessors() + 1, 30,
    TimeUnit.SECONDS,
    LinkedBlockingQueue(),
    DefaultThreadFactory()
) {

}