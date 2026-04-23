package com.example.learningassistantapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface QuestionDao {

    @Query("SELECT * FROM questions WHERE taskid = :taskid")
    List<Question> getByTaskId(long taskid);

    @Insert
    void insertAll(Question... questions);

    @Insert
    long insert(Question question);

    @Delete
    void delete(Question question);

    @Update
    void update(Question question);
}