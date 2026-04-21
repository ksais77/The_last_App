package com.example.myapplication.domain.model.usecase;

import com.example.myapplication.domain.model.Note;
import com.example.myapplication.domain.model.repository.NoteRepository;

public class GetByIdNoteUseCase {
    private final NoteRepository repository;

    public GetByIdNoteUseCase(NoteRepository repository) {
        this.repository = repository;
    }

    public Note execute(int id) {
        if (id <= 0) {
            return null;
        }

        return repository.getNoteById(id);
    }
}
