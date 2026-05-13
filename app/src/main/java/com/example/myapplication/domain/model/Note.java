package com.example.myapplication.domain.model;

import androidx.annotation.NonNull;



import java.util.Comparator;


public class Note {


    public int id;
    public String title;
    public String description;
    private  String priority;
    private  long date;

    public Note(int id, String title, String description, String priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = System.currentTimeMillis();
        this.priority = priority;
    }
    public  static Comparator<Note> NoteNameAZComparator = Comparator.comparing(Note::getTittle);
    public  static Comparator<Note> NoteNameZAComparator = (n1, n2) -> n2.getTittle().compareTo(n1.getTittle());
    public  static Comparator<Note> NoteNameDateAscComparator = (n1, n2) -> Long.compare(n1.getDate(),n2.getDate());
    public static Comparator<Note> NoteDateDescComparator = (n1, n2) -> Long.compare(n2.getDate(), n1.getDate());

    public Note(int id, String title, long date, String description, String priority){
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
                "дата="+ getFormattedDate() +
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

    public void setDate(long date) {
        this.date = date;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
    public String getFormattedDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date(date));
    }
}
