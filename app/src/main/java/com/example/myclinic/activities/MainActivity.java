package com.example.myclinic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myclinic.MapsActivity;
import com.example.myclinic.R;
import com.example.myclinic.fragments.AppointmentsFragment;
import com.example.myclinic.fragments.BrowseDoctorsFragment;
import com.example.myclinic.fragments.MedicalRecordsFragment;
import com.example.myclinic.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        FloatingActionButton fabOpenMap = findViewById(R.id.fab_open_map);

        // فتح الخريطة عند الضغط على FAB
        fabOpenMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        });

        loadFragment(new BrowseDoctorsFragment());

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int id = item.getItemId();

                if (id == R.id.nav_doctors) {
                    selectedFragment = new BrowseDoctorsFragment();
                } else if (id == R.id.nav_appointments) {
                    selectedFragment = new AppointmentsFragment();
                } else if (id == R.id.nav_records) {
                    selectedFragment = new MedicalRecordsFragment();
                } else if (id == R.id.nav_profile) {
                    selectedFragment = new ProfileFragment();
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}

