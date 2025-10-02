package com.example.myclinic.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclinic.R;
import com.example.myclinic.adapters.AppointmentAdapter;
import com.example.myclinic.db.entities.Appointment;
import com.example.myclinic.db.entities.Doctor;
import com.example.myclinic.db.viewmodel.ClinicViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentsFragment extends Fragment {

    private RecyclerView recyclerAppointments;
    private AppointmentAdapter adapter;
    private ClinicViewModel viewModel;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "clinic_prefs";
    private static final String KEY_USER_ID = "user_id";

    private List<Doctor> allDoctors = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);

        recyclerAppointments = view.findViewById(R.id.recyclerAppointments);
        recyclerAppointments.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt(KEY_USER_ID, -1);

        viewModel = new ViewModelProvider(this).get(ClinicViewModel.class);

        viewModel.getAllDoctors().observe(getViewLifecycleOwner(), doctors -> {
            allDoctors = doctors != null ? doctors : new ArrayList<>();
            if (adapter != null) {
                adapter.setDoctors(allDoctors);
            }
        });

        adapter = new AppointmentAdapter(new ArrayList<>(), allDoctors);
        recyclerAppointments.setAdapter(adapter);

        adapter.setOnItemClickListener((appointment, position) -> {
            appointment.status = "Cancelled";
            viewModel.updateAppointment(appointment);

            adapter.updateAppointmentAt(position, appointment);
            Toast.makeText(getContext(), "Appointment cancelled", Toast.LENGTH_SHORT).show();

            sendStatusNotification("Cancelled", getDoctorName(appointment));
        });

        if (userId != -1) {
            viewModel.getAppointmentsForUser(userId).observe(getViewLifecycleOwner(), appointments -> {
                adapter.setAppointments(appointments);
            });
        } else {
            Toast.makeText(getContext(), "Please log in first", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private String getDoctorName(Appointment appointment) {
        for (Doctor d : allDoctors) {
            if (d.getId() == appointment.doctorId) return d.getName();
        }
        return "Unknown Doctor";
    }

    private void sendStatusNotification(String status, String doctorName) {
        NotificationManager notificationManager = (NotificationManager)
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

        String contentText = "";
        switch (status) {
            case "Cancelled":
                contentText = "Your appointment with Dr. " + doctorName + " has been cancelled";
                break;
            case "Confirmed":
                contentText = "Your appointment with Dr. " + doctorName + " is confirmed";
                break;
            default:
                contentText = "Status of your appointment with Dr. " + doctorName + ": " + status;
        }

        Notification notification = new NotificationCompat.Builder(requireContext(), "clinic_channel_id")
                .setContentTitle("Appointment Update")
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .build();

        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }
}
