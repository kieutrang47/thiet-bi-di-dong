package com.example.nguyenthikieutrang_bai4_module3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword;
    private CheckBox checkBoxSaveInfo;
    private Button buttonLogin, buttonExit;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_SAVE_INFO = "saveInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo các view với ID từ layout
        editTextUsername = findViewById(R.id.editcateid);
        editTextPassword = findViewById(R.id.editTextTextPassword2);
        checkBoxSaveInfo = findViewById(R.id.checkBox);
        buttonLogin = findViewById(R.id.btnDangNhap);
        buttonExit = findViewById(R.id.btnThoat);

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Load thông tin đã lưu nếu có
        loadSavedInfo();

        // Xử lý sự kiện click nút Đăng nhập
        buttonLogin.setOnClickListener(v -> {
            // Kiểm tra nhập liệu
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ username và password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Nếu nhập liệu hợp lệ, tiến hành lưu/xóa thông tin
            if (checkBoxSaveInfo.isChecked()) {
                saveLoginInfo();
            } else {
                clearSavedInfo();
            }
            // Mở WelcomeActivity và truyền username
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        });

        // Xử lý sự kiện click nút Thoát
        buttonExit.setOnClickListener(v -> {
            // Đóng ứng dụng mà không lưu thông tin
            finishAffinity();
        });

        // Listener cho checkbox
        checkBoxSaveInfo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                clearSavedInfo();
            }
        });
    }

    private void loadSavedInfo() {
        boolean saveInfo = sharedPreferences.getBoolean(KEY_SAVE_INFO, false);
        if (saveInfo) {
            String username = sharedPreferences.getString(KEY_USERNAME, "");
            String password = sharedPreferences.getString(KEY_PASSWORD, "");
            editTextUsername.setText(username);
            editTextPassword.setText(password);
            checkBoxSaveInfo.setChecked(true);
        }
    }

    private void saveLoginInfo() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, editTextUsername.getText().toString().trim());
        editor.putString(KEY_PASSWORD, editTextPassword.getText().toString().trim());
        editor.putBoolean(KEY_SAVE_INFO, true);
        editor.apply();
    }

    private void clearSavedInfo() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}