/*
* This is the main activity class that shows all the notes user
* has in the database
* Notes are shown by default in encrypted format
*/
package bmurali.ennotes.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.LinkedList;

import bmurali.ennotes.R;
import bmurali.ennotes.database.EnNotesDbHelper;
import bmurali.ennotes.encryption.CryptoEnNotes;

public class MainActivity extends AppCompatActivity {

  private final LinkedList<String> notesList = new LinkedList<>();
  private final LinkedList<String> notesTitle = new LinkedList<>();
  private final LinkedList<Integer> notesID = new LinkedList<>();
  private RecyclerView mRecyclerView;
  private NotesAdapter mAdapter;
  String decryptedText;

  private View.OnClickListener onItemClickListener =
      new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      // This viewHolder will have all required values.
      RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder)
          view.getTag();
      int position = viewHolder.getAdapterPosition();
      String noteTitle = notesTitle.get(position);
      String noteContent = notesList.get(position);
      Integer noteID = notesID.get(position);
      Intent intent = new Intent(getApplicationContext(),
          ActivityViewNote.class);
      intent.putExtra("NOTE_TITLE", noteTitle);
      intent.putExtra("NOTE_CONTENT", noteContent);
      intent.putExtra("NOTE_ID", noteID);
      startActivity(intent);
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ((Toolbar) findViewById(R.id.toolbar)).setTitle("Notes");
    ((Toolbar) findViewById(R.id.toolbar)).setTitleTextColor
        (getResources().getColor(R.color.white));
    EnNotesDbHelper notesDbHelper = new EnNotesDbHelper(this);
    SQLiteDatabase userdb = notesDbHelper.getReadableDatabase();

    Cursor notes_db = userdb.rawQuery("select Title,Content,_id from Notes",
        null);
    notes_db.moveToFirst();

    try {
      for (int i = 0; i < notes_db.getCount(); i++) {
        decryptedText = CryptoEnNotes.decrypt(this,
            notes_db.getString(1));
        notesList.addLast(notes_db.getString(1));
        notesTitle.addLast(notes_db.getString(0));
        notesID.addLast(notes_db.getInt(2));
        notes_db.moveToNext();
      }
    } catch (Exception e) {}

    mRecyclerView = findViewById(R.id.recyclerview);
    mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
        StaggeredGridLayoutManager.VERTICAL));
    mAdapter = new NotesAdapter(this, notesList, notesTitle, notesID);
    mRecyclerView.setAdapter(mAdapter);

    mAdapter.setOnItemClickListener(onItemClickListener);


    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), NewNote.class);
        startActivity(intent);
        finish();
      }
    });
  }

}
