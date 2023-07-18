package com.anso.lib.analytics.utils

import android.util.Log
import com.anso.lib.analytics.EventManager

/**
 * 日志输出
 */
object ALogger {

    private const val TAG = "ALogger"

    @JvmStatic
    fun logWrite(message: String?) {
        if (!EventManager.isDebug) return
        if (message == null) {
            Log.d(TAG, "null")
            return
        }
        Log.d(TAG, message)
    }

    @JvmStatic
    fun logError(message: String?) {
        if (!EventManager.isDebug) return
        if (message == null) {
            Log.e(TAG, "null")
            return
        }
        Log.e(TAG, message)
    }
}