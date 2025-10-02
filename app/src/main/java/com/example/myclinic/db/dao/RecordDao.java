package com.example.myclinic.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.lifecycle.LiveData;
import com.example.myclinic.db.entities.MedicalRecord;
import java.util.List;

@Dao
public interface RecordDao  {

    @Insert
    void insert(MedicalRecord record);

    @Update
    void update(MedicalRecord record);

    @Delete
    void delete(MedicalRecord record);

    @Query("SELECT * FROM records WHERE userId = :userId ORDER BY visitDate DESC")
    LiveData<List<MedicalRecord>> getRecordsForUser(int userId);

    @Query("SELECT * FROM records WHERE userId = :userId")
    List<MedicalRecord> getRecordsForUserSync(int userId);
}
