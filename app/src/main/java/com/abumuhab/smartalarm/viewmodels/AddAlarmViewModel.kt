package com.abumuhab.smartalarm.viewmodels

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abumuhab.smartalarm.R
import com.abumuhab.smartalarm.models.AlarmSound
import java.util.*
import kotlin.collections.ArrayList

class AddAlarmViewModel(private val application: Application) : ViewModel() {
    private val _isAM = MutableLiveData<Boolean>()
    val isAM: LiveData<Boolean>
        get() = _isAM
    var days = ArrayList<String>()
    var minute: Number
    var hour: Number
    private var alarmSounds: Map<String, Int>

    private val alarmSound = MutableLiveData<Int>()
    val alarmName = MutableLiveData<String>()

    init {
        _isAM.value = true
        Calendar.getInstance().apply {
            minute = this.get(Calendar.MINUTE)
            hour = this.get(Calendar.HOUR)
            if (hour.toInt() > 12) {
                hour = hour.toInt() - 12
            }
        }

        alarmSounds =
            mapOf(
                application.getString(R.string.default_sound) to R.raw.buzzer,
                application.getString(R.string.clock_sound) to R.raw.clock,
                application.getString(R.string.rooster_sound) to R.raw.rooster
            )

        alarmName.value = "Default"
        alarmSound.value = alarmSounds[alarmName.value]
    }

    fun setAM(value: Boolean) {
        _isAM.value = value
    }

    fun addToSelectedDays(day: String) {
        days.add(day)
        val modified = days.distinct()
        days = arrayListOf()
        days.addAll(modified)
    }

    fun removeFromSelectedDays(day: String) {
        days.remove(day)
    }

    fun setAlarmSound(sound: String) {
        alarmName.value = sound
        alarmSound.value = alarmSounds[sound]
    }

    fun playSelectedAlarmSound() {
        val mediaPlayer =
            MediaPlayer.create(application.applicationContext, alarmSound.value!!)
        mediaPlayer.start()
    }
}