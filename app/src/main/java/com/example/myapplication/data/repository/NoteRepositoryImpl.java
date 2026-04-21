package com.example.myapplication.data.repository;

import android.util.Log;

import com.example.myapplication.data.db.NoteDao;
import com.example.myapplication.data.db.NoteEntity;
import com.example.myapplication.data.db.NoteMapper;
import com.example.myapplication.domain.model.Note;
import com.example.myapplication.domain.model.repository.NoteRepository;

import java.util.List;

public  class NoteRepositoryImpl extends NoteRepository {
    private final NoteDao noteDao;

    public NoteRepositoryImpl(NoteDao noteDao) {
        this.noteDao = noteDao;
    }
    @Override
    public List<Note> getAllNotes() {
        List<NoteEntity> entities = noteDao.getAll();
        return NoteMapper.toDomainList(entities);
    }

    @Override
    public Note getNoteById(int id) {
        NoteEntity entity = noteDao.getById(id);
        return NoteMapper.toDomain(entity);
    }

    @Override
    public void insertNote(Note note) {
        Log.d("NoteRepositoryImpl", "insertNote: " + note.getTittle());
        NoteEntity entity = NoteMapper.toEntity(note);
        Log.d("NoteRepositoryImpl", "Entity создан, title: " + entity.getTittle());
        noteDao.insertAll(entity);
        Log.d("NoteRepositoryImpl", "Вставка выполнена");
    }


    @Override
    public void updateNote(Note note) {
        Log.d("NoteRepositoryImpl", "updateNote: " + note.getTittle() + ", id=" + note.getId());
        NoteEntity entity = NoteMapper.toEntity(note);
        Log.d("NoteRepositoryImpl", "Entity создан, id=" + entity.getId());
        noteDao.update(entity);
        Log.d("NoteRepositoryImpl", "Обновление выполнено");
    }
    @Override
    public void deleteNote(Note note) {
        NoteEntity entity = NoteMapper.toEntity(note);
        noteDao.delete(entity);
    }

}
