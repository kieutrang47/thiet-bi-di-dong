package com.example.smarthospital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthospital.R;
import com.example.smarthospital.database.AppDatabase;
import com.example.smarthospital.model.Prescription;

import java.util.List;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.ViewHolder> {
    private final List<Prescription> prescriptions;
    private final AppDatabase db;

    public PrescriptionAdapter(Context context, List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
        this.db = AppDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prescription, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Prescription prescription = prescriptions.get(position);
        String medicineName = db.medicineDao().getAllMedicines().stream()
                .filter(m -> m.getId() == prescription.getMedicineId())
                .findFirst()
                .map(m -> m.getName())
                .orElse("Unknown");

        holder.tvMedicine.setText(medicineName);
        holder.tvDosage.setText(prescription.getDosage());
        holder.tvInstructions.setText(prescription.getInstructions());
        holder.tvDateIssued.setText(prescription.getDateIssued());
    }

    @Override
    public int getItemCount() {
        return prescriptions.size();
    }

    public void updatePrescriptions(List<Prescription> newPrescriptions) {
        prescriptions.clear();
        prescriptions.addAll(newPrescriptions);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedicine, tvDosage, tvInstructions, tvDateIssued;

        ViewHolder(View itemView) {
            super(itemView);
            tvMedicine = itemView.findViewById(R.id.tvMedicine);
            tvDosage = itemView.findViewById(R.id.tvDosage);
            tvInstructions = itemView.findViewById(R.id.tvInstructions);
            tvDateIssued = itemView.findViewById(R.id.tvDateIssued);
        }
    }
}