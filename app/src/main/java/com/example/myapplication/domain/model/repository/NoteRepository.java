package com.example.myapplication.domain.model.repository;

import com.example.myapplication.domain.model.Note;

import java.util.List;

public abstract class NoteRepository {
    public abstract List<Note> getAllNotes();

    public abstract Note getNoteById(int id);

    public abstract void insertNote(Note note);

    public abstract void updateNote(Note note);

    public abstract void deleteNote(Note note);
}
