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

class DateTimePickerPresenter(dialogFramgent: DialogFragment) {
    private val TAG = javaClass.simpleName

    var dateTimeView: DialogFragment
    init {
        dateTimeView = dialogFramgent
    }

    private var noteAdapterPosition: Int? = null
    private var noteLayoutPosition: Int? = null

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

    fun setDateTimeToPicker(fragmentManager: FragmentManager,
                            noteAdapterPosition: Int,
                            noteLayoutPosition: Int) {

        Log.d(TAG, "FragmentManager when set datetime picket by main presenter: $fragmentManager")

        //Save current opened note coordinates to the presenter
        this.noteAdapterPosition = noteAdapterPosition
        this.noteLayoutPosition = noteLayoutPosition

        val dateTimePicker: DialogFragment = DateTimePickerFragment.newInstance(Calendar.getInstance().getTime())
        dateTimePicker.show(fragmentManager, "dateTimePicker")

    }


    fun setDateTimeFromPicker(date: Date) {
        Log.d(TAG, "Date of setting dateTime by datetime picker fragment: $date")
        Log.d(TAG, "Context of setting dateTime by datetime picker fragment: ${getApplicationContext()}")


        val draftBroadcastReceiver: DraftBroadcastReceiver = DraftBroadcastReceiver()
        draftBroadcastReceiver.setAlarm(getApplicationContext(),
                                        date,
                                        this.noteAdapterPosition!!,
                                        this.noteLayoutPosition!!)
    }

    private fun getApplicationContext(): Context = getView().applicationContext

    private fun getView(): Activity = dateTimeView.activity

}