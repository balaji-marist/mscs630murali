/*
* This activity will be the first activity the user will interact with
* if they have already chosen a key. Authentication will compare the hash
* value of the password entered with the database. Uses SHA-256 hashing
* and salted passwords.
* */
package bmurali.ennotes.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import bmurali.ennotes.R;
import bmurali.ennotes.database.EnNotesUserDbHelper;
import bmurali.ennotes.encryption.HashMe;

public class EnterPassword extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    EnNotesUserDbHelper notesUserDbHelper = new EnNotesUserDbHelper(this);
    SQLiteDatabase userDB = notesUserDbHelper.getReadableDatabase();

    Cursor password_db = userDB.rawQuery("select FirstName,content," +
        "salt from UserKey", null);
    password_db.moveToFirst();

    setContentView(R.layout.activity_enter_password);

    TextView nameTextView = findViewById(R.id.textViewName);
    nameTextView.setText(password_db.getString(password_db.getColumnIndex
        ("FirstName")));


    Button b = findViewById(R.id.actionSubmitPassword);

    // Submit clicked
    b.setOnClickListener(v -> {
      String pwd_string = "";
      EditText pwd_from_screen = (EditText) findViewById(R.id.editTextKey);
      try {
        pwd_string = HashMe.securePassword(pwd_from_screen.getText().toString(),
            password_db.getBlob(2));
      } catch (Exception e) {}

      // If the password hash checks out, send the user to the main screen.
      if (password_db.getString(1).equals(pwd_string)) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
      }
    });
  }
}
