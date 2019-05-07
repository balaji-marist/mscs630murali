package bmurali.ennotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteCursor;

import java.net.NoRouteToHostException;

import bmurali.ennotes.activities.*;
import bmurali.ennotes.database.EnNotesDbHelper;


public class HomeScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        EnNotesDbHelper notesDbHelper = new EnNotesDbHelper(this);
        SQLiteDatabase userdb = notesDbHelper.getReadableDatabase();

        boolean tableExist =  isTableExists("UserKey",userdb);

        if (!tableExist) {
            //if there is no password
            Intent intent = new Intent(this, SetPassword.class);
            startActivity(intent);
            finish();
        }else {
            //if there is a password
            Intent intent = new Intent(this, EnterPassword.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean isTableExists(String tableName,SQLiteDatabase mDatabase) {
        String selection = "tbl_name=?";
        String[] selectionArgs = {tableName};
        String[] cols = {"tbl_name"};
        Cursor cursor = mDatabase.query("sqlite_master",cols,selection,
                selectionArgs,null,null,null);
        cursor.moveToNext();
        //mDatabase.rawQuery("select * from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor.getCount()>0) {
            cursor.close();
            mDatabase.close();
            return true;
        }
        cursor.close();
        mDatabase.close();
        return false;
    }
}