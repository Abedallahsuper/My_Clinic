package com.example.myclinic.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import com.example.myclinic.db.entities.Doctor;

import java.util.List;

@Dao
public interface DoctorDao {

    @Insert
    void insert(Doctor doctor);

    @Update
    void update(Doctor doctor);

    @Delete
    void delete(Doctor doctor);

    @Query("SELECT * FROM doctors WHERE id = :id")
    LiveData<Doctor> getDoctorById(int id);

    @Query("SELECT * FROM doctors")
    LiveData<List<Doctor>> getAllDoctors();

    @Query("SELECT * FROM doctors WHERE category = :category")
    LiveData<List<Doctor>> getDoctorsByCategory(String category);
}
