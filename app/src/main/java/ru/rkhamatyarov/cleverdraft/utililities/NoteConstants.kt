package ru.rkhamatyarov.cleverdraft.utililities

/**
 * Created by Asus on 24.08.2017.
 */
class NoteConstants {
    companion object {
        val DB_VERSION = 1
        val DB_NAME = "cleverdraft.db";


        val NOTES_TABLE_NAME = "notes"

        val COMMA_SPACE = ", "
        val CREATE_TABLE = "CREATE TABLE "
        val PRIMARY_KEY = "PRIMARY KEY "
        val UNIQUE = "UNIQUE "
        val TYPE_TEXT = " TEXT "
        val TYPE_DATE = " DATETIME "
        val TYPE_INT = " INTEGER "
        val DEFAULT = "DEFAULT "
        val AUTOINCREMENT = "AUTOINCREMENT "
        val NOT_NULL = "NOT NULL "
        val DROP_TABLE = "DROP TABLE IF EXISTS "

        val ID_COL = "_id"
        val CONTENT_COL = "content"
        val TIME_COL = "time"


        // set $variables
        val CREATE_NOTES_TABLE = CREATE_TABLE + NOTES_TABLE_NAME + "(" +
                NOTES_TABLE.ID.getName() + TYPE_INT + NOT_NULL + PRIMARY_KEY + COMMA_SPACE +
                NOTES_TABLE.CONTENT.getName() + TYPE_TEXT + NOT_NULL + COMMA_SPACE +
                NOTES_TABLE.TIME.getName() + TYPE_INT + NOT_NULL + ")"

        val SELECT_ALL_RECORDS = "SELECT * FROM "
        val SELECT_ID_BASED = NOTES_TABLE.ID.getName() + " = ? "
        val PROJECTION_ALL = " * "
        val SORT_ORDER_DEFAULT =  NOTES_TABLE.ID.getName() + " DESC"
    }

    enum class NOTES_TABLE(name: String) {
        ID(ID_COL), CONTENT(CONTENT_COL), TIME(TIME_COL);
        fun getName() = name
        fun getTime() = Integer.valueOf(TIME_COL)
    }
}