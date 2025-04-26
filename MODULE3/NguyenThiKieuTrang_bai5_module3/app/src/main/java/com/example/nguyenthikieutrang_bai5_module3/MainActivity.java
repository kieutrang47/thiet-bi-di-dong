package com.example.nguyenthikieutrang_bai5_module3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private LinearLayout mainLayout;
    private Button buttonStartSetting;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo các view
        mainLayout = findViewById(R.id.main_layout);
        buttonStartSetting = findViewById(R.id.buttonStartSetting);

        // Khởi tạo SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        // Load màu nền ban đầu
        updateBackgroundColor();

        // Xử lý sự kiện click nút Start My Setting
        buttonStartSetting.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void updateBackgroundColor() {
        boolean isRed = sharedPreferences.getBoolean("background_color", false);
        if (isRed) {
            mainLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        } else {
            mainLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("background_color")) {
            updateBackgroundColor();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}