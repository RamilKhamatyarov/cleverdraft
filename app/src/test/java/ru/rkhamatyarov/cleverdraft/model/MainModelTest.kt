package ru.rkhamatyarov.cleverdraft.model

import android.content.Context
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import ru.rkhamatyarov.cleverdraft.BuildConfig
import ru.rkhamatyarov.cleverdraft.MainMVP
import ru.rkhamatyarov.cleverdraft.data.NoteDAO
import ru.rkhamatyarov.cleverdraft.presenter.MainPresenter
import ru.rkhamatyarov.cleverdraft.view.MainActivity
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by RKhamatyarov on 11.09.2017.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class MainModelTest {
    private lateinit var mainModel: MainModel
    private lateinit var noteDAO: NoteDAO
    private lateinit var mockView: MainMVP.ViewOps
    private lateinit var mockPresenter: MainPresenter

    @Before
    fun setup(){

        val context: Context = RuntimeEnvironment.application
        noteDAO = NoteDAO(context)

        mockView = Mockito.mock( MainMVP.ViewOps::class.java )
        mockPresenter = MainPresenter(mockView)

        mainModel = MainModel(mockPresenter, noteDAO)
        mainModel.notes = ArrayList()

    }

    @Test
    fun loadData() {
        val noteLength = 20

        for (item: Int in 1..noteLength) noteDAO.insertNote(makeNote("test content #"+item))
        mainModel.loadData()
        assertEquals(mainModel.notes.size, noteLength)

    }

    @Test
    fun insertData() {
        val isInserted = mainModel.insertNote(makeNote("test content"))
    }

    @Test
    fun updateNote(){
         val note = makeNote("test updated")
         val insertId = noteDAO.insertNote(note)
        mainModel.notes = ArrayList<Note>()
        mainModel.notes.add(noteDAO.getNote(insertId))

        val noteForUpd = noteDAO.getNote(insertId)
        assertTrue(mainModel.updateNote(noteForUpd, 0))

        val note2 = makeNote("test update2")
        assertFalse(mainModel.updateNote(note2, 0))
    }

    @Test
    fun removeNote() {
        val note = makeNote("test content")
        val insertId = noteDAO.insertNote(note)
        mainModel.notes = ArrayList<Note>()
        mainModel.notes.add(noteDAO.getNote(insertId))

        val noteForRm = noteDAO.getNote(insertId)
        assertTrue(mainModel.removeNote(noteForRm, 0))

        val note2 = makeNote("test content2")
        assertFalse(mainModel.removeNote(note2, 0))

    }

    @Test
    fun getNoteById(){
        var note = makeNote("test content")
        val insertId = noteDAO.insertNote(note)
        note = noteDAO.getNote(insertId)
        mainModel.notes = ArrayList<Note>()
        mainModel.notes.add(note)

        val note2 = mainModel.getNoteById(insertId)
        assertEquals(note, note2)

        mainModel.removeNote(note, 0)
        val note3 = mainModel.getNoteById(insertId)
        assertNull(note3)
    }

    private fun makeNote(content: String): Note {
        val note = Note()
        note.content = content
        note.createdDate = Calendar.getInstance().getTime()
        return note
    }


}

