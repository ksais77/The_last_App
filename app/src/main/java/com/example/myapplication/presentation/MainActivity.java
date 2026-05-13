package com.example.myapplication.presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
    private SharedPreferences prefs;
    private String currentSort = "dateDesc";

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Инициализация SharedPreferences
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        currentSort = prefs.getString("sort_type", "dateDesc");

        // Загружаем заметки с сохраненной сортировкой
        new LoadNotesTask(currentSort).execute();

        btn_add = findViewById(R.id.add_one);
        btn_add.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Add_new.class)));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.sort_menu,menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_aToz) {
            currentSort = "titleAsc";
            prefs.edit().putString("sort_type", currentSort).apply();
            new LoadNotesTask(currentSort).execute();
            Toast.makeText(this, "Сортировка: А → Я", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_zToa) {
            currentSort = "titleDesc";
            prefs.edit().putString("sort_type", currentSort).apply();
            new LoadNotesTask(currentSort).execute();
            Toast.makeText(this, "Сортировка: Я → А", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_dateAsc) {
            currentSort = "dateAsc";
            prefs.edit().putString("sort_type", currentSort).apply();
            new LoadNotesTask(currentSort).execute();
            Toast.makeText(this, "Сортировка: старые → новые", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.menu_dateDesc) {
            currentSort = "dateDesc";
            prefs.edit().putString("sort_type", currentSort).apply();
            new LoadNotesTask(currentSort).execute();
            Toast.makeText(this, "Сортировка: новые → старые", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        new LoadNotesTask(currentSort).execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadNotesTask extends AsyncTask<Void, Void, List<Note>> {
        private String sortType;

        // Конструктор с параметром
        LoadNotesTask(String sortType) {
            this.sortType = sortType;
        }
        @Override
        protected List<Note> doInBackground(Void... voids) {
            try {
                android.util.Log.d("MainActivity", "=== НАЧАЛО ЗАГРУЗКИ с сортировкой: " + sortType);

                List<NoteEntity> entities = null;

                // Выбираем нужный метод сортировки
                switch (sortType) {
                    case "titleAsc":
                        entities = notedao.getAllSortedByTitleAsc();
                        break;
                    case "titleDesc":
                        entities = notedao.getAllSortedByTitleDesc();
                        break;
                    case "dateAsc":
                        entities = notedao.getAllSortedByDateAsc();
                        break;
                    default:
                        entities = notedao.getAllSortedByDateDesc();
                        break;
                }

                // Убираем дублирование! Здесь уже есть entities из switch
                android.util.Log.d("MainActivity", "entities получены, размер: " + (entities != null ? entities.size() : "null"));

                if (entities == null) {
                    android.util.Log.e("MainActivity", "entities = null");
                    return null;
                }

                android.util.Log.d("MainActivity", "Вызов NoteMapper.toDomainList()...");
                List<Note> notes = NoteMapper.toDomainList(entities);
                android.util.Log.d("MainActivity", "notes получены, размер: " + (notes != null ? notes.size() : "null"));

                android.util.Log.d("MainActivity", "=== ЗАГРУЗКА УСПЕШНА ===");
                return notes;
            } catch (Exception e) {
                android.util.Log.e("MainActivity", "!!! ОШИБКА В doInBackground: " + e.getMessage());
                android.util.Log.e("MainActivity", "Тип ошибки: " + e.getClass().getName());
                android.util.Log.e("MainActivity", "Стек трейс: ", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            android.util.Log.d("MainActivity", "onPostExecute: notes = " + (notes != null ? notes.size() : "null"));

            if (notes == null) {
                Toast.makeText(MainActivity.this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
                return;
            }

            if (notes.isEmpty()) {
                Toast.makeText(MainActivity.this, "Нет заметок", Toast.LENGTH_SHORT).show();
            }

            notesAdapter = new NotesAdapter(notes, MainActivity.this);
            recyclerView.setAdapter(notesAdapter);
        }
    }
}