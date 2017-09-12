package ru.rkhamatyarov.cleverdraft.model

import ru.rkhamatyarov.cleverdraft.MainMVP
import ru.rkhamatyarov.cleverdraft.data.NoteDAO

/**
 * Created by Asus on 03.09.2017.
 */
open class MainModel: MainMVP.ProvidedModelOps{

    private lateinit var mainPresenter: MainMVP.PresenterOps
    private var noteDAO: NoteDAO
    lateinit var notes: ArrayList<Note>

    /*Main constructor, called by Activity during MVP setup*/
    constructor(mainPresenter: MainMVP.PresenterOps) {
        this.mainPresenter = mainPresenter
        noteDAO = NoteDAO(mainPresenter.getApplicationContext())
    }

    /*Test contructor. Called only during unit testing*/
    constructor(mainPresenter: MainMVP.PresenterOps, noteDAO: NoteDAO) {
        this.mainPresenter = mainPresenter
        this.noteDAO = noteDAO
    }


    override fun onDestroy(isChangingConfiguration: Boolean) {
        if (!isChangingConfiguration) {
//            mainPresenter =
//            noteDAO = null
//            notes = null
            TODO()
        }
    }

    override fun insertNote(note: Note): Int {
        val insertedId = noteDAO.insertNote(note)

        if (insertedId != 0) {
            loadData()
            return insertedId
        }
        return -1
    }

    override fun loadData(): Boolean {
        notes = noteDAO.getAllNotes
        return notes != null
    }

    override fun getNote(position: Int): Note {
        return notes.get(position)
    }

    override fun getNotesCount(): Int = notes.size


    override fun removeNote(note: Note, adapterPostion: Int): Boolean {
        val isRemove = noteDAO.removeNote(note)
        if (isRemove) {
            notes.removeAt(adapterPostion)
            return true
        }
        return false
    }

    fun getNotePosition(note: Note): Int{
        for (item in this.notes)
            if (note.id == item.id)
                return notes.indexOf(item)
        return -1
    }
}