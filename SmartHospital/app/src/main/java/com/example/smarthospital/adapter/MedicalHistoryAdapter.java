package com.example.smarthospital.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthospital.R;
import com.example.smarthospital.database.AppDatabase;
import com.example.smarthospital.model.Appointment;
import com.example.smarthospital.model.Medicine;
import com.example.smarthospital.model.Prescription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MedicalHistoryAdapter extends RecyclerView.Adapter<MedicalHistoryAdapter.ViewHolder> {
    private List<Appointment> appointments;
    private List<List<Prescription>> prescriptionsList;
    private AppDatabase db;
    private Map<Integer, Medicine> medicineMap;

    public MedicalHistoryAdapter(List<Appointment> appointments, List<List<Prescription>> prescriptionsList, AppDatabase db) {
        this.appointments = appointments;
        this.prescriptionsList = prescriptionsList;
        this.db = db;
        this.medicineMap = new HashMap<>();
        loadMedicines();
    }

    private void loadMedicines() {
        new Thread(() -> {
            List<Medicine> medicines = db.medicineDao().getAllMedicines();
            for (Medicine medicine : medicines) {
                medicineMap.put(medicine.getId(), medicine);
            }
        }).start();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medical_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        List<Prescription> prescriptions = prescriptionsList.get(position);

        holder.tvDate.setText("Ngày: " + appointment.getDate());
        holder.tvSymptoms.setText("Triệu chứng: " + (appointment.getSymptoms() != null ? appointment.getSymptoms() : "Chưa ghi nhận"));
        holder.tvDiagnosis.setText("Chẩn đoán: " + (appointment.getDiagnosis() != null ? appointment.getDiagnosis() : "Chưa ghi nhận"));

        StringBuilder prescriptionsText = new StringBuilder("Đơn thuốc: ");
        if (prescriptions.isEmpty()) {
            prescriptionsText.append("Chưa có đơn thuốc");
        } else {
            String prescriptionDetails = prescriptions.stream().map(p -> {
                Medicine medicine = medicineMap.get(p.getMedicineId());
                String medicineName = medicine != null ? medicine.getName() : "Không xác định";
                return medicineName + " - " + p.getDosage() + " - " + p.getInstructions();
            }).collect(Collectors.joining("\n"));
            prescriptionsText.append("\n").append(prescriptionDetails);
        }
        holder.tvPrescriptions.setText(prescriptionsText.toString());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvSymptoms, tvDiagnosis, tvPrescriptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvSymptoms = itemView.findViewById(R.id.tvSymptoms);
            tvDiagnosis = itemView.findViewById(R.id.tvDiagnosis);
            tvPrescriptions = itemView.findViewById(R.id.tvPrescriptions);
        }
    }
}