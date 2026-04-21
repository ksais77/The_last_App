package com.example.myapplication.domain.model.usecase;

import com.example.myapplication.domain.model.Note;
import com.example.myapplication.domain.model.repository.NoteRepository;

public class DeleteNoteUseCase {
    private final NoteRepository repository;

    public DeleteNoteUseCase(NoteRepository repository) {
        this.repository = repository;
    }

    public boolean execute(Note note) {
        if (note == null) {
            return false;
        }


        if (note.getId() <= 0) {
            return false;
        }

        repository.deleteNote(note);
        return true;
    }
}
