package com.anso.lib.analytics.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.anso.lib.analytics.AEvent
import com.anso.lib.analytics.EventConfig
import com.anso.lib.analytics.EventManager
import com.anso.lib.analytics.IAllService
import com.anso.lib.analytics.IArrayService
import com.anso.lib.analytics.db.EventTable
import com.anso.lib.analytics.utils.ALogger

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val impl = NetImpl()
        EventManager.init(this).apply {
            config = EventConfig(debug = true)
            arrayService = impl
        }
    }

    class NetImpl : IAllService, IArrayService {

        override suspend fun send(list: Array<EventTable>): Boolean {
            val request = mutableListOf<String>()
            list.forEach {
                val map = mutableMapOf<String, String>()
                map["pt"] = it.name
                map["en"] = it.el
                map["msg"] = it.msg
                ALogger.logWrite("请求数据:$map")
                request.add(map.toString())
            }
            return true
        }

        override suspend fun fromArray(list: Array<EventTable>, action: (Long) -> Unit) {
            list.forEach {
                val map = mutableMapOf<String, String>()
                map["pt"] = it.name
                map["en"] = it.el
                map["msg"] = it.msg
                ALogger.logWrite("请求数据:$map")
                action.invoke(it.id)
            }
        }

    }

    fun onEventClick(view: View) {
        for (index in 1..10) {
            AEvent.event("pt-main", "en:$index")
        }

    }

    fun onEventNowClick(view: View) {
        for (index in 11..14) {
            AEvent.event("pt-main", "en:$index", sendNow = true)
        }
//        AEvent.event("pt-main", "en:15", sendNow = true)
    }
}