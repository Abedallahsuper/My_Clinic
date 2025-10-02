package com.example.myclinic.db.entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String username;

    @NonNull
    public String email;

    @NonNull
    public String password;

    public String fullName;

    public String phone;

    public String imageUri;
}
