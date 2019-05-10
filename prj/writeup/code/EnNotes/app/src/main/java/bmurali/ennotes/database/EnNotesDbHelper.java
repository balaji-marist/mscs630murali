
/*
This Database helper class provides the onCreate and onUpgrade methods that are called to create
a new database if there is none.
*/

package bmurali.ennotes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import bmurali.ennotes.database.EnNotesContract.EnNotesEntry;

public class EnNotesDbHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "EnNotes.db";
  private static final int DATABASE_VERSION = 1;

  public EnNotesDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  /*
  Creates a new table with specified table columns and an auto incremented id column
   */
  @Override
  public void onCreate(SQLiteDatabase db) {
    final String SQL_CREATE_NOTES_TABLE = "CREATE TABLE IF NOT EXISTS " +
        EnNotesEntry.TABLE_NAME + " (" +
        EnNotesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        EnNotesEntry.TITLE + " TEXT NOT NULL, " +
        EnNotesEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
        EnNotesEntry.COLUMN_DATE + " INT NOT NULL" +
        ");";

    db.execSQL(SQL_CREATE_NOTES_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + EnNotesEntry.TABLE_NAME);
    onCreate(db);
  }

  public void createTable(SQLiteDatabase db) {
    onCreate(db);
  }
}