package com.example.myapplication.domain.model;

import androidx.annotation.NonNull;

import com.example.myapplication.data.db.NoteEntity;


public class Note {


    public int id;
    public String title;
    public String description;
    private  String date,priority;

    public Note(String title, String description, String date, String priority) {
        this.id = 0;
        this.title = title;
        this.description = description;
        this.date = date;
        this.priority = priority;
    }

    public Note(int id,String title,String date,String description,String priority){
        this.id=id;
        this.title=title;
        this.date=date;
        this.description=description;
        this.priority=priority;

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

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
    public String getPriority() {
        return priority;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setTittle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
