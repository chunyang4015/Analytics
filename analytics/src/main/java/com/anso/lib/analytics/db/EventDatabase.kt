package com.anso.lib.analytics.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EventTable::class], version = 1)
abstract class EventDatabase : RoomDatabase() {

    abstract fun event(): EventDao

    companion object {
        lateinit var INSTANCE: EventDatabase

        @JvmStatic
        fun init(context: Context) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                EventDatabase::class.java,
                "analytics.db"
            ).build()
        }
    }
}