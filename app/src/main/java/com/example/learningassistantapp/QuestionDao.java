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

    @Query("SELECT * FROM questions WHERE taskid = :taskId AND selectedanswerid != 0")
    List<Question> getAnsweredByTaskId(long taskId);

    @Query("SELECT COUNT(*) FROM questions WHERE selectedanswerid != 0")
    int getTotalAnsweredQuestions();

    @Query("SELECT COUNT(*) FROM questions WHERE selectedanswerid != 0 AND selectedanswerid = correctanswerid")
    int getCorrectAnswerCount();


    // Use for the profile summary (e.g. we query the AI to provide a short sentence on weak areas)
    @Query("SELECT * FROM questions WHERE selectedanswerid != 0 AND selectedanswerid != correctanswerid")
    List<Question> getIncorrectQuestions();

    @Insert
    void insertAll(Question... questions);

    @Insert
    long insert(Question question);

    @Delete
    void delete(Question question);

    @Update
    void update(Question question);
}