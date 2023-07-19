package com.anso.lib.analytics

import com.anso.lib.analytics.db.EventTable
interface INetService {
    suspend fun push(
        list: Array<EventTable>,
        all: ((Boolean) -> Unit)? = null,
        fromArray: ((Long) -> Unit)? = null
    )
}