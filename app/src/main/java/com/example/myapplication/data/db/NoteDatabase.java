package com.example.myapplication.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;



@Database(entities = {NoteEntity.class}, version = 3)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();

    private static final String DB_NAME = "notes.db";
    private static volatile NoteDatabase INSTANCE = null;

    public synchronized static NoteDatabase getInstance(Context ctxt) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            ctxt.getApplicationContext(),
                            NoteDatabase.class,
                            DB_NAME
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}