package com.example.myapplication.domain.model.usecase;

import com.example.myapplication.domain.model.Note;
import com.example.myapplication.domain.model.repository.NoteRepository;

import java.util.Collections;
import java.util.List;

public class GetAllNotesUseCase {
    private final NoteRepository repository;

    public GetAllNotesUseCase(NoteRepository repository) {
        this.repository = repository;
    }

    public List<Note> execute() {
        List<Note> notes = repository.getAllNotes();

        if (notes != null && notes.size() > 1) {
            notes.sort((n1, n2) -> {
                int priority1 = getPriorityWeight(n1.getPriority());
                int priority2 = getPriorityWeight(n2.getPriority());
                return Integer.compare(priority1, priority2);
            });
        }

        return notes;
    }

    private int getPriorityWeight(String priority) {
        if (priority.equals("важно")) return 1;
        if (priority.equals("средне")) return 2;
        return 3;
    }
}
