package bmurali.ennotes.activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.content.Intent;
import android.widget.Toast;
import bmurali.ennotes.database.*;
import bmurali.ennotes.R;


public class SetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inital_password);
        EditText key = findViewById(R.id.editKeyText);
        EditText confirmKey = findViewById(R.id.editKeyTextConfirm);
        EditText firstName = findViewById(R.id.editTextFirstName);
        EditText lastName = findViewById(R.id.editTextLastName);
        Button bt_submit = findViewById(R.id.actionSubmit);

        bt_submit.setOnClickListener(v -> {
            String pass = key.getText().toString();
            String confim_pass = confirmKey.getText().toString();
            String first_name = firstName.getText().toString();
            String last_name = lastName.getText().toString();

            if (pass.equals("") || confim_pass.equals("")) {
                //there is no password entered
                Toast.makeText(SetPassword.this, "Please enter a password", Toast.LENGTH_SHORT).show();

            } else {
                if (pass.equals(confim_pass)) {
                    EnNotesUserDbHelper notesDbHelper = new EnNotesUserDbHelper(this);
                    SQLiteDatabase userdb = notesDbHelper.getWritableDatabase();
                    notesDbHelper.createTable(userdb);

                    ContentValues values = new ContentValues();
                    values.put(EnNotesContract.EnNotesEntry.USER_FIRST_NAME,first_name);
                    values.put(EnNotesContract.EnNotesEntry.USER_LAST_NAME,last_name);
                    values.put(EnNotesContract.EnNotesEntry.USER_KEY_CONTENT,confim_pass);
                    long rowid = userdb.insert(EnNotesContract.EnNotesEntry.TABLE_NAME_USER_KEY,null,values);
                    Toast.makeText(this, "The new Row Id is " + rowid, Toast.LENGTH_LONG).show();
                    //enter the app
                    Intent intent = new Intent(getApplicationContext(), EnterPassword.class);
                    startActivity(intent);
                    finish();
                } else {
                    //there is no match on the passwords
                    Toast.makeText(SetPassword.this, "Passwords don't Match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
