package com.example.smarthospital.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthospital.R;
import com.example.smarthospital.adapter.AppointmentAdapter;
import com.example.smarthospital.database.AppDatabase;
import com.example.smarthospital.model.Appointment;
import com.example.smarthospital.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PatientDashboardActivity extends AppCompatActivity {
    private Spinner spinnerDoctor;
    private EditText etDate, etTime, etReason;
    private Button btnBookAppointment, btnViewPrescriptions;
    private FloatingActionButton fabMenu;
    private RecyclerView rvAppointments;
    private AppDatabase db;
    private int userId;
    private List<User> doctors;
    private AppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        spinnerDoctor = findViewById(R.id.spinnerDoctor);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etReason = findViewById(R.id.etReason);
        btnBookAppointment = findViewById(R.id.btnBookAppointment);
        btnViewPrescriptions = findViewById(R.id.btnViewPrescriptions);
        fabMenu = findViewById(R.id.fabMenu);
        rvAppointments = findViewById(R.id.rvAppointments);
        db = AppDatabase.getInstance(this);
        userId = getIntent().getIntExtra("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy userId", Toast.LENGTH_LONG).show();
            finish();
            return;
        } else {
            Log.d("PatientDashboard", "userId: " + userId);
        }

        new Thread(() -> {
            doctors = db.userDao().getDoctors();
            List<String> doctorNames = new ArrayList<>();
            for (User doctor : doctors) {
                doctorNames.add(doctor.getFullName() + " (" + doctor.getSpecialty() + ")");
            }
            runOnUiThread(() -> {
                if (doctorNames.isEmpty()) {
                    Toast.makeText(this, "Không có bác sĩ nào", Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, doctorNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDoctor.setAdapter(adapter);
            });
        }).start();

        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        loadAppointments();

        btnBookAppointment.setOnClickListener(v -> {
            String date = etDate.getText().toString().trim();
            String time = etTime.getText().toString().trim();
            String reason = etReason.getText().toString().trim();
            if (date.isEmpty() || time.isEmpty() || reason.isEmpty() || spinnerDoctor.getSelectedItem() == null) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            int doctorIndex = spinnerDoctor.getSelectedItemPosition();
            int doctorId = doctors.get(doctorIndex).getId();

            Appointment appointment = new Appointment(userId, doctorId, date, time, reason, "pending");
            new Thread(() -> {
                long appointmentId = db.appointmentDao().insert(appointment);
                runOnUiThread(() -> {
                    loadAppointments();
                    Toast.makeText(this, "Đã đặt lịch hẹn", Toast.LENGTH_SHORT).show();
                    etDate.setText("");
                    etTime.setText("");
                    etReason.setText("");
                });

                Intent notifyIntent = new Intent("com.example.smarthospital.NEW_APPOINTMENT");
                notifyIntent.putExtra("doctorId", doctorId);
                notifyIntent.putExtra("appointmentId", (int) appointmentId);
                Log.d("PatientDashboard", "Sending NEW_APPOINTMENT: doctorId=" + doctorId +
                        ", appointmentId=" + appointmentId);
                sendBroadcast(notifyIntent);
            }).start();
        });

        btnViewPrescriptions.setOnClickListener(v -> {
            Intent intent = new Intent(PatientDashboardActivity.this, PrescriptionListActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        fabMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, fabMenu);
            popup.getMenu().add(0, 1, 1, "Chỉnh sửa hồ sơ");
            popup.getMenu().add(0, 2, 2, "Đăng xuất");
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case 1:
                        Intent editIntent = new Intent(PatientDashboardActivity.this, EditProfileActivity.class);
                        editIntent.putExtra("userId", userId);
                        startActivity(editIntent);
                        return true;
                    case 2:
                        Intent logoutIntent = new Intent(PatientDashboardActivity.this, RoleSelectionActivity.class);
                        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(logoutIntent);
                        finish();
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
        });
    }

    private void loadAppointments() {
        new Thread(() -> {
            List<Appointment> appointments = db.appointmentDao().getAppointmentsByPatient(userId);
            runOnUiThread(() -> {
                adapter = new AppointmentAdapter(appointments, db);
                rvAppointments.setAdapter(adapter);
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAppointments();
    }
}


