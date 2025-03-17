package com.almax.simpleforegroundservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlin.concurrent.thread

class ForegroundService : Service() {

    private var isRunning: Boolean = false

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "------service created------")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "------service started------")
        thread(start = true) {
            var counter = 0
            isRunning = true
            while (isRunning && counter < 10) {
                Log.d(TAG, "Counter: $counter")
                counter += 1
                Thread.sleep(2000)
            }

            if (counter == 10) {
                Log.d(TAG, "------service completed task execution------")
                stopSelf()
            }
        }
        startForegroundService()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        createNotificationChannel()
        val notification = createNotification()
        startForeground(1, notification)
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    private fun createNotificationChannel(): NotificationChannel {
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager =
            ContextCompat.getSystemService(this, NotificationManager::class.java)
        notificationManager!!.createNotificationChannel(channel)
        return channel
    }

    private fun createNotification(): Notification {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentText("Service running")
            .setContentTitle("Foreground Service")
            .setContentIntent(getPendingIntent())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()
        return notification
    }

    override fun onDestroy() {
        Log.d(TAG, "------service stopped------")
        super.onDestroy()
        isRunning = false
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

const val CHANNEL_ID = "id"
const val CHANNEL_NAME = "name"
const val TAG = "ForegroundServiceTAG"