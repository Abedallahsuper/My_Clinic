package com.example.myclinic.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclinic.R;
import com.example.myclinic.adapters.DoctorAdapter;
import com.example.myclinic.db.entities.Appointment;
import com.example.myclinic.db.entities.Doctor;
import com.example.myclinic.db.viewmodel.ClinicViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BrowseDoctorsFragment extends Fragment {

    private RecyclerView recyclerDoctors;
    private DoctorAdapter adapter;
    private ClinicViewModel viewModel;
    private Spinner spinnerCategory;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "clinic_prefs";
    private static final String KEY_USER_ID = "user_id";

    private List<Doctor> allDoctors = new ArrayList<>();
    private List<String> categories = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse_doctors, container, false);

        recyclerDoctors = view.findViewById(R.id.recyclerDoctors);
        recyclerDoctors.setLayoutManager(new LinearLayoutManager(getContext()));

        spinnerCategory = view.findViewById(R.id.spinnerCategory);

        adapter = new DoctorAdapter(new ArrayList<>());
        recyclerDoctors.setAdapter(adapter);

        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        viewModel = new ViewModelProvider(this).get(ClinicViewModel.class);

        viewModel.getAllDoctors().observe(getViewLifecycleOwner(), doctors -> {
            allDoctors = doctors != null ? doctors : new ArrayList<>();
            adapter.setDoctors(allDoctors);
            updateSpinnerCategories();
        });

        adapter.setOnItemClickListener(doctor -> {
            int userId = sharedPreferences.getInt(KEY_USER_ID, -1);
            if(userId == -1) {
                Toast.makeText(getContext(), "Please log in first", Toast.LENGTH_SHORT).show();
                return;
            }

            long selectedTimestamp = System.currentTimeMillis() + 3600000;

            Appointment appt = new Appointment();
            appt.userId = userId;
            appt.doctorId = doctor.getId();
            appt.timestamp = new Date(selectedTimestamp);
            appt.status = "Pending";
            appt.notes = "Appointment with " + doctor.getName();

            viewModel.insertAppointment(appt);

            Toast.makeText(getContext(), "Appointment booked with: " + doctor.getName(), Toast.LENGTH_SHORT).show();

            sendAppointmentNotification(doctor.getName());
        });

        return view;
    }

    private void updateSpinnerCategories() {
        categories.clear();
        categories.add("All");
        for (Doctor d : allDoctors) {
            if (d.getCategory() != null && !categories.contains(d.getCategory())) {
                categories.add(d.getCategory());
            }
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = categories.get(position);
                if (selected.equals("All")) {
                    adapter.setDoctors(allDoctors);
                } else {
                    List<Doctor> filtered = new ArrayList<>();
                    for(Doctor d : allDoctors) {
                        if(d.getCategory() != null && d.getCategory().equals(selected)) filtered.add(d);
                    }
                    adapter.setDoctors(filtered);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void sendAppointmentNotification(String doctorName) {
        NotificationManager notificationManager = (NotificationManager)
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(requireContext(), "clinic_channel_id")
                .setContentTitle("Appointment Booked")
                .setContentText("Your appointment with Dr. " + doctorName + " has been booked")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .build();

        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }
}
