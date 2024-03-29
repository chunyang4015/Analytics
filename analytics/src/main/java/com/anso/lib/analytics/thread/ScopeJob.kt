package com.anso.lib.analytics.thread

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

object ScopeJob {
    private val channel = Channel<suspend () -> Unit>()

    init {
        scope.launch {
            channel.consumeEach { it.invoke() }
        }
    }

    /**
     * 添加任务到队列中
     */
    @JvmStatic
    fun enqueue(block: suspend () -> Unit) {
        scope.launch { channel.send(block) }
    }

    @JvmStatic
    fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch(block = block)
    }

    @JvmStatic
    fun <T> async(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return scope.async(block = block)
    }
}