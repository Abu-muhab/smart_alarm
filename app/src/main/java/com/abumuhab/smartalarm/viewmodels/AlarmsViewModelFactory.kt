package com.abumuhab.smartalarm.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abumuhab.smartalarm.database.AlarmDao

class AlarmsViewModelFactory (private val application: Application, private val alarmDao: AlarmDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmsViewModel::class.java)) {
            return AlarmsViewModel(application,alarmDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}