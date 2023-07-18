package com.anso.lib.analytics

data class EventConfig(
    private val debug: Boolean = false,
    private val interval: Int = 1,
    private val delay: Int = 1,
    private val maxCount: Int = 100
) {

    val intervalTime: Long
        get() = interval * 60 * 1000L

    val delayTime: Long
        get() = delay * 60 * 1000L

    val pushMaxCount: Int
        get() = maxCount

    val isDebug: Boolean
        get() = debug

}