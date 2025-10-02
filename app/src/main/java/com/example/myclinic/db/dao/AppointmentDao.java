package com.example.myclinic.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import com.example.myclinic.db.entities.Appointment;
import java.util.List;

@Dao
public interface AppointmentDao {

    @Insert
    void insert(Appointment appointment);

    @Update
    void update(Appointment appointment);

    @Delete
    void delete(Appointment appointment);

    @Query("SELECT * FROM appointments WHERE id = :id")
    LiveData<Appointment> getAppointmentById(int id);

    @Query("SELECT * FROM appointments WHERE userId = :userId ORDER BY timestamp DESC")
    LiveData<List<Appointment>> getAppointmentsForUser(int userId);
}
