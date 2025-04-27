package com.example.smarthospital.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthospital.R;
import com.example.smarthospital.adapter.AppointmentAdapter;
import com.example.smarthospital.database.AppDatabase;
import com.example.smarthospital.model.Appointment;
import com.example.smarthospital.service.AppointmentNotificationService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DoctorDashboardActivity extends AppCompatActivity {
    private RecyclerView rvAppointments;
    private AppDatabase db;
    private int userId;
    private AppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        rvAppointments = findViewById(R.id.rvAppointments);
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));

        db = AppDatabase.getInstance(this);
        userId = getIntent().getIntExtra("userId", -1);

        Intent serviceIntent = new Intent(this, AppointmentNotificationService.class);
        serviceIntent.putExtra("doctorId", userId);
        startService(serviceIntent);

        loadAppointments();

        FloatingActionButton fabMenu = findViewById(R.id.fabMenu);
        fabMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, fabMenu);
            popup.getMenu().add("Đăng xuất");
            popup.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Đăng xuất")) {
                    Intent intent = new Intent(DoctorDashboardActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                return true;
            });
            popup.show();
        });
    }

    private void loadAppointments() {
        new Thread(() -> {
            List<Appointment> appointments = db.appointmentDao().getAppointmentsByDoctor(userId);
            runOnUiThread(() -> {
                adapter = new AppointmentAdapter(appointments, db);
                adapter.setOnActionListener((appointment, action) -> {
                    appointment.setStatus(action);
                    new Thread(() -> {
                        db.appointmentDao().update(appointment);
                        if (action.equals("accepted")) {
                            // Gửi thông báo đến bệnh nhân
                            Intent notifyPatientIntent = new Intent("com.example.smarthospital.PATIENT_NOTIFICATION");
                            notifyPatientIntent.putExtra("patientId", appointment.getPatientId());
                            notifyPatientIntent.putExtra("appointmentId", appointment.getId());
                            notifyPatientIntent.putExtra("doctorId", userId);
                            Log.d("DoctorDashboard", "Sending PATIENT_NOTIFICATION: patientId=" + appointment.getPatientId() +
                                    ", appointmentId=" + appointment.getId() + ", doctorId=" + userId);
                            sendBroadcast(notifyPatientIntent);
                            runOnUiThread(() -> {
                                Intent intent = new Intent(DoctorDashboardActivity.this, ExaminePatientActivity.class);
                                intent.putExtra("appointmentId", appointment.getId());
                                intent.putExtra("doctorId", userId);
                                startActivity(intent);
                            });
                        } else {
                            runOnUiThread(this::loadAppointments);
                        }
                    }).start();
                });
                adapter.setOnViewRecordListener(appointment -> {
                    Intent intent = new Intent(DoctorDashboardActivity.this, ExaminePatientActivity.class);
                    intent.putExtra("appointmentId", appointment.getId());
                    intent.putExtra("doctorId", userId);
                    startActivity(intent);
                });
                rvAppointments.setAdapter(adapter);
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAppointments();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}
