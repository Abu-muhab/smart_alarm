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
import androidx.core.app.NotificationCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.abumuhab.smartalarm.R

class MediaPlaybackService : MediaBrowserServiceCompat() {
    private var mediaSession: MediaSessionCompat? = null
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    private lateinit var stateBuilder: PlaybackStateCompat.Builder
    override fun onCreate() {
        super.onCreate()

        mediaSession = MediaSessionCompat(baseContext, "GGG").apply {
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PAUSE)
            setPlaybackState(stateBuilder.build())

            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlay() {
                    this@MediaPlaybackService.startService(
                        Intent(
                            applicationContext,
                            MediaPlaybackService::class.java
                        )
                    )
                    val builder = NotificationCompat.Builder(applicationContext, "alarm").apply {
                        setOngoing(true)
                        setContentTitle("test")
                        setContentText("test")
                        setSubText("test")
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
                    vibrator = application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
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
                    mediaPlayer =
                        MediaPlayer.create(application.applicationContext, R.raw.buzzer).apply {
                            start()
                            setOnCompletionListener {
                                start()
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