package ru.rkhamatyarov.cleverdraft.presenter

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import ru.rkhamatyarov.cleverdraft.R
import ru.rkhamatyarov.cleverdraft.view.MainActivity
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by RKhamatyarov on 03.11.2017.
 */
class Alarm: BroadcastReceiver() {
    private val TAG = javaClass.simpleName
    private val ONE_TIME: String = "ONETIME"

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Alarm: $context")

        val notificationManager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val intent: Intent = Intent(context, MainActivity::class.java)
        val contentIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context)
        val notification: Notification = builder.setContentIntent(contentIntent).
                setSmallIcon(R.drawable.ic_alarm_white_48dp).
                setTicker("DateTime appname").setAutoCancel(true).
                setContentTitle("Content name").build()

        notificationManager.notify(0, notification)

        //TODO: understand where calling and remove
        cancelAlarm(context)
    }

    fun setAlarm(context: Context, date: Date) {
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, Alarm::class.java)
        intent.putExtra(ONE_TIME, false)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        Log.d(TAG, "DateTime: ${date.time}")
        Log.d(TAG, "Context: $context")
        Log.d(TAG, "alarmManager: $alarmManager")

        //Repeating 30 seconds
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, date.time, 1000*5, pendingIntent)
    }

    fun cancelAlarm(context: Context) {
        val intent: Intent = Intent(context, Alarm::class.java)
        val sender: PendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }

    fun setOneTimer(context:Context) {
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent: Intent = Intent(context, Alarm::class.java)
        intent.putExtra(ONE_TIME, false)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent)

    }
}