package ru.rkhamatyarov.cleverdraft.presenter


import android.app.AlertDialog
import android.app.DialogFragment

import android.app.FragmentManager
import android.content.Context
import android.os.AsyncTask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import ru.rkhamatyarov.cleverdraft.MainMVP
import ru.rkhamatyarov.cleverdraft.R
import ru.rkhamatyarov.cleverdraft.model.Note
import ru.rkhamatyarov.cleverdraft.view.utilities.DatePickerFragment
import ru.rkhamatyarov.cleverdraft.view.utilities.NotesViewHolder
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent
import ru.rkhamatyarov.cleverdraft.presenter.async.*
import ru.rkhamatyarov.cleverdraft.view.DraftListActivity
import ru.rkhamatyarov.cleverdraft.view.MainActivity




/**
 * Created by Asus on 03.09.2017.
 */
class MainPresenter(view: MainMVP.ViewOps): MainMVP.PresenterOps, MainMVP.ProvidedPresenterOps  {


    /*View reference. We use as a WeakReference
    // because the Activity could be destroyed at any time
    // and we don't want to create a memory leak*/
    var mainView: WeakReference<MainMVP.ViewOps>?
    init {
        mainView = WeakReference<MainMVP.ViewOps>(view)
    }

    var mainModel: MainMVP.ProvidedModelOps? = null
        set(value){
            field = value
            loadData()
        }
    private var adapterPos = -1
    private var layoutPos = -1

    private var alarm: Alarm? = null

    //TODO take out separate class
    companion object {
        const val EXTRA_MESSAGE = "ru.rkhamatyarov.cleverdraft.presenter.MESSAGE"
        const val EXTRA_MESSAGE_ID = "ru.rkhamatyarov.cleverdraft.presenter.ID"
    }





    override fun setView(view: MainMVP.ViewOps) {
        this.mainView = WeakReference<MainMVP.ViewOps>(view)
    }

    override fun createViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {

        var inflater: LayoutInflater = LayoutInflater.from(parent.context)

        var viewTaskRow: View = inflater.inflate(R.layout.holder_notes, parent, false)
        var viewHolder = NotesViewHolder(viewTaskRow)

        return viewHolder
    }

    override fun bindViewHolder(holder: NotesViewHolder, position: Int) {
        val note: Note? = mainModel?.getNote(position)

        holder.content.setText(note?.content)
        val formatter = SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.US)
        holder.date.setText(formatter.format(note?.createdDate))
        holder.butonDelete.setOnClickListener({ v -> clickDeleteNote(note, holder.adapterPosition, holder.layoutPosition)})

        holder.container.setOnClickListener({v -> clickOpenNote(holder.adapterPosition, holder.layoutPosition)})

//        holder.container.setOnClickListener(({v -> setDateTimePicker(holder)}))


    }

    override fun getNotesCount(): Int? = mainModel?.getNotesCount()

    override fun newNote(editText: EditText) {
        getView().showProgress()

        val noteText: String = editText.text.toString()

        if (!noteText.isEmpty()) {
            val newNoteTask: NewNoteTask = NewNoteTask(mainModel, mainView)
            newNoteTask.noteText = noteText
            newNoteTask.execute()

        } else {
            try{
                getView().showToast(makeToast("Cannot add a blank note!"))
            } catch(e: NullPointerException){
                e.printStackTrace()
            }
        }
    }

    override fun updateNote(editText: EditText, position: Int) {
        getView().showProgress()

        val noteText: String = editText.text.toString()

        if (!noteText.isEmpty()) {
            val updateNoteTask: UpdateNoteTask = UpdateNoteTask(mainModel, mainView)
            updateNoteTask.adapterPos = position
            updateNoteTask.layoutPos = layoutPos
            updateNoteTask.noteText = noteText
            updateNoteTask.execute()
        } else {
            try{
                getView().showToast(makeToast("Cannot add a blank note!"))
            } catch(e: NullPointerException){
                e.printStackTrace()
            }
        }
    }

    private fun loadData() {
        try{
            getView().showProgress()
            LoadDataTask(mainModel, mainView).execute()

        } catch (e: NullPointerException){
            e.printStackTrace()
        }
    }

    private fun makeToast(text: String): Toast {
        return Toast.makeText(getView().getAppContext(), text, Toast.LENGTH_SHORT)
    }
    fun makeNote(content: String): Note {
        val note = Note(content, getDate())

        return note
    }

    private fun getDate(): Date {
        return Calendar.getInstance().getTime()
    }

//    @Throws(NullPointerException::class)
    private fun getView(): MainMVP.ViewOps = mainView?.get()!!

    override fun clickOpenNote(adapterPosition: Int, layoutPosition: Int) {}

    override fun clickDeleteNote(note: Note?, adapterPosition: Int, layoutPosition: Int) {}

    override fun onDestroy(isChangingConfiguration: Boolean) {
        mainView = null
        mainModel?.onDestroy(isChangingConfiguration)

        if (!isChangingConfiguration) mainModel = null
    }


    override fun setDateTimePicker(fragmentManager: FragmentManager) {
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val min = calendar.get(Calendar.MINUTE)

        val dateTimePicker: DialogFragment = DatePickerFragment.newInstance(Calendar.getInstance().getTime())
        dateTimePicker.show(fragmentManager, "dateTimePicker")
//        println("setDateTimePicker newInstance")
//        dateTimePicker.setTargetFragment()
//        dateTimePicker.show(getSupportFragmentManager(), DATEPICKER_TAG);
    }

    override fun getApplicationContext(): Context = getView().getAppContext()

    override fun getActivityContext(): Context = getView().getActContext()

}

