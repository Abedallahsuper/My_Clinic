package com.example.myclinic.db.appdatabase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myclinic.db.dao.UserDao;
import com.example.myclinic.db.dao.DoctorDao;
import com.example.myclinic.db.dao.AppointmentDao;
import com.example.myclinic.db.dao.RecordDao;
import com.example.myclinic.db.entities.User;
import com.example.myclinic.db.entities.Doctor;
import com.example.myclinic.db.entities.Appointment;
import com.example.myclinic.db.entities.MedicalRecord;
import com.example.myclinic.utils.Converter;

import java.util.concurrent.Executors;

@Database(
        entities = {User.class, Doctor.class, Appointment.class, MedicalRecord.class},
        version = 6,
        exportSchema = false
)

@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract DoctorDao doctorDao();
    public abstract AppointmentDao appointmentDao();
    public abstract RecordDao recordDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "clinic_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);

                            Executors.newSingleThreadExecutor().execute(() -> {
                                DoctorDao doctorDao = instance.doctorDao();
                                doctorDao.insert(new Doctor("Dr. Ahmed", "General", "Experienced general doctor", "", 4.5f));
                                doctorDao.insert(new Doctor("Dr. Eman", "Dental", "Specialist in dental care", "", 4.7f));
                                doctorDao.insert(new Doctor("Dr. Abedallah", "Dermatology", "Skin care specialist", "", 4.6f));
                                doctorDao.insert(new Doctor("Dr. Mona", "Pediatrics", "Child specialist doctor", "", 4.8f));
                            });
                        }
                    })
                    .build();
        }
        return instance;
    }
}
