package ru.rkhamatyarov.cleverdraft.utililities

/**
 * Created by Asus on 24.08.2017.
 */
class NoteConstants {
    companion object {
        val NOTES_TABLE : String = "notes"

        val NOTE_ID : String = "_id"
        val NOTE_TITLE : String = "title"
        val NOTE_CONTENT : String = "content"
        val NOTE_MOD_TIME : String = "modified_time"
        val NOTE_CREAT_TIME: String = "created_time"

        val NOTE_ROW = arrayOf(NOTE_ID, NOTE_TITLE, NOTE_CONTENT, NOTE_MOD_TIME, NOTE_CREAT_TIME)

    }
}