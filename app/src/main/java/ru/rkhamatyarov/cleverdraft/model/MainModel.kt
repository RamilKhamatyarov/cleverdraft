package ru.rkhamatyarov.cleverdraft.model

import ru.rkhamatyarov.cleverdraft.MainMVP
import ru.rkhamatyarov.cleverdraft.data.NoteDAO

/**
 * Created by Asus on 03.09.2017.
 */
open class MainModel(var mainPresenter: MainMVP.ProvidedPresenterOps?): MainMVP.ProvidedModelOps{

    //    private lateinit var mainPresenter: MainMVP.ProvidedPresenterOps
    private var noteDAO: NoteDAO?
    lateinit var notes: ArrayList<Note>

    /*Main constructor, called by Activity during MVP setup*/
    init {
        noteDAO = NoteDAO(mainPresenter?.getApplicationContext())
    }

    /*Test contructor. Called only during unit testing*/
    constructor(mainPresenter: MainMVP.ProvidedPresenterOps, noteDAO: NoteDAO): this(mainPresenter) {
        this.noteDAO = noteDAO
    }


    override fun onDestroy(isChangingConfiguration: Boolean) {
        if (!isChangingConfiguration) {
            mainPresenter = null
            noteDAO = null
            notes.clear()

        }
    }

    override fun insertNote(note: Note): Long {
        val insertedId = noteDAO?.insertNote(note)

        if (insertedId != null) {
            loadData()
            return insertedId
        }
        return -1
    }

    override fun loadData(): Boolean {
        notes = checkNotNull(noteDAO?.getAllNotes())
        return true
    }

    override fun getNote(position: Int): Note? {


        return notes.get(position)
    }

    override fun getNotesCount(): Int? {
        if (true) return notes.size
        return 0
    }

    override fun updateNote(note: Note, adapterPosition: Int): Boolean {
        val countUpd = noteDAO?.updateNote(note)


        if (countUpd == 1) {
            notes[adapterPosition] = note

            val noteIndex = getNotePosition(note)
            if (noteIndex != -1) {//To change body of created functions use File | Settings | File Templates.
                return true
            }
        }

        return false
    }

    override fun removeNote(note: Note, adapterPosition: Int): Boolean {
        val isRemove = noteDAO?.removeNote(note)

        if (isRemove != null && isRemove) {

            notes.removeAt(adapterPosition)
            return true
        }
        return false
    }

    override fun getNoteById(id: Long): Note? {
        for (item in this.notes){
            if (id == item.id) return item
        }
        return null
    }

    override fun getNotePosition(note: Note): Int? {
        for (item in this.notes)
            if (note.id == item.id)
                return notes.indexOf(item)
        return -1
    }




}