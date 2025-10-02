package com.example.myclinic.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclinic.R;
import com.example.myclinic.adapters.MedicalRecordAdapter;
import com.example.myclinic.db.viewmodel.ClinicViewModel;

import java.util.ArrayList;

public class MedicalRecordsFragment extends Fragment {

    private RecyclerView recyclerRecords;
    private MedicalRecordAdapter adapter;
    private ClinicViewModel viewModel;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "clinic_prefs";
    private static final String KEY_USER_ID = "user_id";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medical_records, container, false);

        recyclerRecords = view.findViewById(R.id.recyclerRecords);
        recyclerRecords.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MedicalRecordAdapter(new ArrayList<>());
        recyclerRecords.setAdapter(adapter);

        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt(KEY_USER_ID, -1);

        viewModel = new ViewModelProvider(this).get(ClinicViewModel.class);

        if(userId != -1) {

            viewModel.insertDummyRecords(userId);

            viewModel.getRecordsForUser(userId).observe(getViewLifecycleOwner(), records -> {
                adapter.setRecords(records);
            });
        }

        return view;
    }
}
