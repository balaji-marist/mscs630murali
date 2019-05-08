package bmurali.ennotes.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

import bmurali.ennotes.R;

public class NotesAdapter extends
        RecyclerView.Adapter<NotesAdapter.NotesViewHolder>  {

    private final LinkedList<String> notesContentList;
    private final LinkedList<String> notesTitleList;
    private LayoutInflater mInflater;


    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View mItemView = mInflater.inflate(R.layout.noteslist_item,
                parent, false);
        return new NotesViewHolder(mItemView, this);
    }

    public NotesAdapter(Context context,
                        LinkedList<String> notesContentList,LinkedList<String> notesTitleList) {
        mInflater = LayoutInflater.from(context);
        this.notesContentList = notesContentList;
        this.notesTitleList = notesTitleList;
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {
        public final TextView mNoteViewContent,mNoteViewTitle;
        final NotesAdapter mAdapter;

        public NotesViewHolder(View itemView, NotesAdapter adapter) {
            super(itemView);
            mNoteViewTitle = itemView.findViewById(R.id.notes_title);
            mNoteViewContent = itemView.findViewById(R.id.notes_content);
            this.mAdapter = adapter;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        String mNotesContent = notesContentList.get(position);
        String mNotesTitle = notesTitleList.get(position);
        holder.mNoteViewTitle.setText(mNotesTitle);
        holder.mNoteViewContent.setText(mNotesContent);
    }

    @Override
    public int getItemCount() {
        return notesContentList.size();
    }
}
