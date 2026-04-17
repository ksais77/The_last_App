package com.example.myapplication.presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.data.db.NoteEntity;
import com.example.myapplication.data.db.NoteMapper;
import com.example.myapplication.domain.model.Note;
import com.example.myapplication.R;
import com.example.myapplication.data.db.NoteDao;
import com.example.myapplication.data.db.NoteDatabase;

public class Add_new extends AppCompatActivity {
    Button btn_ok, btn_cancel;
    EditText title, desc, date, priority;
    TextView notes_id;
    int id;
    NoteDatabase db;
    NoteDao notedao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new);

        db = NoteDatabase.getInstance(this);
        notedao = db.noteDao();

        btn_ok = findViewById(R.id.save);
        btn_cancel = findViewById(R.id.cancel);

        title = findViewById(R.id.current_name);
        desc = findViewById(R.id.desc_content);
        date = findViewById(R.id.date_content);
        priority = findViewById(R.id.priority_content);
        notes_id = findViewById(R.id.id_current);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        if (id >= 0) {
            new LoadNoteTask().execute(id);
        }

        btn_ok.setOnClickListener(v -> {
            String titleText = title.getText().toString().trim();
            String descText = desc.getText().toString().trim();
            String dateText = date.getText().toString().trim();
            String priorityText = priority.getText().toString().trim();


            if (titleText.isEmpty()) {
                title.setError("Введите заголовок");
                return;
            }

            if (id >= 0) {
                Note updateNote = new Note(id, titleText, descText, dateText, priorityText);
                new UpdateNoteTask().execute(updateNote);
            } else {
                Note newNote = new Note(0, titleText, descText, dateText, priorityText);
                new InsertNoteTask().execute(newNote);
            }
        });

        btn_cancel.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadNoteTask extends AsyncTask<Integer, Void, Note> {
        @Override
        protected Note doInBackground(Integer... params) {
            int noteId = params[0];
            NoteEntity entity = notedao.getById(noteId);
            return NoteMapper.toDomain(entity);
        }

        @Override
        protected void onPostExecute(Note note) {
            super.onPostExecute(note);
            if (note != null) {
                title.setText(note.getTittle());
                desc.setText(note.getDescription());
                date.setText(note.getDate());
                priority.setText(note.getPriority());
                notes_id.setText(String.valueOf(note.getId()));
            }
        }
    }

    private class InsertNoteTask extends AsyncTask<Note, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Note... notes) {
            try {
                NoteEntity entity = NoteMapper.toEntity(notes[0]);
                notedao.insertAll(entity);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                Toast.makeText(Add_new.this, "Заметка сохранена", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Add_new.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Add_new.this, "Ошибка при сохранении", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class UpdateNoteTask extends AsyncTask<Note, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Note... notes) {
            try {
                NoteEntity entity = NoteMapper.toEntity(notes[0]);
                notedao.update(entity);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                Toast.makeText(Add_new.this, "Заметка обновлена", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Add_new.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Add_new.this, "Ошибка при обновлении", Toast.LENGTH_SHORT).show();
            }
        }
    }
}