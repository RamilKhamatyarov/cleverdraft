package ru.rkhamatyarov.cleverdraft.data

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

import ru.rkhamatyarov.cleverdraft.model.Note
import ru.rkhamatyarov.cleverdraft.utilities.NoteConstants
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Asus on 28.08.2017.
 */

class NoteDAO(ctx: Context?) {
    private val mainHelper: DBSchema = DBSchema(ctx)

    private fun getReadableDb(): SQLiteDatabase{
        return mainHelper.readableDatabase
    }

    private fun getWritableDb(): SQLiteDatabase{
        return mainHelper.writableDatabase
    }

    fun getAllNotes(): ArrayList<Note> {
        val db: SQLiteDatabase = getReadableDb()

//        val cursor: Cursor = db.query(NoteConstants.NOTES_TABLE_NAME, null, null, null, null, null,
//                NoteConstants.SORT_ORDER_DEFAULT)
        val cursor: Cursor = db.rawQuery(NoteConstants.SELECT_ALL_RECORDS + NoteConstants.NOTES_TABLE_NAME, null)
        cursor.moveToFirst()
        Log.d("DAO", cursor.toString())
        val notes: ArrayList<Note> = ArrayList<Note>()
        var note: Note?
        while (!cursor.isAfterLast()) {
            note = Note()
            note.id = cursor.getLong(cursor.getColumnIndexOrThrow(NoteConstants.NOTES_TABLE.ID.getName()))
            note.content = cursor.getString(cursor.getColumnIndexOrThrow(NoteConstants.NOTES_TABLE.CONTENT.getName()))
            note.createdDate = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(cursor.getString(cursor.getColumnIndexOrThrow(NoteConstants.NOTES_TABLE.TIME.getName())))
            notes.add(note)
            cursor.moveToNext()
            Log.d("DAO", notes.toString())
        }
        Log.d("DAO", notes.toString())
        cursor.close()
        db.close()
        return notes
    }

    fun insertNote(note: Note): Long {

        val db: SQLiteDatabase = getWritableDb()


        val id = db.insert(NoteConstants.NOTES_TABLE_NAME, null, note.getContentValues())

        db.close()
        return id
    }


    fun removeNote(note: Note): Boolean {
        val db: SQLiteDatabase = getWritableDb()
        val id = db.delete(NoteConstants.NOTES_TABLE_NAME, NoteConstants.SELECT_ID_BASED,
                arrayOf(note.id.toString()))

        db.close()

        return id > 0
    }

    fun getNote(id: Long): Note {
        val db: SQLiteDatabase = getReadableDb()

        val cursor: Cursor = db.query(NoteConstants.NOTES_TABLE_NAME, null, NoteConstants.SELECT_ID_BASED,
                    arrayOf(id.toString()), null, null, null)

        cursor.moveToFirst()
        val note = Note()
        note.id = cursor.getLong(cursor.getColumnIndexOrThrow(NoteConstants.NOTES_TABLE.ID.toString()))
        note.content = cursor.getString(cursor.getColumnIndexOrThrow(NoteConstants.NOTES_TABLE.CONTENT.toString()))
        note.createdDate = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(cursor.getString(cursor.getColumnIndexOrThrow(NoteConstants.NOTES_TABLE.TIME.getName())))
        cursor.close()
        db.close()
        return note
    }

    fun updateNote(note: Note): Int {
        val db = getWritableDb()

        val updCount = db.update(NoteConstants.NOTES_TABLE_NAME, note.getContentValues(), NoteConstants.SELECT_ID_BASED,
                arrayOf(note.id.toString()))

        db.close()
        return updCount
    }


}
