package ru.rkhamatyarov.cleverdraft.presenter

import android.app.Activity
import android.app.DialogFragment
import android.app.FragmentManager
import android.content.Context
import android.util.Log
import ru.rkhamatyarov.cleverdraft.view.DateTimePickerFragment
import java.util.*

/**
 * Created by RKhamatyarov on 08.02.2018.
 */

class DateTimePickerPresenter(dialogFragment: DateTimePickerFragment) {
    private val TAG = javaClass.simpleName

    var dateTimeView: DialogFragment
    init {
        dateTimeView = dialogFragment
    }

    private var _dateTime: Date? = null
        set(value) {
            if (value != null) _dateTime = value
        }

    val dateTime: Date
        get() {
            if (_dateTime == null) {
                _dateTime = Date()
            }
            return _dateTime ?: throw AssertionError("Set to null by another thread")
        }

    fun setDateTimeToPicker(fragmentManager: FragmentManager) {

        var dateTimePicker: DialogFragment = DateTimePickerFragment.newInstance(Calendar.getInstance().getTime())
        dateTimePicker.show(fragmentManager, "dateTimePicker")

    }


    fun setDateTimeFromPicker(date: Date) {
        Log.d(TAG, "dateTimeFromPicker: $date")
        Log.d(TAG, "context: ${getApplicationContext()}")

        val alarm: Alarm = Alarm()
        alarm.setAlarm(getApplicationContext(), date)
    }

    private fun getApplicationContext(): Context = getView().applicationContext

    private fun getView(): Activity = dateTimeView.activity

}