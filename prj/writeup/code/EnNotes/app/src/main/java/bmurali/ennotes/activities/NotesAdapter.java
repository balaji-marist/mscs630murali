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

    private final LinkedList<String> mWordList;
    private LayoutInflater mInflater;


    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View mItemView = mInflater.inflate(R.layout.noteslist_item,
                parent, false);
        return new NotesViewHolder(mItemView, this);
    }

    public NotesAdapter(Context context,
                        LinkedList<String> wordList) {
        mInflater = LayoutInflater.from(context);
        this.mWordList = wordList;
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {
        public final TextView wordItemView;
        final NotesAdapter mAdapter;

        public NotesViewHolder(View itemView, NotesAdapter adapter) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.word);
            this.mAdapter = adapter;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        String mCurrent = mWordList.get(position);
        holder.wordItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }
}
