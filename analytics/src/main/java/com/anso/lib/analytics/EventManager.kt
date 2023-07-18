package com.anso.lib.analytics

import android.content.Context
import androidx.annotation.Keep
import com.anso.lib.analytics.db.EventDatabase
import com.anso.lib.analytics.db.EventTable
import com.anso.lib.analytics.push.APushService
import com.anso.lib.analytics.utils.ALogger

@Keep
object EventManager {

    var config = EventConfig()

    /**
     * 两种接口上传方式，由于安速这面，没有all方式，因此特意制作两种上传方式
     */
    /**
     * 通过List上传数据
     */
    var allService: IAllService? = null

    /**
     * 遍历List数据上传
     */
    var arrayService: IArrayService? = null
    val allSend: Boolean
        get() = allService != null

    val fromArray: Boolean
        get() = arrayService != null

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

    /**
     * 调用网络接口发送数据
     */
    suspend fun send(list: Array<EventTable>): Boolean {
        return try {
            if (list.isNotEmpty()) {
                allService?.send(list) ?: false
            } else {
                true
            }
        } catch (e: Exception) {
            ALogger.logError("发送数据失败~")
            false
        }
    }

    /**
     * 调用网络接口发送数据
     */
    suspend fun fromArray(list: Array<EventTable>, action: suspend (List<Long>) -> Unit) {
        val successList = mutableListOf<Long>()
        try {
            if (list.isNotEmpty()) {
                arrayService?.fromArray(list) {
                    successList.add(it)
                }
                action.invoke(successList)
            }
        } catch (e: Exception) {
            ALogger.logError("发送数据失败~")
        }
    }

}