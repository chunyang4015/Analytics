package com.anso.lib.analytics.bean

import androidx.annotation.Keep
import com.anso.lib.analytics.db.EventTable
import org.json.JSONObject

@Keep
data class EventData(
    val sid: String,//APP 生命周期内唯一ID
    val type: String,//类型 [EVENT] [SCREEN]
    val name: String,//事件名称
    val el: String,//事件标签
    private val msg: Map<String, String>?//自定义参数
) {

    private val ecp: String
        get() {
            val jsonObject = JSONObject()
            msg?.forEach {
                jsonObject.put(it.key, it.value)
            }
            return jsonObject.toString()
        }

    /**
     * 转化成 DB 数据
     */
    fun db(): EventTable {
        return EventTable(sid, type, name, el, System.currentTimeMillis(), ecp)
    }

    override fun toString(): String {
        return "周期:$sid ,事件类型:$type ,事件名称:$name ,事件标签:$el, 自定义事件:$ecp"
    }
}