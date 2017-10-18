package ru.rkhamatyarov.cleverdraft.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ru.rkhamatyarov.cleverdraft.model.Note
import ru.rkhamatyarov.cleverdraft.utililities.NoteConstants

/**
 * Created by Asus on 09.10.2017.
 */
class DBSchema: SQLiteOpenHelper{

    constructor(ctx: Context?) : super(ctx, NoteConstants.DB_NAME, null, NoteConstants.DB_VERSION)

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(NoteConstants.CREATE_NOTES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(NoteConstants.CREATE_TABLE)
    }


}