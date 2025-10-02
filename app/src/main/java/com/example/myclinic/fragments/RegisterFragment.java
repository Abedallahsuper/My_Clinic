package com.example.myclinic.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myclinic.R;
import com.example.myclinic.activities.AuthActivity;
import com.example.myclinic.activities.MainActivity;
import com.example.myclinic.db.entities.User;
import com.example.myclinic.db.viewmodel.ClinicViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragment extends Fragment {

    private TextInputEditText etUsername, etEmail, etPassword, etFullName, etPhone;
    private MaterialButton btnRegister;
    private ClinicViewModel viewModel;

    private static final String PREF_NAME = "clinic_prefs";
    private static final String KEY_USER_ID = "user_id";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        TextInputLayout tilUsername = view.findViewById(R.id.etUsername);
        TextInputLayout tilEmail = view.findViewById(R.id.etEmail);
        TextInputLayout tilPassword = view.findViewById(R.id.etPassword);
        TextInputLayout tilFullName = view.findViewById(R.id.etFullName);
        TextInputLayout tilPhone = view.findViewById(R.id.etPhone);

        etUsername = (TextInputEditText) tilUsername.getEditText();
        etEmail = (TextInputEditText) tilEmail.getEditText();
        etPassword = (TextInputEditText) tilPassword.getEditText();
        etFullName = (TextInputEditText) tilFullName.getEditText();
        etPhone = (TextInputEditText) tilPhone.getEditText();

        btnRegister = view.findViewById(R.id.btnRegister);
        viewModel = new ViewModelProvider(this).get(ClinicViewModel.class);

        btnRegister.setOnClickListener(v -> registerUser());

        return view;
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "املأ الحقول الأساسية", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.username = username;
        user.email = email;
        user.password = password;
        user.fullName = fullName;
        user.phone = phone;

        viewModel.insertUser(user, id -> {
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "تم التسجيل بنجاح", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(requireActivity(), AuthActivity.class);
                intent.putExtra("openLogin", true);
                startActivity(intent);
                requireActivity().finish();
            });
        });
    }

}
