package ru.rkhamatyarov.cleverdraft.presenter.async

import android.os.AsyncTask
import android.widget.Toast
import ru.rkhamatyarov.cleverdraft.MainMVP
import ru.rkhamatyarov.cleverdraft.model.Note
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
            getView().showToast(makeToast("Error creating note [$noteText]"))
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

    private fun makeToast(text: String): Toast {
        return Toast.makeText(getView().getAppContext(), text, Toast.LENGTH_SHORT)
    }

}