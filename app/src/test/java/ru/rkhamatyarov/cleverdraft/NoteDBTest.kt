package ru.rkhamatyarov.cleverdraft

import android.content.Context
import android.util.Log
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import ru.rkhamatyarov.cleverdraft.data.NoteDAO

import org.robolectric.RuntimeEnvironment
import ru.rkhamatyarov.cleverdraft.model.Note
import java.util.*
import kotlin.properties.Delegates
import org.junit.After
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


/**
 * Created by RKhamatyarov on 28.08.2017.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class NoteDBTest {
//    private lateinit var context: Context
    private var noteDAO: NoteDAO by Delegates.notNull()
    private var context: Context by Delegates.notNull()

    @Before
    fun setup() {

        context = RuntimeEnvironment.application
        noteDAO = NoteDAO(context)
    }

    private fun getNote(content: String): Note = Note(content, Calendar.getInstance().getTime())

    @Test
    fun insertNote() {
        val content = "noteContent"
        val id = noteDAO.insertNote(getNote(content))
        val insertedNote: Note = noteDAO.getNote(id)
        assertNotNull(insertedNote)
        val note: Note = noteDAO.getNote(insertedNote.id)
        assertNotNull(note)
        assertEquals(note.content, content)
    }

    @Test
    fun noteList() {
        val notes = ArrayList<String>()
        notes.add("noteListText1")
        notes.add("noteListText2")
        notes.add("noteListText3")
        notes.add("noteListText4")

        for(noteText in notes){
            val note = Note(noteText, Calendar.getInstance().getTime())
            noteDAO.insertNote(note)
        }

        val allNotes = noteDAO.getAllNotes()
        assertNotNull(allNotes)
        assertEquals(notes.size, allNotes.size)
    }

    @Test
    fun removeNotes() {
        val content = "removeContent"
        val note = getNote(content)

        val insertId = noteDAO.insertNote(note)
        assertNotNull(insertId)

        val noteForRm = noteDAO.getNote(insertId)

        assertNotNull(noteForRm)
        assertEquals(noteForRm.content, content)

        val isRemove = noteDAO.removeNote(noteForRm)
        assertEquals(true, isRemove)

    }

    @Test
    fun updateNotes() {
        val content = "updateNote"
        val note = getNote(content)

        val insertId = noteDAO.insertNote(note)
        assertNotNull(insertId)

        val noteForUpd = noteDAO.getNote(insertId)

        assertNotNull(noteForUpd)
        assertEquals(noteForUpd.content, content)

        val countUpd = noteDAO.updateNote(noteForUpd)
        assertEquals(1, countUpd)

    }


}