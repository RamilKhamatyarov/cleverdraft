package ru.rkhamatyarov.cleverdraft.presenter

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import ru.rkhamatyarov.cleverdraft.MainMVP
import ru.rkhamatyarov.cleverdraft.model.Note
import ru.rkhamatyarov.cleverdraft.view.utilities.NotesViewHolder
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by Asus on 03.09.2017.
 */
open class MainPresenter: MainMVP.PresenterOps, MainMVP.ProvidedPresenterOps  {
    /*View reference. We use as a WeakReference
    // because the Activity could be destroyed at any time
    // and we don't want to create a memory leak*/
    private lateinit var mainView: WeakReference<MainMVP.ViewOps>
    private lateinit var mainModel: MainMVP.ProvidedModelOps

//    private lateinit var applicationContext: Context
//    private lateinit var activityContext: Context

    constructor(view: MainMVP.ViewOps) {
        this.mainView = WeakReference<MainMVP.ViewOps>(view)
    }



    override fun setView(view: MainMVP.ViewOps) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindViewHolder(holder: NotesViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNotesCount() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clickNewNote(editText: EditText) {
        getView().showProgress();

        val noteText: String = editText.text.toString()

        if (!noteText.isEmpty()) {
            object : AsyncTask<Void, Void, Int>() {
                override fun doInBackground(vararg params: Void): Int {
                    // Insert note in Model, returning result
                    return mainModel.insertNote(makeNote(noteText))
                }

                override fun onPostExecute(adapterPosition: Int) {
                    try {
                        if (adapterPosition > -1) {
                            // Insert note
                            getView().clearEditText()
                            getView().notifyItemInserted(adapterPosition + 1)
                            getView().notifyItemRangeChanged(adapterPosition, mainModel.getNotesCount())
                        } else {
                            // Inform about error
                            getView().hideProgress()
                            getView().showToast(makeToast("Error creating note [$noteText]"))
                        }

                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }

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

    fun setModel(model: MainMVP.ProvidedModelOps){
        this.mainModel = model

        loadData()
    }

    private fun loadData() {
        try{
            getView().showProgress()
            object: AsyncTask<Void, Void, Boolean>(){
                override fun doInBackground(vararg p0: Void): Boolean = mainModel.loadData()
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
    private fun getView(): MainMVP.ViewOps = mainView.get()!!

    override fun clickDeleteNote(note: Note, adapterPostion: Int, layoutPosition: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy(isChangingConfiguration: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun removeNote(note: Note, adapterPosition: Int, layoutPosition: Int){

        getView().showProgress()
        try{
            object: AsyncTask<Void, Void, Boolean>(){
                override fun doInBackground(vararg p0: Void): Boolean = mainModel.removeNote(note, adapterPosition)
                override fun onPostExecute(result: Boolean) {
                    getView().hideProgress()
                    if (result) {
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

    override fun getApplicationContext(): Context = getView().getAppContext()

    override fun getActivityContext(): Context = getView().getActContext()

}

