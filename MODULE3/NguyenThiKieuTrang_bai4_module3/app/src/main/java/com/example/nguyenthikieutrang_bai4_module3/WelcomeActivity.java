package com.example.nguyenthikieutrang_bai4_module3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    private TextView textViewWelcome;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Khởi tạo các view
        textViewWelcome = findViewById(R.id.textViewWelcome);
        buttonLogout = findViewById(R.id.buttonLogout);

        // Lấy username từ Intent
        String username = getIntent().getStringExtra("USERNAME");
        if (username == null || username.isEmpty()) {
            username = "User";
        }
        textViewWelcome.setText("Welcome " + username);

        // Xử lý sự kiện click nút OK để đóng ứng dụng
        buttonLogout.setOnClickListener(v -> {
            finishAffinity();
        });
    }
}