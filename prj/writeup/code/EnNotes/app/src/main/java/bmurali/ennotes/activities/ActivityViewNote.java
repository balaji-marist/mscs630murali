package bmurali.ennotes.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
    String title, content;
    static final int responseFromFingerprint = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        title = getIntent().getStringExtra("NOTE_TITLE");
        content = getIntent().getStringExtra("NOTE_CONTENT");
        Integer id = getIntent().getIntExtra("NOTE_ID",0);

        ((EditText)findViewById(R.id.editTextTitleInViewActivity)).setText(title);
        ((EditText)findViewById(R.id.editTextContentInViewActivity)).setText(content);

        FloatingActionButton decrypt = findViewById(R.id.floatingActionButtonDecrypt);
        FloatingActionButton save = findViewById(R.id.floatingActionButtonSaveInViewScreen);
        decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!decrypted){
                    startActivityForResult(new Intent(getApplicationContext(),FingerprintActivity.class),responseFromFingerprint);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedEncryptedContent = "";
                ContentValues values = new ContentValues();
                if(!decrypted)
                    Toast.makeText(getApplicationContext(),
                            "Decrypt your content before updating" ,
                            Toast.LENGTH_LONG).show();
                else{
                    EnNotesUserDbHelper notesDbHelper = new EnNotesUserDbHelper(getApplicationContext());
                    SQLiteDatabase userdb = notesDbHelper.getWritableDatabase();

                    String updatedTitle = ((EditText) findViewById(R.id.editTextTitleInViewActivity)).getText().toString();
                    String updatedContent = ((EditText) findViewById(R.id.editTextContentInViewActivity)).getText().toString();
                    try{
                        updatedEncryptedContent = CryptoEnNotes.encrypt(getApplicationContext(),updatedContent);
                        values.put(EnNotesContract.EnNotesEntry.ID,id);
                        values.put(EnNotesContract.EnNotesEntry.TITLE,updatedTitle);
                        values.put(EnNotesContract.EnNotesEntry.COLUMN_CONTENT,updatedEncryptedContent);
                        userdb.update(EnNotesContract.EnNotesEntry.TABLE_NAME,values,
                                "_id="+id,null);
                    }
                    catch(Exception e){}

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == responseFromFingerprint) {
            if (resultCode == RESULT_OK) {
                int fingerprintResult = data.getIntExtra("result",9);

                switch (fingerprintResult) {
                    // Fingerprint passed
                    case 1:
                        decrypted=true;
                        try{
                            decryptedText = CryptoEnNotes.decrypt(getApplicationContext(),content);
                            ((EditText)findViewById(R.id.editTextContentInViewActivity)).setText(decryptedText);
                        }
                        catch (Exception e){}
                        break;
                    // Fingerprint failed
                    case 0:
                        Toast.makeText(this, "Nah Nah Nah, show me your fingerprint again", Toast.LENGTH_SHORT).show();
                        break;
                    // If not both then it timed-out
                    default:
                        Toast.makeText(this, "Got to make up your mind, decrypt or not?", Toast.LENGTH_SHORT).show();
                        break;
                }
            } else{
                // If Activity did'nt return with RESULT_OK
                Toast.makeText(this, "Fingerprint Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
