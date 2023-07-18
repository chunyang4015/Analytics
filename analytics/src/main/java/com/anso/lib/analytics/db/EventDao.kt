package com.anso.lib.analytics.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg data: EventTable)

    @Query("SELECT * FROM Event where time <:time  order by id")
    suspend fun getLastByTime(time: Long): Array<EventTable>

    @Query("DELETE FROM Event where time<:time")
    suspend fun delListByTime(time: Long)

    @Query("DELETE FROM Event where id==:id")
    fun delById(id: Long)
}