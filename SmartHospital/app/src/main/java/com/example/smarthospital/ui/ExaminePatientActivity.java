package com.example.smarthospital.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smarthospital.R;
import com.example.smarthospital.database.AppDatabase;
import com.example.smarthospital.model.Appointment;
import com.example.smarthospital.model.Medicine;
import com.example.smarthospital.model.Prescription;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExaminePatientActivity extends AppCompatActivity {
    private TextInputEditText symptomsInput, diagnosisInput, dosageInput, instructionsInput;
    private TextInputLayout symptomsLayout, diagnosisLayout, dosageLayout, instructionsLayout;
    private Spinner medicineSpinner;
    private MaterialButton saveButton;
    private AppDatabase db;
    private int appointmentId;
    private List<Medicine> medicines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record);

        symptomsInput = findViewById(R.id.symptomsInput);
        diagnosisInput = findViewById(R.id.diagnosisInput);
        dosageInput = findViewById(R.id.dosageInput);
        instructionsInput = findViewById(R.id.instructionsInput);
        symptomsLayout = findViewById(R.id.symptomsLayout);
        diagnosisLayout = findViewById(R.id.diagnosisLayout);
        dosageLayout = findViewById(R.id.dosageLayout);
        instructionsLayout = findViewById(R.id.instructionsLayout);
        medicineSpinner = findViewById(R.id.medicineInput);
        saveButton = findViewById(R.id.saveButton);
        db = AppDatabase.getInstance(this);
        appointmentId = getIntent().getIntExtra("appointmentId", -1);

        if (appointmentId == -1) {
            Toast.makeText(this, "Cuộc hẹn không hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load medicines
        new Thread(() -> {
            medicines = db.medicineDao().getAllMedicines();
            List<String> medicineNames = new ArrayList<>();
            for (Medicine medicine : medicines) {
                medicineNames.add(medicine.getName());
            }
            runOnUiThread(() -> {
                if (medicineNames.isEmpty()) {
                    Toast.makeText(this, "Không có thuốc nào trong kho", Toast.LENGTH_SHORT).show();
                    medicineSpinner.setEnabled(false);
                    return;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, medicineNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                medicineSpinner.setAdapter(adapter);
            });
        }).start();

        // Load existing appointment data
        new Thread(() -> {
            Appointment appointment = db.appointmentDao().getAppointmentById(appointmentId);
            runOnUiThread(() -> {
                if (appointment == null) {
                    Toast.makeText(this, "Không tìm thấy lịch hẹn", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                if (appointment.getSymptoms() != null) {
                    symptomsInput.setText(appointment.getSymptoms());
                }
                if (appointment.getDiagnosis() != null) {
                    diagnosisInput.setText(appointment.getDiagnosis());
                }
            });
        }).start();

        saveButton.setOnClickListener(v -> {
            String symptoms = symptomsInput.getText().toString().trim();
            String diagnosis = diagnosisInput.getText().toString().trim();
            String dosage = dosageInput.getText().toString().trim();
            String instructions = instructionsInput.getText().toString().trim();
            int selectedMedicineIndex = medicineSpinner.getSelectedItemPosition();

            // Validate input
            boolean isValid = true;
            symptomsLayout.setError(null);
            diagnosisLayout.setError(null);
            dosageLayout.setError(null);
            instructionsLayout.setError(null);

            if (symptoms.isEmpty()) {
                symptomsLayout.setError("Triệu chứng là bắt buộc");
                isValid = false;
            }
            if (diagnosis.isEmpty()) {
                diagnosisLayout.setError("Chẩn đoán là bắt buộc");
                isValid = false;
            }
            if (dosage.isEmpty()) {
                dosageLayout.setError("Liều lượng là bắt buộc");
                isValid = false;
            }
            if (instructions.isEmpty()) {
                instructionsLayout.setError("Hướng dẫn sử dụng là bắt buộc");
                isValid = false;
            }
            if (selectedMedicineIndex == -1) {
                Toast.makeText(this, "Vui lòng chọn thuốc", Toast.LENGTH_SHORT).show();
                isValid = false;
            }

            if (!isValid) {
                return;
            }

            int medicineId = medicines.get(selectedMedicineIndex).getId();
            String dateIssued = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            new Thread(() -> {
                Appointment appointment = db.appointmentDao().getAppointmentById(appointmentId);
                appointment.setSymptoms(symptoms);
                appointment.setDiagnosis(diagnosis);
                appointment.setStatus("completed");
                db.appointmentDao().update(appointment);

                Prescription prescription = new Prescription(
                        appointmentId,
                        medicineId,
                        dosage,
                        instructions,
                        dateIssued
                );
                db.prescriptionDao().insert(prescription);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Đã lưu kết quả khám", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }
}