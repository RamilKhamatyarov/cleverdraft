package ru.rkhamatyarov.cleverdraft.data

import android.content.Context
import com.orm.SugarApp
import com.orm.SugarRecord
import com.orm.SugarRecord.findWithQuery

import com.orm.query.Select

import java.util.ArrayList

import ru.rkhamatyarov.cleverdraft.model.Note

/**
 * Created by Asus on 28.08.2017.
 */

class NoteDAO(private val context: Context?) {

    val getAllNotes: ArrayList<Note>
        get() = ArrayList<Note> (Select.from(Note::class.java).list())

    fun insertNote(note: Note): Int = note.save().toInt()


    fun removeNote(note: Note): Boolean {

        return note.delete()
    }

    fun getNote(id: Int): Note {
        return SugarRecord.findWithQuery<Note>(Note::class.java, "select * from note where id = ?", id.toString()).get(0)
    }

}
