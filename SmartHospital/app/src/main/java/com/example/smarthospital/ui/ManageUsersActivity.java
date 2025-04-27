package com.example.smarthospital.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthospital.R;
import com.example.smarthospital.adapter.UserAdapter;
import com.example.smarthospital.database.AppDatabase;
import com.example.smarthospital.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {
    private TextInputEditText etEmail, etPassword, etFullName;
    private Spinner spinnerRole, spinnerSpecialty;
    private MaterialButton btnAddUser;
    private RecyclerView rvUsers;
    private AppDatabase db;
    private UserAdapter adapter;
    private User editingUser = null;
    private ArrayAdapter<String> specialtyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etFullName = findViewById(R.id.etFullName);
        spinnerSpecialty = findViewById(R.id.spinnerSpecialty);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnAddUser = findViewById(R.id.btnAddUser);
        rvUsers = findViewById(R.id.rvUsers);
        db = AppDatabase.getInstance(this);

        // Thiết lập spinner vai trò
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"doctor", "admin"});
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(roleAdapter);

        // Thiết lập spinner chuyên môn
        List<String> specialties = Arrays.asList(
                "Nội khoa", "Ngoại khoa", "Nhi khoa", "Sản khoa", "Tim mạch",
                "Thần kinh", "Da liễu", "Mắt", "Tai mũi họng", "Răng hàm mặt"
        );
        specialtyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, specialties);
        specialtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpecialty.setAdapter(specialtyAdapter);

        // Tải danh sách người dùng
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        new Thread(() -> {
            List<User> users = db.userDao().getAllUsers();
            runOnUiThread(() -> {
                adapter = new UserAdapter(users,
                        id -> new Thread(() -> {
                            db.userDao().delete(id);
                            runOnUiThread(() -> {
                                adapter.updateUsers(db.userDao().getAllUsers());
                                Toast.makeText(this, "Đã xóa người dùng", Toast.LENGTH_SHORT).show();
                            });
                        }).start(),
                        user -> runOnUiThread(() -> {
                            editingUser = user;
                            etEmail.setText(user.getEmail());
                            etPassword.setText(user.getPassword());
                            etFullName.setText(user.getFullName());
                            if (user.getSpecialty() != null) {
                                spinnerSpecialty.setSelection(specialtyAdapter.getPosition(user.getSpecialty()));
                            } else {
                                spinnerSpecialty.setSelection(0);
                            }
                            spinnerRole.setSelection(user.getRole().equals("doctor") ? 0 : 1);
                            spinnerSpecialty.setEnabled(user.getRole().equals("doctor"));
                            btnAddUser.setText("Cập nhật");
                        }));
                rvUsers.setAdapter(adapter);
            });
        }).start();

        // Xử lý thay đổi vai trò
        spinnerRole.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                spinnerSpecialty.setEnabled(spinnerRole.getSelectedItem().toString().equals("doctor"));
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                spinnerSpecialty.setEnabled(false);
            }
        });

        btnAddUser.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String fullName = etFullName.getText().toString().trim();
            String specialty = spinnerSpecialty.getSelectedItem().toString();
            String role = spinnerRole.getSelectedItem().toString();

            if (email.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ các trường bắt buộc", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                User existingUser = db.userDao().login(email, password);
                if (existingUser != null && (editingUser == null || existingUser.getId() != editingUser.getId())) {
                    runOnUiThread(() -> Toast.makeText(this, "Email đã tồn tại", Toast.LENGTH_SHORT).show());
                } else {
                    if (editingUser != null) {
                        editingUser.setEmail(email);
                        editingUser.setPassword(password);
                        editingUser.setFullName(fullName);
                        editingUser.setRole(role);
                        editingUser.setSpecialty(role.equals("doctor") ? specialty : null);
                        db.userDao().update(editingUser);
                    } else {
                        User user = new User(email, password, fullName, role, role.equals("doctor") ? specialty : null);
                        db.userDao().insert(user);
                    }

                    List<User> updatedUsers = db.userDao().getAllUsers();
                    runOnUiThread(() -> {
                        adapter.updateUsers(updatedUsers);
                        Toast.makeText(this,
                                editingUser != null ? "Đã cập nhật người dùng" : "Đã thêm người dùng",
                                Toast.LENGTH_SHORT).show();
                        resetForm();
                    });
                }
            }).start();
        });

    }

    private void resetForm() {
        etEmail.setText("");
        etPassword.setText("");
        etFullName.setText("");
        spinnerSpecialty.setSelection(0);
        spinnerRole.setSelection(0);
        spinnerSpecialty.setEnabled(true);
        btnAddUser.setText("Thêm Người Dùng");
        editingUser = null;
    }
}