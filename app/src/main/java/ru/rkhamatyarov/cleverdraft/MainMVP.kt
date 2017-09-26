package ru.rkhamatyarov.cleverdraft

import android.app.AlertDialog
import android.content.Context
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import ru.rkhamatyarov.cleverdraft.model.Note
import ru.rkhamatyarov.cleverdraft.view.utilities.NotesViewHolder

/**
 * Created by Asus on 03.09.2017.
 */

interface   MainMVP {

    interface ViewOps{
        fun getAppContext(): Context
        fun getActContext(): Context
        fun showToast(toast: Toast)
        fun showProgress()
        fun hideProgress()
        fun showAlert(alertDialog: AlertDialog)
        fun notifyItemRemoved(position: Int)
        fun notifyDataSetChanged()
        fun notifyItemInserted(layoutPosition: Int)
        fun notifyItemRangeChanged(startPosition: Int, count: Int)
        fun clearEditText()
    }

    interface PresenterOps{
        fun getApplicationContext(): Context
        fun getActivityContext(): Context
    }


    interface ProvidedPresenterOps{
        fun onDestroy(isChangingConfiguration: Boolean)
        fun setView(view: ViewOps)
        fun createViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder
        fun bindViewHolder(holder: NotesViewHolder, position: Int)
        fun getNotesCount(): Int
        fun clickNewNote(editText: EditText)
        fun clickDeleteNote(note: Note, adapterPostion: Int, layoutPosition: Int)
    }

    interface ProvidedModelOps {
        fun onDestroy(isChangingConfiguration: Boolean)
        fun insertNote(note: Note): Int
        fun loadData(): Boolean
        fun removeNote(note: Note, adapterPostion: Int): Boolean
        fun getNote(position: Int): Note
        fun getNotesCount(): Int
    }
}