package com.example.smarthospital.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smarthospital.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdminDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        findViewById(R.id.btnManageUsers).setOnClickListener(v -> {
            startActivity(new Intent(this, ManageUsersActivity.class));
        });

        findViewById(R.id.btnManageMedicines).setOnClickListener(v -> {
            startActivity(new Intent(this, ManageMedicinesActivity.class));
        });

        findViewById(R.id.btnManageRooms).setOnClickListener(v -> {
            startActivity(new Intent(this, ManageRoomsActivity.class));
        });

        // Xử lý FAB
        FloatingActionButton fabMenu = findViewById(R.id.fabMenu);
        fabMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, fabMenu);
            popup.getMenu().add("Đăng xuất");
            popup.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Đăng xuất")) {
                    // Quay về màn hình đăng nhập/chọn vai trò
                    Intent intent = new Intent(this, LoginActivity.class); // Thay bằng activity đăng nhập/chọn vai trò
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                return true;
            });
            popup.show();
        });
    }
}