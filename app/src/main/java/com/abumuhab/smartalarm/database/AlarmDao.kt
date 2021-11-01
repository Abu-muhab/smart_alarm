package com.abumuhab.smartalarm.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.abumuhab.smartalarm.models.Alarm

@Dao
interface AlarmDao {
    @Insert
    suspend fun insert(alarm: Alarm)

    @Delete
    suspend fun delete(alarm: Alarm)

    @Query("SELECT * FROM alarms")
    fun getAlarms(): LiveData<List<Alarm>>
}