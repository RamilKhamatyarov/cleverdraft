package ru.rkhamatyarov.cleverdraft.presenter

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.widget.Toast
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Asus on 03.11.2017.
 */
class Alarm: BroadcastReceiver() {
    private val ONE_TIME: String = "ONETIME"

    override fun onReceive(context: Context?, intent: Intent?) {
        val powerManager: PowerManager  = context?.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock: PowerManager.WakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CLVR_DR")
        wakeLock.acquire()

        val extras: Bundle? = intent?.getExtras()
        val msgSb: StringBuilder = StringBuilder()

        if (extras != null && extras.getBoolean(ONE_TIME, false)) {
            msgSb.append("Onetime timer - ")
        }
        val formatter: Format = SimpleDateFormat("hh:mm:ss a")
        msgSb.append(formatter.format(Date()))

        Toast.makeText(context, msgSb, Toast.LENGTH_LONG).show()

        wakeLock.release()
    }

    fun setAlarm(context: Context) {
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, Alarm::class.java)
        intent.putExtra(ONE_TIME, false)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        //Repeating 30 seconds
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000*5, pendingIntent)
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