package com.example.myapplication.data.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;




import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM notes")
    List<NoteEntity> getAll();

    @Query("SELECT * FROM notes WHERE id IN (:noteid)")
    List<NoteEntity> loadAllByIds(int[] noteid);

    @Query("SELECT * FROM notes WHERE id = :id")
    NoteEntity getById(long id);

    @Query("SELECT * FROM notes WHERE title LIKE :title AND " +
            "description LIKE :description LIMIT 1")
    NoteEntity findByName(String title, String description);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(NoteEntity... notes);

    @Delete
    void delete(NoteEntity note);

    @Update
    void update(NoteEntity entity);
    @Query("SELECT * FROM notes ORDER BY title ASC")
    List<NoteEntity> getAllSortedByTitleAsc();

    // Сортировка по названию Я → А
    @Query("SELECT * FROM notes ORDER BY title DESC")
    List<NoteEntity> getAllSortedByTitleDesc();

    // Сортировка по дате (старые → новые)
    @Query("SELECT * FROM notes ORDER BY date ASC")
    List<NoteEntity> getAllSortedByDateAsc();

    // Сортировка по дате (новые → старые)
    @Query("SELECT * FROM notes ORDER BY date DESC")
    List<NoteEntity> getAllSortedByDateDesc();
}