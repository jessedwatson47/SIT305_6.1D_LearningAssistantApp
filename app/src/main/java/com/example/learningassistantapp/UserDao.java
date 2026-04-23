package com.example.learningassistantapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE id IN (:Id)")
    User getById(int Id);

    @Query("SELECT * FROM user WHERE username = :username")
    boolean checkUsername(String username);

    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    User login(String username, String password);


    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

    @Update
    void update(User user);
}