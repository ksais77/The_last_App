package com.example.myapplication.data.db;

import android.util.Log;

import com.example.myapplication.domain.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteMapper {

    public static Note toDomain(NoteEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Note(
                entity.getId(),
                entity.getTittle(),
                entity.getDescription(),
                entity.getPriority()
        );
    }

    public static NoteEntity toEntity(Note note) {
        Log.d("NoteMapper", "toEntity: " + note.getTittle());
        if (note == null) {
            return null;
        }

        return new NoteEntity(
                note.getId(),
                note.getTittle(),
                note.getDescription(),
                note.getDate(),
                note.getPriority()

        );

    }


    public static List<Note> toDomainList(List<NoteEntity> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }

        List<Note> notes = new ArrayList<>();
        for (NoteEntity entity : entities) {
            notes.add(toDomain(entity));
        }
        return notes;
    }


    public static List<NoteEntity> toEntityList(List<Note> notes) {
        if (notes == null) {
            return new ArrayList<>();
        }

        List<NoteEntity> entities = new ArrayList<>();
        for (Note note : notes) {
            entities.add(toEntity(note));
        }
        return entities;
    }
}
