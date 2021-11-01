package com.abumuhab.smartalarm.viewmodels

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abumuhab.smartalarm.R
import java.lang.Exception
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
    var alarmVolume = 0.5F

    var mediaPlayer: MediaPlayer? = null
    var mediaPlaying = MutableLiveData<Boolean>()

    var addVibration = MutableLiveData<Boolean>()
    private var vibrator: Vibrator


    init {
        _isAM.value = true
        mediaPlaying.value = false
        addVibration.value = false

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

        alarmName.value = application.getString(R.string.default_sound)
        alarmSound.value = alarmSounds[alarmName.value]

        vibrator = application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
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
        stopAlarmSound()
        if (addVibration.value == true) {
            vibrator.vibrate(
                VibrationEffect.createWaveform(
                    longArrayOf(600, 400, 600, 400),
                    intArrayOf(
                        VibrationEffect.DEFAULT_AMPLITUDE,
                        0,
                        VibrationEffect.DEFAULT_AMPLITUDE,
                        0
                    ), 0
                )
            )
        }
        mediaPlayer =
            MediaPlayer.create(application.applicationContext, alarmSound.value!!)
        mediaPlayer?.apply {
            setVolume(alarmVolume, alarmVolume)
            start()
            mediaPlaying.value = true
            setOnCompletionListener {
                it.release()
                mediaPlaying.value = false
                vibrator.cancel()
            }
        }
    }

    fun changeAlarmVolume(volume: Float) {
        alarmVolume = volume
        try {
            mediaPlayer?.setVolume(alarmVolume, alarmVolume)
        } catch (e: Exception) {
        }
    }

    fun stopAlarmSound() {
        mediaPlayer?.apply {
            try {
                vibrator.cancel()
                this.stop()
                this.release()
                mediaPlaying.value = false
            } catch (e: Exception) {
            }
        }
    }
}