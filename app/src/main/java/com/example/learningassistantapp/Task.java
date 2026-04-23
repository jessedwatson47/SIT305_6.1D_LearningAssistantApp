package com.example.learningassistantapp;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    public int userId;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "desc")
    public String desc;

    @Ignore
    public boolean isLoading;
    @Ignore
    public List<Question> questions;


    public Task(){}

    public Task(String title, String desc, int userId)
    {
        this.title = title;
        this.desc = desc;
        this.userId = userId;
    }

}