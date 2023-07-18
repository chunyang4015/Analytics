package com.anso.lib.analytics

import com.anso.lib.analytics.db.EventTable

/**
 * 通过List上传数据
 */
interface IArrayService {
    suspend fun fromArray(list: Array<EventTable>, action: (Long) -> Unit)
}

/**
 * 遍历List数据上传
 */
interface IAllService {
    suspend fun send(list: Array<EventTable>): Boolean
}