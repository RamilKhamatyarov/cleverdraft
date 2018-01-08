package ru.rkhamatyarov.cleverdraft.presenter

import android.app.AlertDialog
import android.app.FragmentManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import ru.rkhamatyarov.cleverdraft.MainMVP
import ru.rkhamatyarov.cleverdraft.R
import ru.rkhamatyarov.cleverdraft.model.Note
import ru.rkhamatyarov.cleverdraft.presenter.async.DeleteNoteTask
import ru.rkhamatyarov.cleverdraft.presenter.async.LoadDataTask
import ru.rkhamatyarov.cleverdraft.presenter.async.OpenNoteTask
import ru.rkhamatyarov.cleverdraft.view.utilities.NotesViewHolder
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Asus on 08.01.2018.
 */

class DraftListPresenter(view: MainMVP.ViewOps): MainMVP.PresenterOps, MainMVP.ProvidedPresenterOps  {

    var mainView: WeakReference<MainMVP.ViewOps>?
    init {
        mainView = WeakReference<MainMVP.ViewOps>(view)
    }

    var mainModel: MainMVP.ProvidedModelOps? = null
        set(value){
            field = value
            loadData()
        }

    private var isUpdate = false

    override fun onDestroy(isChangingConfiguration: Boolean) {
        mainView = null
        mainModel?.onDestroy(isChangingConfiguration)

        if (!isChangingConfiguration) mainModel = null
    }

    override fun setView(view: MainMVP.ViewOps) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
    }

    override fun getNotesCount(): Int? = mainModel?.getNotesCount()

    override fun newNote(editText: EditText) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateNote(editText: EditText) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun switchCreateOrUpdate(editText: EditText) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clickDeleteNote(note: Note?, adapterPosition: Int, layoutPosition: Int)
            = openDeleteAlert(note, adapterPosition, layoutPosition)

    override fun clickOpenNote(adapterPosition: Int, layoutPosition: Int) {
        getView().showProgress()

        val openNoteTask: OpenNoteTask = OpenNoteTask(mainModel, mainView)
        openNoteTask.adapterPos = adapterPosition
        openNoteTask.layoutPos = layoutPosition
        isUpdate = true
        openNoteTask.isUpdate = isUpdate

        openNoteTask.execute()
    }

    override fun setDateTimePicker(fragmentManager: FragmentManager) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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

        val deleteNoteTask: DeleteNoteTask = DeleteNoteTask(mainModel, mainView)
        deleteNoteTask.adapterPos = adapterPosition
        deleteNoteTask.layoutPos = layoutPosition
        deleteNoteTask.note = note

        deleteNoteTask.execute()
    }


    override fun getApplicationContext(): Context = getView().getAppContext()

    override fun getActivityContext(): Context = getView().getActContext()

    private fun loadData() {
        try{
            getView().showProgress()
            LoadDataTask(mainModel, mainView).execute()

        } catch (e: NullPointerException){
            e.printStackTrace()
        }
    }

    private fun getView(): MainMVP.ViewOps = mainView?.get()!!

}
