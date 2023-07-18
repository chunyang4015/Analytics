package com.anso.lib.analytics.db

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "Event")
data class EventTable(
    val sid: String,//APP 生命周期唯一id
    val type: String,//类型 [EVENT] [SCREEN]
    val name: String,//名称
    val el: String,//事件标签
    val time: Long,
    val msg: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

}