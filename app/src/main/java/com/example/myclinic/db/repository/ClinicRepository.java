package com.example.myclinic.db.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.myclinic.db.appdatabase.AppDatabase;
import com.example.myclinic.db.dao.UserDao;
import com.example.myclinic.db.dao.DoctorDao;
import com.example.myclinic.db.dao.AppointmentDao;
import com.example.myclinic.db.dao.RecordDao;
import com.example.myclinic.db.entities.User;
import com.example.myclinic.db.entities.Doctor;
import com.example.myclinic.db.entities.Appointment;
import com.example.myclinic.db.entities.MedicalRecord;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ClinicRepository {

    private final UserDao userDao;
    private final DoctorDao doctorDao;
    private final AppointmentDao appointmentDao;
    private final RecordDao recordDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ClinicRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app);
        userDao = db.userDao();
        doctorDao = db.doctorDao();
        appointmentDao = db.appointmentDao();
        recordDao = db.recordDao();
    }

    public void insertUser(User user, Consumer<Long> callback) {
        executor.execute(() -> {
            long id = userDao.insert(user);
            callback.accept(id);
        });
    }

    public LiveData<User> getUserById(int id) { return userDao.getUserById(id); }
    public LiveData<User> login(String email, String password) { return userDao.login(email, password); }
    public LiveData<List<User>> getAllUsers() { return userDao.getAllUsers(); }
    public void updateUser(User user) {
        executor.execute(() -> userDao.update(user));
    }

    public LiveData<List<Doctor>> getAllDoctors() { return doctorDao.getAllDoctors(); }
    public LiveData<List<Doctor>> getDoctorsByCategory(String category) { return doctorDao.getDoctorsByCategory(category); }
    public void insertDoctor(Doctor doctor) { executor.execute(() -> doctorDao.insert(doctor)); }

    public void insertAppointment(Appointment appt) { executor.execute(() -> appointmentDao.insert(appt)); }
    public LiveData<List<Appointment>> getAppointmentsForUser(int userId) { return appointmentDao.getAppointmentsForUser(userId); }
    public void updateAppointment(Appointment appt) { executor.execute(() -> appointmentDao.update(appt)); }

    public void insertRecord(MedicalRecord record) { executor.execute(() -> recordDao.insert(record)); }
    public LiveData<List<MedicalRecord>> getRecordsForUser(int userId) { return recordDao.getRecordsForUser(userId); }

    public void insertDummyRecords(int userId) {
        executor.execute(() -> {
            List<MedicalRecord> existing = recordDao.getRecordsForUserSync(userId);
            if (existing == null || existing.isEmpty()) {
                recordDao.insert(new MedicalRecord(userId, "General Diagnosis", new Date(), "Initial Notes"));
                recordDao.insert(new MedicalRecord(userId, "Dental Checkup", new Date(), "Dental Notes"));
                recordDao.insert(new MedicalRecord(userId, "Skin Checkup", new Date(), "Dermatology Notes"));
            }
        });

    } }