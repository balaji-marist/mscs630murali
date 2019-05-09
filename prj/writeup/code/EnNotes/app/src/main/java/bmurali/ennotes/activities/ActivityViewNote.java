package bmurali.ennotes.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import bmurali.ennotes.R;
import bmurali.ennotes.database.EnNotesContract;
import bmurali.ennotes.database.EnNotesUserDbHelper;
import bmurali.ennotes.encryption.CryptoEnNotes;

public class ActivityViewNote extends AppCompatActivity {
    String decryptedText;
    boolean decrypted = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        String title = getIntent().getStringExtra("NOTE_TITLE");
        String content = getIntent().getStringExtra("NOTE_CONTENT");
        Integer id = getIntent().getIntExtra("NOTE_ID",0);

        ((EditText)findViewById(R.id.editTextTitleInViewActivity)).setText(title);
        ((EditText)findViewById(R.id.editTextContentInViewActivity)).setText(content);

        FloatingActionButton decrypt = findViewById(R.id.floatingActionButtonDecrypt);
        FloatingActionButton save = findViewById(R.id.floatingActionButtonSaveInViewScreen);
        decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrypted = true;
                try{
                    decryptedText = CryptoEnNotes.decrypt(getApplicationContext(),content);
                    ((EditText)findViewById(R.id.editTextContentInViewActivity)).setText(decryptedText);
                }
                catch (Exception e){}
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!decrypted)
                    Toast.makeText(getApplicationContext(),
                            "Decrypt your content before updating" ,
                            Toast.LENGTH_LONG).show();
                else{
                    EnNotesUserDbHelper notesDbHelper = new EnNotesUserDbHelper(getApplicationContext());
                    SQLiteDatabase userdb = notesDbHelper.getWritableDatabase();

                    String updatedTitle = ((EditText) findViewById(R.id.editTextTitleInViewActivity)).getText().toString();
                    String updatedContent = ((EditText) findViewById(R.id.editTextContentInViewActivity)).getText().toString();
                    ContentValues values = new ContentValues();
                    values.put(EnNotesContract.EnNotesEntry.ID,id);
                    values.put(EnNotesContract.EnNotesEntry.TITLE,updatedTitle);
                    values.put(EnNotesContract.EnNotesEntry.COLUMN_CONTENT,updatedContent);
                    userdb.update(EnNotesContract.EnNotesEntry.TABLE_NAME,values,
                            "_id="+id,null);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
