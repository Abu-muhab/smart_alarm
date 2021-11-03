package com.abumuhab.smartalarm.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.util.Log
import android.view.KeyEvent
import androidx.core.content.ContextCompat
import com.abumuhab.smartalarm.services.MediaPlaybackService

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        var mediaBrowser: MediaBrowserCompat? = null
        var connectionCallBack: MediaBrowserCompat.ConnectionCallback? = null

        if (intent.action == "com.abumuhab.alarm.action.START_ALARM") {
            createNotificationChannel(context.applicationContext)
            Log.i("UUID_ALARM", intent.getStringExtra("id").toString())
            connectionCallBack = object : MediaBrowserCompat.ConnectionCallback() {
                override fun onConnected() {
                    mediaBrowser!!.sessionToken.also { token ->
                        val mediaController =
                            MediaControllerCompat(context.applicationContext, token)
                        mediaController.transportControls.playFromMediaId(
                            intent.getStringExtra("id").toString(), null
                        )
                    }
                }
            }
        } else if (intent.action == "android.intent.action.MEDIA_BUTTON") {
            val keyEvent = intent.extras!!.get(Intent.EXTRA_KEY_EVENT) as KeyEvent
            if (keyEvent.keyCode == KeyEvent.KEYCODE_MEDIA_STOP) {
                connectionCallBack = object : MediaBrowserCompat.ConnectionCallback() {
                    override fun onConnected() {
                        mediaBrowser!!.sessionToken.also { token ->
                            val mediaController =
                                MediaControllerCompat(context.applicationContext, token)
                            mediaController.transportControls.stop()
                        }
                    }
                }
            }
        }

        mediaBrowser = MediaBrowserCompat(
            context.applicationContext,
            ComponentName(context.applicationContext, MediaPlaybackService::class.java),
            connectionCallBack,
            null
        )
        mediaBrowser.connect()
    }
}

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "chat channel"
        val descriptionText = "messages channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("alarm", name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            ContextCompat.getSystemService(
                context.applicationContext,
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}