package com.example.myapplication.presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.db.NoteEntity;
import com.example.myapplication.data.db.NoteMapper;
import com.example.myapplication.domain.model.Note;
import com.example.myapplication.data.db.NoteDao;
import com.example.myapplication.data.db.NoteDatabase;

import com.example.myapplication.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn_add;
    RecyclerView recyclerView;
    NotesAdapter notesAdapter;
    NoteDao notedao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        NoteDatabase db = NoteDatabase.getInstance(this);
        notedao = db.noteDao();

        new LoadNotesTask().execute();

        btn_add = findViewById(R.id.add_one);
        btn_add.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Add_new.class));
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadNotesTask().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadNotesTask extends AsyncTask<Void, Void, List<Note>> {
        @Override
        protected List<Note> doInBackground(Void... voids) {
            try {
                android.util.Log.d("MainActivity", "=== НАЧАЛО ЗАГРУЗКИ ===");

                android.util.Log.d("MainActivity", "1. Вызов notedao.getAll()...");
                List<NoteEntity> entities = notedao.getAll();
                android.util.Log.d("MainActivity", "2. entities получены, размер: " + (entities != null ? entities.size() : "null"));

                if (entities == null) {
                    android.util.Log.e("MainActivity", "entities = null");
                    return null;
                }

                android.util.Log.d("MainActivity", "3. Вызов NoteMapper.toDomainList()...");
                List<Note> notes = NoteMapper.toDomainList(entities);
                android.util.Log.d("MainActivity", "4. notes получены, размер: " + (notes != null ? notes.size() : "null"));

                android.util.Log.d("MainActivity", "=== ЗАГРУЗКА УСПЕШНА ===");
                return notes;
            } catch (Exception e) {
                android.util.Log.e("MainActivity", "!!! ОШИБКА В doInBackground: " + e.getMessage());
                android.util.Log.e("MainActivity", "Тип ошибки: " + e.getClass().getName());
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            android.util.Log.d("MainActivity", "onPostExecute: notes = " + (notes != null ? notes.size() : "null"));
            if (notes != null) {
                notesAdapter = new NotesAdapter(notes, MainActivity.this);
                recyclerView.setAdapter(notesAdapter);
                if (notes.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Нет заметок", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
            }
        }
    }
}