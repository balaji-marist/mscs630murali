/*
This class provides column names and table names for notes.db
*/
package bmurali.ennotes.database;


import android.provider.BaseColumns;


public class EnNotesContract {

  private EnNotesContract() {
  }

  public static final class EnNotesEntry implements BaseColumns {
    public static final String TABLE_NAME = "Notes";
    public static final String ID = "_id";
    public static final String TITLE = "Title";
    public static final String COLUMN_CONTENT = "Content";
    public static final String COLUMN_DATE = "CreatedDateTime";
    public static final String TABLE_NAME_USER_KEY = "UserKey";
    public static final String USER_KEY_CONTENT = "Content";
    public static final String USER_KEY_SALT = "Salt";
    public static final String USER_FIRST_NAME = "FirstName";
    public static final String USER_LAST_NAME = "LastName";
  }
}