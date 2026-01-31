package com.itsthwng.noteapp.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, VERSION
) {
    companion object {
        private const val DATABASE_NAME = "note_app.db"
        private const val VERSION = 1
        private const val TABLE_NAME = "all_notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """CREATE TABLE $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY,
            $COLUMN_TITLE TEXT,
            $COLUMN_CONTENT TEXT
        )""".trimMargin()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("""DROP TABLE IF EXISTS $TABLE_NAME""")
        onCreate(db)
    }

    fun insertNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllNotes(): List<Note> {
        val noteList = mutableListOf<Note>()
        val db = readableDatabase
        val query = """SELECT * FROM $TABLE_NAME"""
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val note = Note(id, title, content)
            noteList.add(note)
        }
        cursor.close()
        db.close()
        return noteList
    }

    fun updateNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArray = arrayOf(note.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArray)
        db.close()
    }

    fun getNoteById(id: Int): Note? {
        val db = readableDatabase
        val query = """SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $id"""
        val cursor = db.rawQuery(query, null)
        var note: Note? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            note = Note(id, title, content)
        }
        cursor.close()
        db.close()
        return note
    }

    fun deleteNote(id: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val array = arrayOf(id.toString())
        db.delete(TABLE_NAME, whereClause, array)
        db.close()
    }
}
