package com.example.learningassistantapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "questions")
public class Question
{
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "taskid")
    public long taskid;

    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "desc")
    public String desc;
    @ColumnInfo(name = "question")
    public String question;
    @ColumnInfo(name = "answer1")
    public String answer1;
    @ColumnInfo(name = "answer2")
    public String answer2;
    @ColumnInfo(name = "answer3")
    public String answer3;
    @ColumnInfo(name = "correctanswerid")
    public int correctanswerid;
    @ColumnInfo(name = "selectedanswerid")
    public int selectedanswerid;


    public Question(){}

    public Question(String title, String question, String answer1, String answer2, String answer3, int correctanswerid, long taskid)
    {
        this.title = title;
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.correctanswerid = correctanswerid;
        this.taskid = taskid;
    }

}