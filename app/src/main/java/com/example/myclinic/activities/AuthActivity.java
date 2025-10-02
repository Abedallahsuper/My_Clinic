package com.example.myclinic.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myclinic.R;
import com.example.myclinic.fragments.LoginFragment;
import com.example.myclinic.fragments.RegisterFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AuthActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private static final String PREF_NAME = "clinic_prefs";
    private static final String KEY_USER_ID = "user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        tabLayout = findViewById(R.id.tabLayoutAuth);
        viewPager = findViewById(R.id.viewPagerAuth);

        viewPager.setAdapter(new AuthPagerAdapter());

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? "Login" : "Register")
        ).attach();

        boolean openLogin = getIntent().getBooleanExtra("openLogin", false);
        if (openLogin) {
            viewPager.setCurrentItem(0, true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean openLogin = getIntent().getBooleanExtra("openLogin", false);
        if (openLogin) return;

        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int userId = prefs.getInt(KEY_USER_ID, -1);
        if (userId != -1) {

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private class AuthPagerAdapter extends FragmentStateAdapter {
        public AuthPagerAdapter() { super(AuthActivity.this); }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return position == 0 ? new LoginFragment() : new RegisterFragment();
        }

        @Override
        public int getItemCount() { return 2; }
    }
}
