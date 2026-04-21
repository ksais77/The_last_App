package com.example.myapplication.domain.model.usecase;

import android.util.Log;

import com.example.myapplication.domain.model.Note;
import com.example.myapplication.domain.model.repository.NoteRepository;

public class InsertNoteUseCase {
    private final NoteRepository repository;

    public InsertNoteUseCase(NoteRepository repository) {
        this.repository = repository;
    }

    public boolean execute(Note note) {
        try {
            Log.d("InsertNoteUseCase", "1. Проверка note == null");
            if (note == null) {
                Log.e("InsertNoteUseCase", "note == null");
                return false;
            }

            Log.d("InsertNoteUseCase", "2. Заголовок: " + note.getTittle());
            if (note.getTittle() == null || note.getTittle().trim().isEmpty()) {
                Log.e("InsertNoteUseCase", "Заголовок пустой");
                return false;
            }

            Log.d("InsertNoteUseCase", "3. Сохраняем заметку...");
            repository.insertNote(note);
            Log.d("InsertNoteUseCase", "4. Заметка сохранена успешно");
            return true;
        } catch (Exception e) {
            Log.e("InsertNoteUseCase", "Ошибка: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
