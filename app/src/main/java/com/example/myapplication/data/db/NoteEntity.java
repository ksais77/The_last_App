package com.example.myapplication.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "notes")
public class NoteEntity {

    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    private  String description;

    @ColumnInfo(name = "date")
    private  long date;

    @ColumnInfo(name = "priority")
    private  String priority;


    public NoteEntity(String title,String description, String priority){
        this.title=title;
        this.date = System.currentTimeMillis();
        this.description=description;
        this.priority=priority;

    }
    public NoteEntity(int id, String title, String description, long date, String priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.priority = priority;
    }


    public NoteEntity() {
    }

    @NonNull
    @Override
    public String toString(){
        return "Заметка: ("+
                "id="+id+
                "заголовок="+title+'\''+
                "дата="+date+
                "описание="+description+'\''+
                ')';

    }
    public int getId(){
        return id;
    }
    public String getTittle() {
        return title;
    }

    public long getDate() {
        return date;
    }
    public String getFormattedDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault());
        return sdf.format(new Date(date));
    }
    public String getDescription() {
        return description;
    }
    public String getPriority() {
        return priority;
    }

    public void setId(int id) { this.id = id; }
    public void setTittle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

}
