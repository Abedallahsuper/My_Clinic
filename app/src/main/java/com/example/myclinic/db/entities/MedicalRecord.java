package com.example.myclinic.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "records")
public class MedicalRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;
    public int doctorId;
    public Date visitDate;
    public String diagnosis;
    public String prescription;
    public String attachments;

    public MedicalRecord(int userId, String diagnosis, Date visitDate, String prescription) {
        this.userId = userId;
        this.diagnosis = diagnosis;
        this.visitDate = visitDate;
        this.prescription = prescription;
        this.attachments = null;
    }
}
