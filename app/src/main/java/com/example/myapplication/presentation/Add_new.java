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

import com.example.myapplication.R;

import com.example.myapplication.data.db.NoteDatabase;

import com.example.myapplication.data.repository.NoteRepositoryImpl;
import com.example.myapplication.domain.model.Note;
import com.example.myapplication.domain.model.repository.NoteRepository;
import com.example.myapplication.domain.model.usecase.InsertNoteUseCase;
import com.example.myapplication.domain.model.usecase.UpdateNoteUseCase;
import com.example.myapplication.domain.model.usecase.GetByIdNoteUseCase;

public class Add_new extends AppCompatActivity {
    Button btn_ok, btn_cancel;
    EditText title, desc, priority;
    TextView notes_id;
    int id;

    private InsertNoteUseCase insertNoteUseCase;
    private UpdateNoteUseCase updateNoteUseCase;
    private GetByIdNoteUseCase getNoteByIdUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new);


        NoteDatabase db = NoteDatabase.getInstance(this);
        NoteRepository repository = new NoteRepositoryImpl(db.noteDao());

        insertNoteUseCase = new InsertNoteUseCase(repository);
        updateNoteUseCase = new UpdateNoteUseCase(repository);
        getNoteByIdUseCase = new GetByIdNoteUseCase(repository);

        btn_ok = findViewById(R.id.save);
        btn_cancel = findViewById(R.id.cancel);

        title = findViewById(R.id.current_name);
        desc = findViewById(R.id.desc_content);
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
            String priorityText = priority.getText().toString().trim();

            if (titleText.isEmpty()) {
                title.setError("Введите заголовок");
                return;
            }

            if (id >= 0) {
                new LoadAndUpdateNoteTask((int) id, titleText, descText, priorityText).execute();
            } else {
                Note newNote = new Note(0, titleText, descText, priorityText);
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
            android.util.Log.d("Add_new", "=== ЗАГРУЗКА ЗАМЕТКИ ID: " + noteId);
            try {
                Note note = getNoteByIdUseCase.execute(noteId);
                android.util.Log.d("Add_new", "Заметка: " + (note != null ? note.getTittle() : "null"));
                return note;
            } catch (Exception e) {
                android.util.Log.e("Add_new", "Ошибка загрузки", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Note note) {
            super.onPostExecute(note);
            if (note != null) {
                title.setText(note.getTittle());
                desc.setText(note.getDescription());
                priority.setText(note.getPriority());
                notes_id.setText(String.valueOf(note.getId()));
            }
        }
    }

    private class InsertNoteTask extends AsyncTask<Note, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Note... notes) {
            try {
                return insertNoteUseCase.execute(notes[0]);
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
                //Intent intent = new Intent(Add_new.this, MainActivity.class);
                //startActivity(intent);
                finish();
            } else {
                Toast.makeText(Add_new.this, "Ошибка при сохранении", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadAndUpdateNoteTask extends AsyncTask<Void, Void, Boolean> {
        private final int id;
        private final String newTitle;
        private final String newDesc;
        private final String newPriority;

        // Конструктор принимает параметры
        LoadAndUpdateNoteTask(int id, String newTitle, String newDesc, String newPriority) {
            this.id = id;
            this.newTitle = newTitle;
            this.newDesc = newDesc;
            this.newPriority = newPriority;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                // Загружаем существующую заметку
                Note existingNote = getNoteByIdUseCase.execute(id);
                if (existingNote != null) {
                    // Создаем обновленную заметку с СОХРАНЕНИЕМ СТАРОЙ ДАТЫ
                    Note updatedNote = new Note(
                            existingNote.getId(),
                            newTitle,
                            existingNote.getDate(),
                            newDesc,
                            newPriority
                    );
                    return updateNoteUseCase.execute(updatedNote);
                }
                return false;
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
                //Intent intent = new Intent(Add_new.this, MainActivity.class);
                //startActivity(intent);
                finish();
            } else {
                Toast.makeText(Add_new.this, "Ошибка при обновлении", Toast.LENGTH_SHORT).show();
            }
        }
    }
}