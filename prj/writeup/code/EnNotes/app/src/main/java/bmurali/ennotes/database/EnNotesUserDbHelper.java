package bmurali.ennotes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EnNotesUserDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EnNotes.db";
    private static final int DATABASE_VERSION = 1;
    private final String SQL_CREATE_ENTRIES_USER_PWD = "CREATE TABLE IF NOT EXISTS " +
            EnNotesContract.EnNotesEntry.TABLE_NAME_USER_KEY + " (" +
            EnNotesContract.EnNotesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            EnNotesContract.EnNotesEntry.USER_KEY_CONTENT + " TEXT NOT NULL," +
            EnNotesContract.EnNotesEntry.USER_FIRST_NAME + " TEXT NOT NULL, " +
            EnNotesContract.EnNotesEntry.USER_LAST_NAME + " TEXT NOT NULL, " +
            EnNotesContract.EnNotesEntry.USER_KEY_SALT + " BLOB NOT NULL" +
            ");";

    private final String SQL_DELETE_ENTRIES_USER_PWD =
            "DROP TABLE IF EXISTS " + EnNotesContract.EnNotesEntry.TABLE_NAME;

    public EnNotesUserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
    Creates a new table with specified table columns and an auto incremented id column
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_USER_PWD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES_USER_PWD);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db,oldVersion,newVersion);
    }

    public void createTable(SQLiteDatabase db){
        onCreate(db);
    }
}
