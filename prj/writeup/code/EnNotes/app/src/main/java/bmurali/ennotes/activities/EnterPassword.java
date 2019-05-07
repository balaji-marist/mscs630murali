package bmurali.ennotes.activities;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import bmurali.ennotes.R;
import bmurali.ennotes.database.EnNotesDbHelper;

public class EnterPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EnNotesDbHelper notesDbHelper = new EnNotesDbHelper(this);
        SQLiteDatabase userdb = notesDbHelper.getReadableDatabase();

        Cursor password_db = userdb.rawQuery("select FirstName,content from UserKey",null);
        password_db.moveToFirst();

        setContentView(R.layout.activity_enter_password);

        TextView nameTextView = findViewById(R.id.textViewName);
        nameTextView.setText(password_db.getString(password_db.getColumnIndex("FirstName")));


        Button b = findViewById(R.id.actionSubmitPassword);

        b.setOnClickListener(v -> {
            EditText pwd_from_screen = (EditText)findViewById(R.id.editTextKey);
            String pwd_string = pwd_from_screen.getText().toString();

            if(password_db.getString(1).equals(pwd_string)){
                Intent intent = new Intent(getApplicationContext(), NewNote.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
