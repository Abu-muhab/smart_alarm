package com.abumuhab.smartalarm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddAlarmViewModel :ViewModel() {
    private val _isAM = MutableLiveData<Boolean>()
    val isAM: LiveData<Boolean>
        get()=_isAM

    init {
        _isAM.value=true
    }

    fun setAM(value: Boolean){
        _isAM.value = value
    }
}