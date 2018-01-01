package ru.rkhamatyarov.cleverdraft.presenter.async

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.widget.Toast
import ru.rkhamatyarov.cleverdraft.MainMVP
import ru.rkhamatyarov.cleverdraft.model.Note
import ru.rkhamatyarov.cleverdraft.presenter.MainPresenter
import ru.rkhamatyarov.cleverdraft.view.MainActivity
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by Asus on 20.12.2017.
 */

class NewNoteTask constructor(val mainModel: MainMVP.ProvidedModelOps?, val mainView: WeakReference<MainMVP.ViewOps>?,
                               val noteText: String) : AsyncTask<Void, Void, Int>() {

    override fun doInBackground(vararg params: Void?): Int? = mainModel?.insertNote(makeNote(noteText))?.toInt()

    override fun onPostExecute(adapterPosition: Int?) {
        if (adapterPosition != null && adapterPosition > -1) {
            // Insert note
            getView().clearEditText()
            getView().notifyItemInserted(adapterPosition + 1)
            getView().notifyItemRangeChanged(adapterPosition, mainModel?.getNotesCount())
            getView().hideProgress()
        } else {
            // Inform about error
            getView().hideProgress()
            getView().showToast(makeToast(getView(),"Error creating note [$noteText]"))
        }
    }

    private fun getView(): MainMVP.ViewOps = mainView?.get()!!

    fun makeNote(content: String): Note {
        val note = Note(content, getDate())

        return note
    }

    private fun getDate(): Date {
        return Calendar.getInstance().getTime()
    }

}

class UpdateNoteTask constructor(val mainModel: MainMVP.ProvidedModelOps?, val mainView: WeakReference<MainMVP.ViewOps>?,
val noteText: String) : AsyncTask<Void, Void, Int>(){
    var adapterPos = -1
    var layoutPos = -1
    var isUpdate = false

    override fun doInBackground(vararg params: Void?): Int {
        // Insert note in Model, returning result
        val note = mainModel?.getNote(adapterPos)

        note?.content = noteText
        note?.let { mainModel?.updateNote(it, adapterPos) }

        return adapterPos
    }

    override fun onPostExecute(adapterPosition: Int)= try {
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
            getView().showToast(makeToast(getView(),"Error creating note [$noteText]"))
        }

    } catch (e: NullPointerException) {
        e.printStackTrace()
    }


    private fun getView(): MainMVP.ViewOps = mainView?.get()!!
}

class LoadDataTask constructor(val mainModel: MainMVP.ProvidedModelOps?, val mainView: WeakReference<MainMVP.ViewOps>?)
                                : AsyncTask<Void, Void, Boolean>(){
    override fun doInBackground(vararg p0: Void): Boolean? = mainModel?.loadData()
    override fun onPostExecute(result: Boolean) {
        getView().hideProgress()
        if (result)
            getView().notifyDataSetChanged()
        else
            getView().showToast(makeToast(getView(),"Error loading data"))
    }

    private fun getView(): MainMVP.ViewOps = mainView?.get()!!

}

class OpenNoteTask constructor(val mainModel: MainMVP.ProvidedModelOps?, val mainView: WeakReference<MainMVP.ViewOps>?)
    : AsyncTask<Void, Void, Boolean>(){
    var adapterPos = -1
    var layoutPos = -1
    var isUpdate = false
    var note: Note? = null

    override fun doInBackground(vararg params: Void?): Boolean {
        note = checkNotNull(mainModel).getNote(adapterPos)

//        adapterPos = adapterPosition
//        layoutPos = layoutPosition
        isUpdate = true

        return note != null
    }

    override fun onPostExecute(result: Boolean?) {
        if (result != null && result) {
            val intent = Intent(getActivityContext(), MainActivity::class.java)
            val message: String? = note?.content

            intent.putExtra(MainPresenter.EXTRA_MESSAGE, message)
            getActivityContext().startActivity(intent)

//                    note?.content?.let { getView().setEditText(it) }
        }
    }

    fun getActivityContext(): Context = mainView?.get()!!.getActContext()

}

class DeleteNoteTask constructor(val mainModel: MainMVP.ProvidedModelOps?, val mainView: WeakReference<MainMVP.ViewOps>?)
    : AsyncTask<Void, Void, Boolean>(){

    var adapterPos = -1
    var layoutPos = -1
    var note: Note? = null

    override fun doInBackground(vararg params: Void?): Boolean? = note?.let { mainModel?.removeNote(it, adapterPos) }

    override fun onPostExecute(result: Boolean?) {
        getView().hideProgress()
        if (result != null && result) {
            // Remove item from RecyclerView
            getView().notifyItemRemoved(layoutPos)
            getView().showToast(makeToast(getView(),"Note deleted."))
        } else {
            // Inform about error
            getView().showToast(makeToast(getView(), "Error deleting note[" + note?.id + "]"))
        }
    }

    private fun getView(): MainMVP.ViewOps = mainView?.get()!!
}

private fun makeToast(view: MainMVP.ViewOps, text: String): Toast =
        Toast.makeText(view.getAppContext(), text, Toast.LENGTH_SHORT)