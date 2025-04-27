package com.example.smarthospital.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smarthospital.R;
import com.example.smarthospital.database.AppDatabase;
import com.example.smarthospital.model.Medicine;
import com.example.smarthospital.model.Prescription;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PrescriptionActivity extends AppCompatActivity {
    private Spinner spinnerMedicine;
    private EditText etDosage, etInstructions;
    private Button btnSavePrescription;
    private AppDatabase db;
    private int appointmentId;
    private List<Medicine> medicines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        spinnerMedicine = findViewById(R.id.spinnerMedicine);
        etDosage = findViewById(R.id.etDosage);
        etInstructions = findViewById(R.id.etInstructions);
        btnSavePrescription = findViewById(R.id.btnSavePrescription);
        db = AppDatabase.getInstance(this);
        appointmentId = getIntent().getIntExtra("appointmentId", -1);

        // Load medicines
        new Thread(() -> {
            medicines = db.medicineDao().getAllMedicines();
            List<String> medicineNames = new ArrayList<>();
            for (Medicine medicine : medicines) {
                medicineNames.add(medicine.getName());
            }
            runOnUiThread(() -> {
                if (medicineNames.isEmpty()) {
                    Toast.makeText(this, "No medicines available", Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, medicineNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMedicine.setAdapter(adapter);
            });
        }).start();

        btnSavePrescription.setOnClickListener(v -> {
            int selectedPosition = spinnerMedicine.getSelectedItemPosition();
            if (selectedPosition < 0 || selectedPosition >= medicines.size()) {
                Toast.makeText(this, "Please select a medicine", Toast.LENGTH_SHORT).show();
                return;
            }
            int medicineId = medicines.get(selectedPosition).getId();
            String dosage = etDosage.getText().toString().trim();
            String instructions = etInstructions.getText().toString().trim();
            if (dosage.isEmpty() || instructions.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            String dateIssued = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            Prescription prescription = new Prescription(appointmentId, medicineId, dosage, instructions, dateIssued);
            new Thread(() -> {
                db.prescriptionDao().insert(prescription);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Prescription saved", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }
}