package com.example.myclinic.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.myclinic.R;
import com.example.myclinic.activities.AuthActivity;
import com.example.myclinic.db.entities.User;
import com.example.myclinic.db.viewmodel.ClinicViewModel;

public class ProfileFragment extends Fragment {

    private ImageView imgProfile;
    private TextView tvUsername, tvEmail;
    private Button btnLogout, btnEdit;
    private ClinicViewModel viewModel;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "clinic_prefs";
    private static final String KEY_USER_ID = "user_id";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imgProfile = view.findViewById(R.id.imgProfile);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnEdit = view.findViewById(R.id.btnEdit);

        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt(KEY_USER_ID, -1);

        viewModel = new ViewModelProvider(this).get(ClinicViewModel.class);

        viewModel.getUserById(userId).observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                tvUsername.setText(user.username);
                tvEmail.setText(user.email);

                if (user.imageUri != null && !user.imageUri.isEmpty()) {
                    Glide.with(requireContext())
                            .load(user.imageUri)
                            .placeholder(R.drawable.profile1)
                            .into(imgProfile);
                }
            }
        });

        btnEdit.setOnClickListener(v -> {
            View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_profile, null);
            EditText etUsername = dialogView.findViewById(R.id.etUsername);
            EditText etEmail = dialogView.findViewById(R.id.etEmail);

            etUsername.setText(tvUsername.getText().toString());
            etEmail.setText(tvEmail.getText().toString());

            new AlertDialog.Builder(getContext())
                    .setTitle("Edit Profile")
                    .setView(dialogView)
                    .setPositiveButton("Save", (dialog, which) -> {
                        String newUsername = etUsername.getText().toString().trim();
                        String newEmail = etEmail.getText().toString().trim();

                        if (TextUtils.isEmpty(newUsername) || TextUtils.isEmpty(newEmail)) {
                            Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        viewModel.getUserById(userId).observe(getViewLifecycleOwner(), user -> {
                            if (user != null) {
                                user.username = newUsername;
                                user.email = newEmail;
                                viewModel.updateUser(user);

                                tvUsername.setText(newUsername);
                                tvEmail.setText(newEmail);

                                Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        });


        btnLogout.setOnClickListener(v -> {
            sharedPreferences.edit().clear().apply();
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }
}
