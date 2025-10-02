package com.example.myclinic.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.myclinic.R;
import com.example.myclinic.adapters.OnboardingAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private Button btnStart;

    private static final String PREF_NAME = "clinic_onboarding_prefs";
    private static final String KEY_FIRST_LAUNCH = "first_launch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean firstLaunch = prefs.getBoolean(KEY_FIRST_LAUNCH, true);

        if (!firstLaunch) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.onboardingViewPager);
        tabLayout = findViewById(R.id.tabLayout);
        btnStart = findViewById(R.id.btnStart);

        List<OnboardingItem> items = new ArrayList<>();
        items.add(new OnboardingItem(R.drawable.img1, "Welcome", "Manage your clinic appointments easily."));
        items.add(new OnboardingItem(R.drawable.img2, "Easy Booking", "Book your doctor appointments in a few taps."));
        items.add(new OnboardingItem(R.drawable.img3, "Medical Records", "View your past appointments and medical records."));

        OnboardingAdapter adapter = new OnboardingAdapter(items);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {}).attach();

        btnStart.setOnClickListener(v -> {
            prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply();
            startActivity(new Intent(OnboardingActivity.this, AuthActivity.class));
            finish();
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                btnStart.setVisibility(position == items.size() - 1 ? Button.VISIBLE : Button.GONE);
            }
        });
    }
}
