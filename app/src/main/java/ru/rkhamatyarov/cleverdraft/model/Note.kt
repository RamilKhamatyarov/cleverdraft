package ru.rkhamatyarov.cleverdraft.model

import android.content.ContentValues
import ru.rkhamatyarov.cleverdraft.utililities.NoteConstants
import java.util.Date

/**
 * Created by Asus on 02.09.2017.
 */

class Note {
    var id: Long = -1
    var title: String? = null
    var content: String? = null
    var createdDate: Date? = null
    var modifiedDate: Date? = null

    constructor() {}

    constructor(id: Long, title: String, content: String, createdDate: Date, modifiedDate: Date) {
        this.id = id
        this.title = title
        this.content = content
        this.createdDate = createdDate
        this.modifiedDate = modifiedDate
    }

    constructor(content: String, createdDate: Date) {
        this.content = content
        this.createdDate = createdDate
    }


    fun getContentValues(): ContentValues {
        val contentValues = ContentValues()

        if (!id.equals(-1L))
            contentValues.put(NoteConstants.NOTES_TABLE.ID.getName(), id)

        contentValues.put(NoteConstants.NOTES_TABLE.CONTENT.getName(), content)
        contentValues.put(NoteConstants.NOTES_TABLE.TIME.getName(), createdDate.toString())

        return contentValues
    }


}
