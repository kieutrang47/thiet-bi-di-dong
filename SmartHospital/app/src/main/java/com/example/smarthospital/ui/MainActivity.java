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
//public class MainActivity extends AppCompatActivity {
//    private EditText etEmail, etPassword, etFullName;
//    private Button btnAction;
//    private TabLayout tabLayout;
//    private AppDatabase db;
//    private boolean isLoginMode = true;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        etEmail = findViewById(R.id.etEmail);
//        etPassword = findViewById(R.id.etPassword);
//        etFullName = findViewById(R.id.etFullName);
//        btnAction = findViewById(R.id.btnAction);
//        tabLayout = findViewById(R.id.tabLayout);
//        db = AppDatabase.getInstance(this);
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                isLoginMode = tab.getPosition() == 0;
//                etFullName.setVisibility(isLoginMode ? View.GONE : View.VISIBLE);
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
//            String email = etEmail.getText().toString();
//            String password = etPassword.getText().toString();
//            String fullName = etFullName.getText().toString();
//
//            if (isLoginMode) {
//                // Login
//                new Thread(() -> {
//                    User user = db.userDao().login(email, password);
//                    runOnUiThread(() -> {
//                        if (user != null) {
//                            Intent intent;
//                            if (user.getRole().equals("patient")) {
//                                intent = new Intent(MainActivity.this, PatientDashboardActivity.class);
//                            } else if (user.getRole().equals("doctor")) {
//                                intent = new Intent(MainActivity.this, DoctorDashboardActivity.class);
//                            } else {
//                                intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
//                            }
//                            intent.putExtra("userId", user.getId());
//                            startActivity(intent);
//                        } else {
//                            Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }).start();
//            } else {
//                // Register
//                new Thread(() -> {
//                    User user = new User(email, password, fullName, "patient", null);
//                    db.userDao().insert(user);
//                    runOnUiThread(() -> {
//                        Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
//                        tabLayout.getTabAt(0).select();
//                    });
//                }).start();
//            }
//        });
//    }
//}