package com.abumuhab.smartalarm.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.abumuhab.smartalarm.database.AlarmDao
import com.abumuhab.smartalarm.models.Alarm

class AlarmsViewModel(private val application: Application, private val alarmDao: AlarmDao) :
    ViewModel() {
    var alarms: LiveData<List<Alarm>> = alarmDao.getAlarms()
}