/*
* This blank activity verifies if the user has already set a key
* if yes, redirects to enter password activity else set password activity.
*/
package bmurali.ennotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import bmurali.ennotes.activities.*;
import bmurali.ennotes.database.EnNotesDbHelper;


public class HomeScreen extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home_screen);

    EnNotesDbHelper notesDbHelper = new EnNotesDbHelper(this);
    SQLiteDatabase userdb = notesDbHelper.getReadableDatabase();

    boolean tableExist = doesTableExist("UserKey", userdb);

    if (!tableExist) {
      //if there is no password
      Intent intent = new Intent(this, SetPassword.class);
      startActivity(intent);
      finish();
    } else {
      //if there is a password
      Intent intent = new Intent(this, EnterPassword.class);
      startActivity(intent);
      finish();
    }
  }

  // If the table exists assumption is user has already set a key.
  public boolean doesTableExist(String tableName, SQLiteDatabase mDatabase) {
    String selection = "tbl_name=?";
    String[] selectionArgs = {tableName};
    String[] cols = {"tbl_name"};
    Cursor cursor = mDatabase.query("sqlite_master", cols, selection,
        selectionArgs, null, null, null);
    cursor.moveToNext();
    if (cursor.getCount() > 0) {
      cursor.close();
      mDatabase.close();
      return true;
    }
    cursor.close();
    mDatabase.close();
    return false;
  }
}