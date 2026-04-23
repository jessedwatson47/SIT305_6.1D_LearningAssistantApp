package com.example.learningassistantapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM tasks")
    List<Task> getAll();

    @Query("SELECT * FROM tasks WHERE id IN (:Id)")
    Task getById(int Id);

    @Query("SELECT * FROM tasks WHERE userId = :userId")
    List<Task> getByUserId(int userId);

    @Insert
    void insertAll(Task... tasks);

    @Insert
    long insert(Task task);

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);
}