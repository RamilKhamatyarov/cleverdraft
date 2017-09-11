package ru.rkhamatyarov.cleverdraft

import android.content.Context
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import ru.rkhamatyarov.cleverdraft.data.NoteDAO

import org.robolectric.RuntimeEnvironment
import ru.rkhamatyarov.cleverdraft.model.Note
import java.util.*
import kotlin.properties.Delegates
import com.orm.SugarContext
import org.junit.After
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


/**
 * Created by Asus on 28.08.2017.
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
        SugarContext.init(context)
        noteDAO = NoteDAO(context)
    };

    private fun getNote(content: String): Note = Note(content, Calendar.getInstance().getTime())

    @Test
    fun insertNote() {
        val content = "noteContent"
        val id = noteDAO.insertNote(getNote(content))
        val insertedNote: Note = noteDAO.getNote(id)
        assertNotNull(insertedNote)
        val note: Note = noteDAO.getNote(insertedNote.id.toInt())
        assertNotNull(note)
        assertEquals(note.content, content)
        SugarContext.terminate()
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

        val allNotes = noteDAO.getAllNotes
        assertNotNull(allNotes)
        assertEquals(notes.size, allNotes.size)
    }

    @Test
    fun removeNotes() {
        val content = "removeContent"
        val note = getNote(content)
        val id = noteDAO.insertNote(note)
        assertNotNull(id)

        val noteForRm = noteDAO.getNote(id)
        assertNotNull(noteForRm)
        assertEquals(noteForRm.content, content)

        val isRemove = noteDAO.removeNote(note)
        assertEquals(true, isRemove)

    }


    @After
    fun terminate() {
        SugarContext.terminate()
    }


}