package com.example.weathermobile

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.util.*

class NotificationTimerService : Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    private val timer = Timer()
    private val notificationId = 101
    private val CHANNEL_ID = "0"
    private val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
    private lateinit var testing : String



    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
    {
        val time = intent.getDoubleExtra(TIME_EXTRA, 0.0)
        timer.scheduleAtFixedRate(TimeTask(time),0,1000)
        testing = intent.getStringExtra("test").toString()
        startForeground(notificationId, sendNotification())
        return START_NOT_STICKY

    }

    fun sendNotification(): Notification {
        return mBuilder
            .setSmallIcon(R.drawable.sun)
            .setContentTitle("WeatherMobile")
            .setContentText(testing)
            //.setStyle(
            //  NotificationCompat.BigTextStyle().bigText("HEY LOOK AT ME THIS IS " +
            //        "SUPPOSED TO BE A LOT OF TEXT LOREM IPSUM"))
            //.setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOnlyAlertOnce(true)
            .build()


    }




    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }

    private inner class TimeTask(private var time: Double) : TimerTask()
    {
        override fun run()
        {
            val intent = Intent(TIMER_UPDATED)
            time++
            intent.putExtra(TIME_EXTRA, time)
            sendBroadcast(intent)
        }
    }


    companion object
    {
        const val TIMER_UPDATED = "timerUpdated"
        const val TIME_EXTRA = "timerExtra"
    }
}