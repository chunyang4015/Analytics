package com.anso.lib.analytics.push

import com.anso.lib.analytics.EventManager
import com.anso.lib.analytics.db.EventDB
import com.anso.lib.analytics.thread.ScopeJob
import com.anso.lib.analytics.utils.ALogger
import java.util.Timer
import java.util.TimerTask

/**
 * 定时推送服务
 */
internal object APushService {

    private var timer = Timer()
    private var task: TimerTask? = null
    private var stopService = false
    private var isStartService = false
    private var time: Long = 0L

    /**
     * 启动定时服务
     * 通过[EventManager.delayTime] 与 [EventManager.intervalTime] 控制事件间隔
     */
    @JvmStatic
    fun startService() {
        //如果启动了则不再重复启动
        if (isStartService) return
        if (stopService) {
            timer = Timer()
            stopService = false
        }
        var task = task
        if (task == null) {
            task = object : TimerTask() {
                override fun run() {
                    ScopeJob.launch {
                        ALogger.logWrite("触发定时任务")
                        push()
                    }
                }
            }
        }
        APushService.task = task
        timer.schedule(task, EventManager.delayTime, EventManager.intervalTime)
        isStartService = true
    }

    /**
     * 停止定时服务
     */
    @JvmStatic
    fun stopService() {
        task?.cancel();task = null
        timer.cancel()
        stopService = true
        isStartService = false
    }

    /**
     * 及时推送上传数据
     */
    @JvmStatic
    suspend fun push(action: ((Int) -> Unit)? = null) {
        time = System.currentTimeMillis()
        val list = EventDB.getLastByTime(time)
        if (EventManager.allSend) {
            if (EventManager.send(list)) {
                EventDB.delListByTime(time)
                action?.invoke(list.size)
            }
        } else if (EventManager.fromArray) {
            EventManager.fromArray(list) {
                EventDB.delById(it)
                if (list.size == it.size) {
                    action?.invoke(list.size)
                }
            }
        }
    }
}