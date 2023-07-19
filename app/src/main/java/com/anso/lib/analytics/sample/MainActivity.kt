package com.anso.lib.analytics.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.anso.lib.analytics.AEvent
import com.anso.lib.analytics.EventConfig
import com.anso.lib.analytics.EventManager
import com.anso.lib.analytics.INetService
import com.anso.lib.analytics.db.EventTable
import com.anso.lib.analytics.utils.ALogger
import rxhttp.toAwait
import rxhttp.wrapper.param.RxHttp
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //初始化 SDK
        EventManager.init(this).apply {
            config = EventConfig(debug = true)
            netService = NetImpl()
        }
    }

    override fun onResume() {
        super.onResume()
        AEvent.screen(MainActivity::class.java.name, "show")
    }

    override fun onStop() {
        super.onStop()
        AEvent.screen(MainActivity::class.java.name, "dismiss")
    }

    /**
     * 接口具体实现
     */
    class NetImpl : INetService {
        private val all = true
        private val debug = true
        override suspend fun push(
            list: Array<EventTable>,
            all: ((Boolean) -> Unit)?,
            fromArray: ((Long) -> Unit)?
        ) {
            //如果接口支持直接上传则使用 all 方式
            if (this.all) {
                try {
                    if (debug) {
                        val builder = StringBuilder()
                        list.forEach {
                            builder.append(it.toString())
                        }
                        ALogger.logWrite("全部发送:$builder")
                    } else {
                        RxHttp.get("todo").add("list", list).toAwait<String>().await()
                        all?.invoke(true)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                list.forEach {
                    //此处也可以根据自己业务需求进行定制操作
                    val map = mutableMapOf<String, String>()
                    map["pt"] = it.name
                    map["en"] = it.el
                    map["msg"] = it.msg
                    if (debug) {
                        ALogger.logWrite("请求数据:$map")
                    } else {
                        try {
                            RxHttp.get("todo").addAll(map).toAwait<String>().await()
                            fromArray?.invoke(it.id)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    fun onEventClick(view: View) {
        for (index in 1..10) {
            AEvent.event("pt-main", "en:$index")
        }
    }

    fun onEventNowClick(view: View) {
        //点击事件
        AEvent.event("share_image", "click", mutableMapOf("imageName" to "xxx.png"), true)
    }
}