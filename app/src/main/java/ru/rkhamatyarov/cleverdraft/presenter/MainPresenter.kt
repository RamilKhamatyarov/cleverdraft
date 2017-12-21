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
import ru.rkhamatyarov.cleverdraft.presenter.async.NewNoteTask
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
    var mainModel: MainMVP.ProvidedModelOps? = null
        set(value){
            field = value
            loadData()
        }
    private var adapterPos = -1
    private var layoutPos = -1
    private var isUpdate = false;

    private var alarm: Alarm? = null
    companion object {
        const val EXTRA_MESSAGE = "ru.rkhamatyarov.cleverdraft.presenter.MESSAGE"
    }

    init {
       mainView = WeakReference<MainMVP.ViewOps>(view)
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

    override fun switchCreateOrUpdate(editText: EditText) {
        getView().showProgress()

        if (isUpdate) {
            updateNote(editText)

        } else {
            newNote(editText)
        }
    }

    override fun newNote(editText: EditText) {
        getView().showProgress()

        val noteText: String = editText.text.toString()

        if (!noteText.isEmpty()) {
            NewNoteTask(mainModel, mainView, noteText).execute()

        } else {
            try{
                getView().showToast(makeToast("Cannot add a blank note!"))
            } catch(e: NullPointerException){
                e.printStackTrace()
            }
        }
    }

    override fun updateNote(editText: EditText) {
        getView().showProgress()

        val noteText: String = editText.text.toString()

        if (!noteText.isEmpty()) {
            object : AsyncTask<Void, Void, Int>() {
                override fun doInBackground(vararg params: Void): Int? {
                    // Insert note in Model, returning result
                    val note = mainModel?.getNote(adapterPos)

                    note?.content = noteText
                    note?.let { mainModel?.updateNote(it, adapterPos) }

                    return adapterPos
                }

                override fun onPostExecute(adapterPosition: Int) = try {
                    if  (adapterPosition > -1) {
                        getView().hideProgress()
                        // Update note
                        getView().notifyDataSetChanged()
                        getView().clearEditText()

                        adapterPos = -1
                        layoutPos = -1
                        isUpdate = false

                    }else{
                        // Inform about error
                        getView().hideProgress()
                        getView().showToast(makeToast("Error creating note [$noteText]"))
                    }

                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }.execute()
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
            object: AsyncTask<Void, Void, Boolean>(){
                override fun doInBackground(vararg p0: Void): Boolean? = mainModel?.loadData()
                override fun onPostExecute(result: Boolean) {
                    getView().hideProgress()
                    if (result)
                        getView().notifyDataSetChanged()
                    else
                        getView().showToast(makeToast("Error loading data"))
                }
            }.execute()

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

    override fun clickOpenNote(adapterPosition: Int, layoutPosition: Int) {
        getView().showProgress()

        object : AsyncTask<Void, Void, Boolean?>() {
            var note: Note? = null
            override fun doInBackground(vararg params: Void?): Boolean? {
                note = checkNotNull(mainModel).getNote(adapterPosition)

                adapterPos = adapterPosition
                layoutPos = layoutPosition
                isUpdate = true

                return note != null
            }

            override fun onPostExecute(result: Boolean?) {
                if (result != null && result) {
                    val intent = Intent(getActivityContext(), MainActivity::class.java)
                    val message: String? = note?.content

                    intent.putExtra(EXTRA_MESSAGE, message)
                    getActivityContext().startActivity(intent)

//                    note?.content?.let { getView().setEditText(it) }
                }
            }

        }.execute()
    }

    override fun clickDeleteNote(note: Note?, adapterPosition: Int, layoutPosition: Int) =
            openDeleteAlert(note, adapterPosition, layoutPosition)

    /**
     * Create an AlertBox to confirm a delete action
     * @param note          Note to be deleted
     * @param adapterPos    Adapter position
     * @param layoutPos     Recycler layout position
     */
    private fun openDeleteAlert(note: Note?, adapterPosition: Int, layoutPosition: Int ) {
        var alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(getActivityContext())
        alertDialogBuilder.setPositiveButton("DELETE", { dialog, width ->

            deleteNote(checkNotNull(note), adapterPosition, layoutPosition)
        })
        alertDialogBuilder.setNegativeButton("CANCEL", {dialog, width -> dialog.dismiss()})
        alertDialogBuilder.setTitle("Delete note")
        alertDialogBuilder.setMessage("Delete " + note?.content + "?")

        var alertDialog: AlertDialog = alertDialogBuilder.create()
        getView().showAlert(alertDialog)
    }

    /**
     * Create a asyncTask to delete the object in Model
     * @param note          Note to delete
     * @param adapterPos    Adapter position
     * @param layoutPos     Recycler layout position
     */
    fun deleteNote(note: Note, adapterPosition: Int, layoutPosition: Int) {
        getView().showProgress()
        object : AsyncTask<Void, Void, Boolean?>() {
            // Delete note on Model, returning result
            override fun doInBackground(vararg params: Void): Boolean? = mainModel?.removeNote(note, adapterPosition)

            override fun onPostExecute(result: Boolean?) {
                getView().hideProgress()
                if (result != null && result) {
                    // Remove item from RecyclerView
                    getView().notifyItemRemoved(layoutPosition)
                    getView().showToast(makeToast("Note deleted."))
                } else {
                    // Inform about error
                    getView().showToast(makeToast("Error deleting note[" + note.id + "]"))
                }
            }
        }.execute()
    }

    override fun onDestroy(isChangingConfiguration: Boolean) {
        mainView = null
        mainModel?.onDestroy(isChangingConfiguration)

        if (!isChangingConfiguration) mainModel = null


    }

    fun removeNote(note: Note, adapterPosition: Int, layoutPosition: Int){

        getView().showProgress()
        try{
            object: AsyncTask<Void, Void, Boolean?>(){
                override fun doInBackground(vararg p0: Void): Boolean? = mainModel?.removeNote(note, adapterPosition)
                override fun onPostExecute(result: Boolean?) {
                    getView().hideProgress()
                    if (result != null && result) {
                        getView().notifyItemRemoved(layoutPosition)
                        getView().showToast(makeToast("Note removed"))
                    }else
                        getView().showToast(makeToast("Error removing data["+note.id+"]"))
                }
            }.execute()

        } catch (e: NullPointerException){
            e.printStackTrace()
        }
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

    override fun startListActivity() {
        val intent = Intent(getActivityContext(), DraftListActivity::class.java)
        getActivityContext().startActivity(intent)

    }

    override fun getApplicationContext(): Context = getView().getAppContext()

    override fun getActivityContext(): Context = getView().getActContext()

}

