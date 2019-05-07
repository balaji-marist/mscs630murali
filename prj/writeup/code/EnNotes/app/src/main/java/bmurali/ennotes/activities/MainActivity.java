package bmurali.ennotes.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.LinkedList;

import bmurali.ennotes.R;
import bmurali.ennotes.database.EnNotesDbHelper;
import bmurali.ennotes.encryption.CryptoEnNotes;

public class MainActivity extends AppCompatActivity {

    private final LinkedList<String> notesList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private NotesAdapter mAdapter;
    String decryptedText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        EnNotesDbHelper notesDbHelper = new EnNotesDbHelper(this);
        SQLiteDatabase userdb = notesDbHelper.getReadableDatabase();

        Cursor notes_db = userdb.rawQuery("select Content from Notes",null);
        notes_db.moveToFirst();

        try{
            // Put initial data into the word list.
            for (int i = 0; i < notes_db.getCount(); i++) {
                decryptedText = notes_db.getString(0);
                notesList.addLast(decryptedText);
                notes_db.moveToNext();
            }
        }
        catch (Exception e){
        }


        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new NotesAdapter(this, notesList);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),NewNote.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
