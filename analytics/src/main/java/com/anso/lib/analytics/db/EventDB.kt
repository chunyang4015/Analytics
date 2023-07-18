package com.anso.lib.analytics.db

import androidx.room.withTransaction
import com.anso.lib.analytics.bean.EventData
import com.anso.lib.analytics.utils.ALogger

internal object EventDB {

    private val db = EventDatabase.INSTANCE

    /**
     * 添加数据或者更新数据
     */
    suspend fun save(data: EventData) {
        val dao = data.db()
        db.event().insert(dao)
        ALogger.logWrite("添加数据或者更新数据:$dao")
    }

    /**
     * 获取指定时间前面数据
     */
    suspend fun getLastByTime(time: Long): Array<EventTable> {
        val list = db.event().getLastByTime(time)
        ALogger.logWrite("获取指定时间前面数据:$time 个数:${list.size}")
        return list
    }

    /**
     * 开始删除过期消息
     */
    suspend fun delListByTime(time: Long) {
        db.event().delListByTime(time)
        ALogger.logWrite("开始删除过期消息:$time")
    }

    /**
     * 开始删除消息
     */
    suspend fun delById(ids: List<Long>) {
        db.withTransaction {
            ids.forEach {
                db.event().delById(it)
                ALogger.logWrite("开始删除消息:$it")
            }
        }
    }

}