package com.abumuhab.smartalarm.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.abumuhab.smartalarm.models.Alarm

@Dao
interface AlarmDao {
    @Insert
    suspend fun insert(alarm: Alarm)

    @Delete
    suspend fun delete(alarm: Alarm)

    @Update
    suspend fun update(alarm: Alarm)

    @Query("SELECT * FROM alarms")
    fun getAlarms(): LiveData<List<Alarm>>
}