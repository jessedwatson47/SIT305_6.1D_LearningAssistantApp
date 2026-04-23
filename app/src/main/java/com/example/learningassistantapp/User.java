package com.example.learningassistantapp;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "phone_number")
    public String phone_number;

    @ColumnInfo(name = "interests")
    public String interests;

    public User(String username, String password, String email, String phone_number, String interests)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone_number = phone_number;
        this.interests = interests;
    }

}