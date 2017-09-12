package ru.rkhamatyarov.cleverdraft.model

import android.content.Context
import com.orm.SugarContext
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.reset
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import ru.rkhamatyarov.cleverdraft.BuildConfig
import ru.rkhamatyarov.cleverdraft.data.NoteDAO
import ru.rkhamatyarov.cleverdraft.presenter.MainPresenter
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by Asus on 11.09.2017.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class MainModelTest {
    private lateinit var mainModel: MainModel
    private lateinit var noteDAO: NoteDAO
    private lateinit var mockPresenter: MainPresenter

    @Before
    fun setup(){
        val context: Context = RuntimeEnvironment.application
        SugarContext.init(context)
        noteDAO = NoteDAO(context)

        mockPresenter = Mockito.mock(MainPresenter::class.java)
        mainModel = MainModel(mockPresenter, noteDAO)
        mainModel.notes = ArrayList()

        reset(mockPresenter)
    }

    @Test
    fun loadDate() {
        val noteCounts = 20
         

    }

    @Test
    fun removeNote() {
        val note = createNote("test content")
        val insertId = noteDAO.insertNote(note)
        mainModel.notes = ArrayList<Note>()
        mainModel.notes.add(noteDAO.getNote(insertId))

        assertTrue(mainModel.removeNote(note, 0))

        val note2 = createNote("test content2")
        assertFalse(mainModel.removeNote(note2, 0))

    }

    private fun createNote(content: String): Note {
        val note = Note()
        note.content = content
        note.createdDate = Calendar.getInstance().getTime()
        return note
    }

    @After
    fun terminate() {
        SugarContext.terminate()
    }
}

