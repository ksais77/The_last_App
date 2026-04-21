package com.example.myapplication.domain.model.usecase;

import android.util.Log;

import com.example.myapplication.domain.model.Note;
import com.example.myapplication.domain.model.repository.NoteRepository;

public class UpdateNoteUseCase {
    private final NoteRepository repository;

    public UpdateNoteUseCase(NoteRepository repository) {
        this.repository = repository;
    }

    public boolean execute(Note note) {
        try {
            Log.d("UpdateNoteUseCase", "1. Проверка note == null");
            if (note == null) {
                Log.e("UpdateNoteUseCase", "note == null");
                return false;
            }

            Log.d("UpdateNoteUseCase", "2. ID заметки: " + note.getId());
            if (note.getId() <= 0) {
                Log.e("UpdateNoteUseCase", "ID заметки некорректный: " + note.getId());
                return false;
            }

            Log.d("UpdateNoteUseCase", "3. Заголовок: " + note.getTittle());
            Log.d("UpdateNoteUseCase", "4. Обновляем заметку...");
            repository.updateNote(note);
            Log.d("UpdateNoteUseCase", "5. Заметка обновлена успешно");
            return true;
        } catch (Exception e) {
            Log.e("UpdateNoteUseCase", "Ошибка: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
