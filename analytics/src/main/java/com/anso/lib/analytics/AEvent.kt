package com.anso.lib.analytics

import com.anso.lib.analytics.bean.EventData
import com.anso.lib.analytics.db.EventDB
import com.anso.lib.analytics.push.APushService
import com.anso.lib.analytics.thread.ScopeJob
import com.anso.lib.analytics.utils.ALogger
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

object AEvent {
    private val eventNum = AtomicInteger(0)

    private val sid: String = UUID.randomUUID().toString()

    /**
     * 自定义事件
     * @param name 事件名称 在屏幕事件中，可以使用 class name
     * @param el 事件标签
     * @param msg 自定义事件
     * @param sendNow 是否即可发送数据 true 即可发送
     */
    @JvmStatic
    fun event(
        name: String,
        el: String,
        msg: Map<String, String>? = null,
        sendNow: Boolean = false
    ) {
        ScopeJob.enqueue {
            //即可推送 或者 满足添加推送
            val send = sendNow || eventNum.incrementAndGet() >= EventManager.pushMaxCount
            val event = EventData(sid, "event", name, el, msg)
            //存储数据
            EventDB.save(event)
            //触发发送数据
            if (send) {
                push()
            }
        }
    }

    /**
     * 屏幕事件
     * @param name 事件名称 在屏幕事件中，可以使用 class name
     * @param el 事件标签
     * @param msg 自定义事件
     * @param sendNow 是否即可发送数据 true 即可发送
     */
    @JvmStatic
    fun screen(name: String, el: String, msg: Map<String, String>?, sendNow: Boolean = false) {
        ScopeJob.enqueue {
            //即可推送 或者 满足添加推送
            val send = sendNow || eventNum.incrementAndGet() >= EventManager.pushMaxCount
            val event = EventData(sid, "screen", name, el, msg)
            ALogger.logWrite("Event.Task $event")
            //存储数据
            EventDB.save(event)
            //触发发送数据
            if (send) {
                push()
            }
        }
    }

    private suspend fun push() {
        APushService.push {
            eventNum.addAndGet(-it)
        }
    }

    /**
     * 启动定时任务
     */
    fun startService() {
        APushService.startService()
    }

    /**
     * 停止定时任务
     */
    fun stopService() {
        APushService.stopService()
    }
}