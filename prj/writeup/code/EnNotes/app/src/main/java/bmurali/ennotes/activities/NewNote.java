package bmurali.ennotes.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.text.DateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

import bmurali.ennotes.database.*;

import bmurali.ennotes.R;
import bmurali.ennotes.encryption.CryptoEnNotes;

public class NewNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String dt = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date());


        TextView dtime = (TextView)findViewById(R.id.textViewDateTime);
        dtime.setText(dt);

        FloatingActionButton fabs = findViewById(R.id.floatingActionButtonSave);
        fabs.setOnClickListener(v -> {
            EditText title = (EditText)findViewById(R.id.editTextTitleInViewActivity);
            String str_title = title.getText().toString();
            EditText content = (EditText)findViewById(R.id.editTextContentInViewActivity);
            String str_content = content.getText().toString();
            String encrypted_content = "";
            try{
                encrypted_content = CryptoEnNotes.encrypt(this,str_content);
            }
            catch(NoSuchAlgorithmException|
            NoSuchPaddingException| InvalidAlgorithmParameterException| InvalidKeyException|
                    BadPaddingException| IllegalBlockSizeException| UnsupportedEncodingException e){

            }

            TextView date_stamp = (TextView)findViewById(R.id.textViewDateTime);
            String str_date_stamp = date_stamp.getText().toString();

            EnNotesDbHelper notesDbHelper = new EnNotesDbHelper(this);
            SQLiteDatabase notesdb = notesDbHelper.getWritableDatabase();
            notesDbHelper.createTable(notesdb);
            ContentValues values = new ContentValues();

            values.put(EnNotesContract.EnNotesEntry.TITLE,str_title);
            values.put(EnNotesContract.EnNotesEntry.COLUMN_CONTENT,encrypted_content);
            values.put(EnNotesContract.EnNotesEntry.COLUMN_DATE,str_date_stamp);

            long rowid = notesdb.insert(EnNotesContract.EnNotesEntry.TABLE_NAME,null,values);
            Toast.makeText(this, "Your secret is a secret now, only you can decrypt it." , Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();

        });

    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivityForResult(intent,0);
        return true;
    }
}
