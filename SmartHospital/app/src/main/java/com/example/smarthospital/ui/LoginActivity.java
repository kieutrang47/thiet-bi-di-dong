package com.example.smarthospital.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log; // Thêm import Log
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smarthospital.R;
import com.example.smarthospital.database.AppDatabase;
import com.example.smarthospital.model.User;
import com.example.smarthospital.ui.AdminDashboardActivity;
import com.example.smarthospital.ui.DoctorDashboardActivity;
import com.example.smarthospital.ui.PatientDashboardActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText etEmail, etPassword, etFullName, etSpecialty;
    private TextInputLayout emailLayout, passwordLayout, fullNameLayout;
    private Button btnAction;
    private TabLayout tabLayout;
    private AppDatabase db;
    private boolean isLoginMode = true;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo các view trước để giao diện phản hồi nhanh
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etFullName = findViewById(R.id.etFullName);
        etSpecialty = findViewById(R.id.etSpecialty);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        fullNameLayout = findViewById(R.id.fullNameLayout);
        btnAction = findViewById(R.id.btnAction);
        tabLayout = findViewById(R.id.tabLayout);
        role = getIntent().getStringExtra("role");

        // Kiểm tra role
        if (role == null) {
            Toast.makeText(this, "Role not specified", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Chỉ bệnh nhân được phép đăng ký
        if (!role.equals("patient")) {
            tabLayout.getTabAt(1).view.setEnabled(false);
        }

        // Xử lý sự kiện chọn tab
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isLoginMode = tab.getPosition() == 0;
                fullNameLayout.setVisibility(isLoginMode ? View.GONE : View.VISIBLE);
                etSpecialty.setVisibility(isLoginMode || !role.equals("doctor") ? View.GONE : View.VISIBLE);
                btnAction.setText(isLoginMode ? "Login" : "Register");
                // Xóa lỗi khi chuyển tab
                emailLayout.setError(null);
                passwordLayout.setError(null);
                fullNameLayout.setError(null);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Khởi tạo cơ sở dữ liệu trong background thread
        new InitializeDatabaseTask().execute();
    }

    private class InitializeDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            // Khởi tạo cơ sở dữ liệu
            db = AppDatabase.getInstance(LoginActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Sau khi cơ sở dữ liệu được khởi tạo, thiết lập sự kiện nút
            btnAction.setOnClickListener(v -> {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String fullName = etFullName.getText().toString().trim();
                String specialty = etSpecialty.getText().toString().trim();

                // Kiểm tra đầu vào
                boolean isValid = true;
                emailLayout.setError(null);
                passwordLayout.setError(null);
                fullNameLayout.setError(null);

                if (email.isEmpty()) {
                    emailLayout.setError("Email is required");
                    isValid = false;
                }
                if (password.isEmpty()) {
                    passwordLayout.setError("Password is required");
                    isValid = false;
                }
                if (!isLoginMode && fullName.isEmpty()) {
                    fullNameLayout.setError("Full name is required");
                    isValid = false;
                }

                if (!isValid) {
                    return;
                }

                if (isLoginMode) {
                    // Đăng nhập
                    new Thread(() -> {
                        User user = db.userDao().login(email, password);
                        runOnUiThread(() -> {
                            if (user != null && user.getRole().equals(role)) {
                                Intent intent;
                                if (role.equals("patient")) {
                                    intent = new Intent(LoginActivity.this, PatientDashboardActivity.class);
                                } else if (role.equals("doctor")) {
                                    intent = new Intent(LoginActivity.this, DoctorDashboardActivity.class);
                                } else {
                                    intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                                }
                                intent.putExtra("userId", user.getId());
                                Log.d("LoginActivity", "Truyền userId: " + user.getId()); // Thêm log để kiểm tra
                                startActivity(intent);
                                finish();
                            } else {
                                emailLayout.setError("Invalid credentials or role");
                            }
                        });
                    }).start();
                } else {
                    // Đăng ký (chỉ cho bệnh nhân)
                    if (!role.equals("patient")) {
                        Toast.makeText(LoginActivity.this, "Only patients can register", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new Thread(() -> {
                        try {
                            // Kiểm tra email trùng lặp
                            User existingUser = db.userDao().findByEmail(email);
                            if (existingUser != null) {
                                runOnUiThread(() -> emailLayout.setError("Email already exists"));
                                return;
                            }
                            User user = new User(email, password, fullName, "patient", null);
                            db.userDao().insert(user);
                            runOnUiThread(() -> {
                                Toast.makeText(LoginActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                tabLayout.getTabAt(0).select();
                            });
                        } catch (Exception e) {
                            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        }
                    }).start();
                }
            });
        }
    }
}
//package com.example.smarthospital.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.smarthospital.R;
//import com.example.smarthospital.database.AppDatabase;
//import com.example.smarthospital.model.User;
//import com.google.android.material.tabs.TabLayout;
//
//public class LoginActivity extends AppCompatActivity {
//    private EditText etEmail, etPassword, etFullName, etSpecialty;
//    private Button btnAction;
//    private TabLayout tabLayout;
//    private AppDatabase db;
//    private boolean isLoginMode = true;
//    private String role;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        etEmail = findViewById(R.id.etEmail);
//        etPassword = findViewById(R.id.etPassword);
//        etFullName = findViewById(R.id.etFullName);
//        etSpecialty = findViewById(R.id.etSpecialty);
//        btnAction = findViewById(R.id.btnAction);
//        tabLayout = findViewById(R.id.tabLayout);
//        db = AppDatabase.getInstance(this);
//        role = getIntent().getStringExtra("role");
//
//        // Chỉ bệnh nhân được phép đăng ký
//        if (!role.equals("patient")) {
//            tabLayout.getTabAt(1).view.setEnabled(false);
//        }
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                isLoginMode = tab.getPosition() == 0;
//                etFullName.setVisibility(isLoginMode ? View.GONE : View.VISIBLE);
//                etSpecialty.setVisibility(isLoginMode || !role.equals("doctor") ? View.GONE : View.VISIBLE);
//                btnAction.setText(isLoginMode ? "Login" : "Register");
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {}
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {}
//        });
//
//        btnAction.setOnClickListener(v -> {
//            String email = etEmail.getText().toString().trim();
//            String password = etPassword.getText().toString().trim();
//            String fullName = etFullName.getText().toString().trim();
//            String specialty = etSpecialty.getText().toString().trim();
//
//            if (email.isEmpty() || password.isEmpty() || (!isLoginMode && fullName.isEmpty())) {
//                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if (isLoginMode) {
//                // Login
//                new Thread(() -> {
//                    User user = db.userDao().login(email, password);
//                    runOnUiThread(() -> {
//                        if (user != null && user.getRole().equals(role)) {
//                            Intent intent;
//                            if (role.equals("patient")) {
//                                intent = new Intent(LoginActivity.this, PatientDashboardActivity.class);
//                            } else if (role.equals("doctor")) {
//                                intent = new Intent(LoginActivity.this, DoctorDashboardActivity.class);
//                            } else {
//                                intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
//                            }
//                            intent.putExtra("userId", user.getId());
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            Toast.makeText(LoginActivity.this, "Invalid credentials or role", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }).start();
//            } else {
//                // Register (chỉ cho bệnh nhân)
//                if (!role.equals("patient")) {
//                    Toast.makeText(this, "Only patients can register", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                new Thread(() -> {
//                    try {
//                        // Kiểm tra email trùng lặp
//                        User existingUser = db.userDao().findByEmail(email); // Cần thêm phương thức findByEmail trong UserDao
//                        if (existingUser != null) {
//                            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Email already exists", Toast.LENGTH_SHORT).show());
//                            return;
//                        }
//                        User user = new User(email, password, fullName, "patient", null);
//                        db.userDao().insert(user);
//                        runOnUiThread(() -> {
//                            Toast.makeText(LoginActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
//                            tabLayout.getTabAt(0).select();
//                        });
//                    } catch (Exception e) {
//                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//                    }
//                }).start();
//            }
////            } else {
////                // Register (chỉ cho bệnh nhân)
////                if (!role.equals("patient")) {
////                    Toast.makeText(this, "Only patients can register", Toast.LENGTH_SHORT).show();
////                    return;
////                }
////                new Thread(() -> {
////                    User user = new User(email, password, fullName, "patient", null);
////                    db.userDao().insert(user);
////                    runOnUiThread(() -> {
////                        Toast.makeText(LoginActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
////                        tabLayout.getTabAt(0).select();
////                    });
////                }).start();
////            }
//        });
//    }
//}