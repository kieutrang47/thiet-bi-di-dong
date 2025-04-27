package com.example.smarthospital.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthospital.R;
import com.example.smarthospital.adapter.MedicalHistoryAdapter;
import com.example.smarthospital.database.AppDatabase;
import com.example.smarthospital.model.Appointment;
import com.example.smarthospital.model.Prescription;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionListActivity extends AppCompatActivity {
    private RecyclerView rvMedicalHistory;
    private AppDatabase db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);

        rvMedicalHistory = findViewById(R.id.rvMedicalHistory);
        db = AppDatabase.getInstance(this);
        userId = getIntent().getIntExtra("userId", -1);

        if (userId == -1) {
            finish();
            return;
        }

        rvMedicalHistory.setLayoutManager(new LinearLayoutManager(this));
        loadMedicalHistory();
    }

    private void loadMedicalHistory() {
        new Thread(() -> {
            List<Appointment> appointments = db.appointmentDao().getAppointmentsByPatient(userId);
            List<List<Prescription>> prescriptionsList = new ArrayList<>();
            for (Appointment appointment : appointments) {
                List<Prescription> prescriptions = db.prescriptionDao().getPrescriptionsByAppointment(appointment.getId());
                prescriptionsList.add(prescriptions);
            }
            runOnUiThread(() -> {
                MedicalHistoryAdapter adapter = new MedicalHistoryAdapter(appointments, prescriptionsList, db);
                rvMedicalHistory.setAdapter(adapter);
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMedicalHistory();
    }
}

//package com.example.smarthospital.ui;
//
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.smarthospital.R;
//import com.example.smarthospital.adapter.PrescriptionAdapter;
//import com.example.smarthospital.database.AppDatabase;
//import com.example.smarthospital.model.Appointment;
//import com.example.smarthospital.model.Prescription;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class PrescriptionListActivity extends AppCompatActivity {
//    private RecyclerView rvPrescriptions;
//    private AppDatabase db;
//    private int userId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_prescription_list);
//
//        rvPrescriptions = findViewById(R.id.rvPrescriptions);
//        db = AppDatabase.getInstance(this);
//        userId = getIntent().getIntExtra("userId", -1);
//
//        rvPrescriptions.setLayoutManager(new LinearLayoutManager(this));
//        new Thread(() -> {
//            List<Appointment> appointments = db.appointmentDao().getAppointmentsByPatient(userId);
//            List<Prescription> prescriptions = new ArrayList<>();
//            for (Appointment appointment : appointments) {
//                prescriptions.addAll(db.prescriptionDao().getPrescriptionsByAppointment(appointment.getId()));
//            }
//            runOnUiThread(() -> {
//                PrescriptionAdapter adapter = new PrescriptionAdapter(this, prescriptions);
//                rvPrescriptions.setAdapter(adapter);
//            });
//        }).start();
//    }
//}