package com.abumuhab.smartalarm.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddAlarmViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddAlarmViewModel::class.java)) {
            return AddAlarmViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}