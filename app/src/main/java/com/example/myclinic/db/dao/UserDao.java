package com.example.myclinic.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import com.example.myclinic.db.entities.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    long insert(User user);


    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    LiveData<User> getUserById(int id);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    LiveData<User> login(String email, String password);

    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();
}
