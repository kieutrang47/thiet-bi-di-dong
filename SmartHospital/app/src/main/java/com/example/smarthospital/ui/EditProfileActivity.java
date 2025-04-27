package com.example.smarthospital.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smarthospital.R;
import com.example.smarthospital.database.AppDatabase;
import com.example.smarthospital.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditProfileActivity extends AppCompatActivity {
    private TextInputEditText etEmail, etPassword, etFullName;
    private TextInputLayout emailLayout, passwordLayout, fullNameLayout;
    private MaterialButton btnSave;
    private AppDatabase db;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etFullName = findViewById(R.id.etFullName);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        fullNameLayout = findViewById(R.id.fullNameLayout);
        btnSave = findViewById(R.id.btnSave);
        db = AppDatabase.getInstance(this);

        // Lấy userId từ Intent
        int userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Tải thông tin người dùng
        new Thread(() -> {
            try {
                user = db.userDao().getUserById(userId);
                runOnUiThread(() -> {
                    if (user == null || !user.getRole().equals("patient")) {
                        Toast.makeText(this, "Không tìm thấy bệnh nhân", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    etEmail.setText(user.getEmail());
                    etPassword.setText(user.getPassword());
                    etFullName.setText(user.getFullName());
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Lỗi khi tải thông tin người dùng", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        }).start();

        btnSave.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String fullName = etFullName.getText().toString().trim();

            // Kiểm tra đầu vào
            boolean isValid = true;
            emailLayout.setError(null);
            passwordLayout.setError(null);
            fullNameLayout.setError(null);

            if (email.isEmpty()) {
                emailLayout.setError("Email là bắt buộc");
                isValid = false;
            }
            if (password.isEmpty()) {
                passwordLayout.setError("Mật khẩu là bắt buộc");
                isValid = false;
            }
            if (fullName.isEmpty()) {
                fullNameLayout.setError("Họ tên là bắt buộc");
                isValid = false;
            }

            if (!isValid) {
                return;
            }

            // Kiểm tra email trùng lặp
            new Thread(() -> {
                try {
                    User existingUser = db.userDao().findByEmail(email);
                    if (existingUser != null && existingUser.getId() != user.getId()) {
                        runOnUiThread(() -> emailLayout.setError("Email đã tồn tại"));
                    } else {
                        user.setEmail(email);
                        user.setPassword(password);
                        user.setFullName(fullName);
                        db.userDao().update(user);
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Đã cập nhật hồ sơ", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(this, "Lỗi khi cập nhật hồ sơ", Toast.LENGTH_SHORT).show());
                }
            }).start();
        });
    }
}

//package com.example.smarthospital.ui;
//
//import android.os.Bundle;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.smarthospital.R;
//import com.example.smarthospital.database.AppDatabase;
//import com.example.smarthospital.model.User;
//import com.google.android.material.button.MaterialButton;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.android.material.textfield.TextInputLayout;
//
//public class EditProfileActivity extends AppCompatActivity {
//    private TextInputEditText etEmail, etPassword, etFullName;
//    private TextInputLayout emailLayout, passwordLayout, fullNameLayout;
//    private MaterialButton btnSave;
//    private AppDatabase db;
//    private User user;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_profile);
//
//        etEmail = findViewById(R.id.etEmail);
//        etPassword = findViewById(R.id.etPassword);
//        etFullName = findViewById(R.id.etFullName);
//        emailLayout = findViewById(R.id.emailLayout);
//        passwordLayout = findViewById(R.id.passwordLayout);
//        fullNameLayout = findViewById(R.id.fullNameLayout);
//        btnSave = findViewById(R.id.btnSave);
//        db = AppDatabase.getInstance(this);
//
//        // Lấy userId từ Intent
//        int userId = getIntent().getIntExtra("userId", -1);
//        if (userId == -1) {
//            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        // Tải thông tin người dùng
//        new Thread(() -> {
//            user = db.userDao().getAllUsers().stream()
//                    .filter(u -> u.getId() == userId)
//                    .findFirst()
//                    .orElse(null);
//            runOnUiThread(() -> {
//                if (user == null || !user.getRole().equals("patient")) {
//                    Toast.makeText(this, "Không tìm thấy bệnh nhân", Toast.LENGTH_SHORT).show();
//                    finish();
//                    return;
//                }
//                etEmail.setText(user.getEmail());
//                etPassword.setText(user.getPassword());
//                etFullName.setText(user.getFullName());
//            });
//        }).start();
//
//        btnSave.setOnClickListener(v -> {
//            String email = etEmail.getText().toString().trim();
//            String password = etPassword.getText().toString().trim();
//            String fullName = etFullName.getText().toString().trim();
//
//            // Kiểm tra đầu vào
//            boolean isValid = true;
//            emailLayout.setError(null);
//            passwordLayout.setError(null);
//            fullNameLayout.setError(null);
//
//            if (email.isEmpty()) {
//                emailLayout.setError("Email là bắt buộc");
//                isValid = false;
//            }
//            if (password.isEmpty()) {
//                passwordLayout.setError("Mật khẩu là bắt buộc");
//                isValid = false;
//            }
//            if (fullName.isEmpty()) {
//                fullNameLayout.setError("Họ tên là bắt buộc");
//                isValid = false;
//            }
//
//            if (!isValid) {
//                return;
//            }
//
//            // Kiểm tra email trùng lặp
//            new Thread(() -> {
//                User existingUser = db.userDao().findByEmail(email);
//                if (existingUser != null && existingUser.getId() != user.getId()) {
//                    runOnUiThread(() -> emailLayout.setError("Email đã tồn tại"));
//                } else {
//                    user.setEmail(email);
//                    user.setPassword(password);
//                    user.setFullName(fullName);
//                    // Không thay đổi role hoặc specialty
//                    db.userDao().update(user);
//                    runOnUiThread(() -> {
//                        Toast.makeText(this, "Đã cập nhật hồ sơ", Toast.LENGTH_SHORT).show();
//                        finish();
//                    });
//                }
//            }).start();
//        });
//    }
//}