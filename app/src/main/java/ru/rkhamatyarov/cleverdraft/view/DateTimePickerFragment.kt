package ru.rkhamatyarov.cleverdraft.view


import android.annotation.TargetApi
import android.app.*
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle

import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import ru.rkhamatyarov.cleverdraft.ChiefApp
import ru.rkhamatyarov.cleverdraft.R
import ru.rkhamatyarov.cleverdraft.presenter.DateTimePickerPresenter
import ru.rkhamatyarov.cleverdraft.presenter.DraftListPresenter
import ru.rkhamatyarov.cleverdraft.utilities.di.DraftListActivityModule
import ru.rkhamatyarov.cleverdraft.utilities.di.module.DateTimePickerFragmentModule
import java.util.*
import javax.inject.Inject


/**
 * https://github.com/mnishiguchi/CriminalRecorder/blob/master/app/src/main/java/com/mnishiguchi/criminalrecorder/ui/DateTimePickerFragmentment.kt
 *
 * A wrapper of an AlertDialog. Although we could display an AlertDialog standalone,
 * having the dialog managed by the FragmentManager gives us more options for presenting the dialog.
 *
 * Usage:
 *   DateTimePickerFragment().show(activity.supportFragmentManager, DIALOG_DATE)
 */
class DateTimePickerFragment : DialogFragment(){
    private val TAG = javaClass.simpleName
    private var time: Long = 0

    @Inject
    lateinit var dateTimePickerPresenter: DateTimePickerPresenter

    companion object {
        val ARG_DATE = "ARG_DATE"
        val EXTRA_DATE = "${DateTimePickerFragment::class.java.canonicalName}.EXTRA_DATE"

        // Define how a hosting activity should create this fragment.
        fun newInstance(date: Date): DateTimePickerFragment {
            return DateTimePickerFragment().apply {
                //                arguments = bundleOf(ARG_DATE to date)
//                arguments.putLong(ARG_DATE, date.time)
                val bundle: Bundle = Bundle()
                bundle.getLong(ARG_DATE, date.time)
            }
        }

        // Define how the previous activity should get result.
        fun dateResult(data: Intent): Date =
                data.getSerializableExtra(EXTRA_DATE) as Date
    }


    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setupComponent()

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)

        val layoutInfalter = LayoutInflater.from(context)
        val dialogDateTimePicker: View = layoutInfalter.inflate(R.layout.dialog_datetime, null)
        dialogBuilder.setView(dialogDateTimePicker)
        dialogBuilder.setTitle("Pick datetime")
        dialogBuilder.setNegativeButton(android.R.string.cancel, null)

        dialogBuilder.setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
            val datePickerView = dialogDateTimePicker.findViewById<DatePicker>(R.id.datePicker)
            val year = datePickerView.year
            val month = datePickerView.month
            val dayOfMonth = datePickerView.dayOfMonth
            Log.d(TAG, "DatePicker: $year : $month : $dayOfMonth")

            val timePickerView = dialogDateTimePicker.findViewById<TimePicker>(R.id.timePicker)
            val hour = timePickerView.hour
            val minutes = timePickerView.minute
            Log.d(TAG, "TimePicker: $hour : $minutes")

            val calendar: Calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth, hour, minutes)
            val date = calendar.time

            Log.d(TAG, "DateTimePicker: $date")

            dateTimePickerPresenter.setDateTimeFromPicker(date)

            sendResult(Activity.RESULT_OK, date)
        })
        return dialogBuilder.create()
    }

    /**
     * Send a result back to a target fragment (requester).
     */
    private fun sendResult(resultCode: Int, date: Date) {
        Log.d(TAG, "sendResult")
        if (targetFragment == null) {
            Log.d(TAG, "sendResult: targetFragment: null")
            return
        }

        val intent = Intent().apply {
            putExtra(EXTRA_DATE, date)
        }

        // We need to directly call onActivityResult because the ActivityManager is not present in
        // the process of communicating between a fragment and a dialog.
        targetFragment.onActivityResult(targetRequestCode, resultCode, intent)
    }

    private fun setupComponent() {
        ChiefApp.get(this.activity).getAppComponent().getDateTimePickerComponent(DateTimePickerFragmentModule(this)).inject(this)
    }


}

