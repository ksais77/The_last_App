package com.example.myapplication.presentation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.db.NoteDao;
import com.example.myapplication.data.db.NoteDatabase;
import com.example.myapplication.data.db.NoteEntity;
import com.example.myapplication.data.db.NoteMapper;
import com.example.myapplication.domain.model.Note;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notes;
    private final Context context;
    private final NoteDao noteDao;

    public NotesAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
        NoteDatabase db = NoteDatabase.getInstance(context);
        noteDao = db.noteDao();
    }
    public void updateNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, date;
        CardView card;

        public NoteViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            desc = itemView.findViewById(R.id.description);
            card = itemView.findViewById(R.id.card);
        }
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Note note = notes.get(position);

        holder.title.setText(note.getTittle());
        holder.date.setText(note.getFormattedDate());
        holder.desc.setText(note.getDescription());

        if (note.getPriority().equals("важно")) {
            holder.card.setCardBackgroundColor(Color.RED);
        } else if (note.getPriority().equals("средне")) {
            holder.card.setCardBackgroundColor(Color.YELLOW);
        } else {
            holder.card.setCardBackgroundColor(Color.GREEN);
        }

        holder.itemView.setOnClickListener(v -> {
            int noteId = notes.get(position).getId();
            String noteTitle = notes.get(position).getTittle();

            android.util.Log.d("DEBUG", "=== НАЖАТИЕ НА ЗАМЕТКУ ===");
            android.util.Log.d("DEBUG", "Позиция: " + position);
            android.util.Log.d("DEBUG", "ID из notes.get(position).getId(): " + noteId);
            android.util.Log.d("DEBUG", "Заголовок: " + noteTitle);
            android.util.Log.d("DEBUG", "Весь объект Note: " + notes.get(position).toString());

            Intent intent = new Intent(context, Add_new.class);
            intent.putExtra("id", noteId);
            context.startActivity(intent);
        });
        holder.itemView.setOnLongClickListener(v -> {
            int position_delete = holder.getAdapterPosition();
            if (position_delete != RecyclerView.NO_POSITION) {
                new DeleteNoteTask(position_delete).execute(notes.get(position_delete));
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
    public void updateList(List<Note> newNotes) {
        this.notes.clear();
        this.notes.addAll(newNotes);
        notifyDataSetChanged();
    }


    private class DeleteNoteTask extends AsyncTask<Note, Void, Boolean> {
        private int position;

        DeleteNoteTask(int position) {
            this.position = position;
        }

        @Override
        protected Boolean doInBackground(Note... notes) {
            try {
                NoteEntity entity = NoteMapper.toEntity(notes[0]);
                noteDao.delete(entity);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                notes.remove(position);
                notifyItemRemoved(position);
            } else {
                Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show();
            }
        }

    }
}