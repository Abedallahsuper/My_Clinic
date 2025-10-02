package com.example.myclinic.db.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(
        tableName = "appointments",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId"),
                @ForeignKey(entity = Doctor.class, parentColumns = "id", childColumns = "doctorId")
        },
        indices = {@Index("userId"), @Index("doctorId")}
)
public class Appointment {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;
    public int doctorId;
    public Date timestamp;
    public String status;
    public String notes;
}
