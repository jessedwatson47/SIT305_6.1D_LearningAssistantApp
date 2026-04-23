package com.example.learningassistantapp;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {User.class, Task.class, Question.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract TaskDao taskDao();

    public abstract QuestionDao questionDao();
}