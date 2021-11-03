package com.abumuhab.smartalarm.services


import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.abumuhab.smartalarm.R
import com.abumuhab.smartalarm.database.AlarmDatabase
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.concurrent.thread
import kotlin.coroutines.coroutineContext

class MediaPlaybackService : MediaBrowserServiceCompat() {
    private var mediaSession: MediaSessionCompat? = null
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    private lateinit var stateBuilder: PlaybackStateCompat.Builder
    override fun onCreate() {
        super.onCreate()

        mediaSession = MediaSessionCompat(baseContext, "ALARM_MEDIA_SESSION").apply {
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PAUSE)
            setPlaybackState(stateBuilder.build())

            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
                    val builder =
                        NotificationCompat.Builder(applicationContext, "alarm").apply {
                            setOngoing(true)
                            setContentTitle("New alarm")
                            setSmallIcon(R.drawable.alarm)
                            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        }
                    startForeground(-8000, builder.build())
                    this@MediaPlaybackService.startService(
                        Intent(
                            applicationContext,
                            MediaPlaybackService::class.java
                        )
                    )

                    // stop media player if active before sounding another alarm
                    mediaPlayer?.apply {
                        try {
                            stop()
                            release()
                            vibrator?.cancel()

                        } catch (e: Exception) {
                            //do nothing. This just means we attempted to stop the player while nothing was being played
                        }
                    }
                    val job = SupervisorJob()
                    val scope = CoroutineScope(Dispatchers.Main + job)
                    scope.launch {
                        val alarmDao = AlarmDatabase.getInstance(applicationContext).alarmDao

                        val alarm = alarmDao.getAlarm(mediaId.toString())

                        alarm?.let {
                            val builder =
                                NotificationCompat.Builder(applicationContext, "alarm").apply {
                                    setOngoing(true)
                                    setContentTitle(alarm.name)
                                    setSmallIcon(R.drawable.alarm)

                                    setDeleteIntent(
                                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                                            applicationContext,
                                            PlaybackStateCompat.ACTION_STOP
                                        )
                                    )

                                    setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                                    addAction(
                                        R.drawable.ic_baseline_delete_outline_24,
                                        "Stop",
                                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                                            applicationContext,
                                            PlaybackStateCompat.ACTION_STOP
                                        )
                                    )
                                }
                            startForeground(-8000, builder.build())

                            //add vibration if set on the alarm
                            if (alarm.vibration) {
                                vibrator =
                                    application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                                vibrator!!.vibrate(
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
                                MediaPlayer.create(application.applicationContext, alarm.sound)
                                    .apply {
                                        start()
                                        setOnCompletionListener {
                                            start()
                                        }
                                    }
                        }
                    }
                }

                override fun onStop() {
                    mediaPlayer?.apply {
                        stop()
                        release()
                    }
                    vibrator?.cancel()
                    this@MediaPlaybackService.stopForeground(true)
                    this@MediaPlaybackService.stopSelf()
                }
            })

            setSessionToken(sessionToken)

            isActive = true
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot("root", null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        result.sendResult(null);
    }
}