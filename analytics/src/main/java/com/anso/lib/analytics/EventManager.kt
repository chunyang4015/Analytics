package com.anso.lib.analytics

import android.app.Application
import android.content.Context
import com.anso.lib.analytics.db.EventDatabase
import com.anso.lib.analytics.db.EventTable
import com.anso.lib.analytics.push.APushService
import com.anso.lib.analytics.utils.ALogger

object EventManager {

    var config = EventConfig()

    /**
     * 通过List上传数据
     */
    var netService: INetService? = null

    /**
     * 间隔时间
     */
    val intervalTime
        get() = config.intervalTime

    /**
     * 延迟时间
     */
    val delayTime: Long
        get() = config.delayTime

    val isDebug: Boolean
        get() = config.isDebug

    /**
     * 设置上传触发边界
     */
    val pushMaxCount: Int
        get() = config.pushMaxCount

    @JvmStatic
    fun init(
        context: Context,
    ) = apply {
        EventDatabase.init(context)
        APushService.startService()
    }

    private fun registerActivity(app: Application) {
        app.registerActivityLifecycleCallbacks(ActivityScreenImpl())
    }

    /**
     * 调用网络接口发送数据
     */
    suspend fun netService(list: Array<EventTable>, action: suspend (Boolean, List<Long>) -> Unit) {
        try {
            val successList = mutableListOf<Long>()
            var success = false
            if (list.isNotEmpty()) {
                netService?.push(list, { success = it }, { successList.add(it) })
                action.invoke(success, successList)
            }
        } catch (e: Exception) {
            ALogger.logError("发送数据失败~")
        }
    }

}