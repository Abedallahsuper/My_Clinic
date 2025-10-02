package com.example.myclinic.db.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.myclinic.db.entities.Doctor;
import com.example.myclinic.db.entities.User;
import com.example.myclinic.db.entities.Appointment;
import com.example.myclinic.db.entities.MedicalRecord;
import com.example.myclinic.db.repository.ClinicRepository;
import java.util.List;

public class ClinicViewModel extends AndroidViewModel {

    private final ClinicRepository repository;

    public ClinicViewModel(@NonNull Application application) {
        super(application);
        repository = new ClinicRepository(application);
    }

    public LiveData<List<Doctor>> getAllDoctors() { return repository.getAllDoctors(); }
    public LiveData<List<Doctor>> getDoctorsByCategory(String category) { return repository.getDoctorsByCategory(category); }
    public void insertDoctor(Doctor doctor) { repository.insertDoctor(doctor); }

    public LiveData<User> getUserById(int id) { return repository.getUserById(id); }
    public LiveData<User> login(String email, String password) { return repository.login(email, password); }
    public void insertUser(User user, java.util.function.Consumer<Long> callback) { repository.insertUser(user, callback); }
    public void updateUser(User user) { repository.updateUser(user); }

    public void insertAppointment(Appointment appt) { repository.insertAppointment(appt); }
    public LiveData<List<Appointment>> getAppointmentsForUser(int userId) { return repository.getAppointmentsForUser(userId); }
    public void updateAppointment(Appointment appt) { repository.updateAppointment(appt); }

    public void insertRecord(MedicalRecord record) { repository.insertRecord(record); }
    public LiveData<List<MedicalRecord>> getRecordsForUser(int userId) { return repository.getRecordsForUser(userId); }

    public void insertDummyRecords(int userId) { repository.insertDummyRecords(userId); }
}
