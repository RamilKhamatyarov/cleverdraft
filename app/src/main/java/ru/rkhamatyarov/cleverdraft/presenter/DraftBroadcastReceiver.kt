package ru.rkhamatyarov.cleverdraft.presenter

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import ru.rkhamatyarov.cleverdraft.R
import java.util.*

/**
 * Created by RKhamatyarov on 03.11.2017.
 */
class DraftBroadcastReceiver: BroadcastReceiver() {
    private val TAG = javaClass.simpleName
    private val ONE_TIME: String = "ONETIME"
    private val ADAPTER_POSITION: String = "ADAPTER_POSITION"
    private val LAYOUT_POSITION: String = "LAYOUT_POSITION"

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Context of receiving broadcast message from system by alarm manager: $context")


        //TODO: extracting note positions from intent and open note by draft list presenter maybe datetime picker presenter
        val notificationManager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //See working of this mechanism
        val intent = Intent(context, MainPresenter::class.java)
        val contentIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context)
        val notification: Notification = builder.setContentIntent(contentIntent).
                setSmallIcon(R.drawable.ic_alarm_white_48dp).
                setTicker("DateTime appname").setAutoCancel(true).
                setContentTitle("Content name in receiving broadcast message from system by alarm manager").build()

        notificationManager.notify(0, notification)

        //TODO: understand where calling and remove
        cancelAlarm(context)
    }

    fun setAlarm(context: Context,
                 date: Date,
                 noteAdapterPosition: Int,
                 noteLayoutPosition: Int) {

        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DraftBroadcastReceiver::class.java)
        intent.putExtra(ONE_TIME, false)
        intent.putExtra(ADAPTER_POSITION, noteAdapterPosition)
        intent.putExtra(LAYOUT_POSITION, noteLayoutPosition)

        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        Log.d(TAG, "DateTime when setting alarm by datetime picker presenter: ${date.time}")
        Log.d(TAG, "Context when setting alarm by datetime picker presenter: $context")
        Log.d(TAG, "AlarmManager when setting alarm by datetime picker presenter: $alarmManager")

        //Repeating 30 seconds
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, date.time, 1000 * 5, pendingIntent)
    }

    fun cancelAlarm(context: Context) {
        val intent: Intent = Intent(context, DraftBroadcastReceiver::class.java)
        val sender: PendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }

    fun setOneTimer(context:Context) {
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent: Intent = Intent(context, DraftBroadcastReceiver::class.java)
        intent.putExtra(ONE_TIME, false)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent)

    }
}