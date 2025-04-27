package com.example.smarthospital.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthospital.R;
import com.example.smarthospital.model.Medicine;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {
    private List<Medicine> medicines;
    private OnDeleteListener deleteListener;
    private OnEditListener editListener;

    public interface OnDeleteListener {
        void onDelete(Medicine medicine);
    }

    public interface OnEditListener {
        void onEdit(Medicine medicine);
    }

    public MedicineAdapter(List<Medicine> medicines, OnDeleteListener deleteListener, OnEditListener editListener) {
        this.medicines = medicines;
        this.deleteListener = deleteListener;
        this.editListener = editListener;
    }

    public void updateMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicine medicine = medicines.get(position);
        holder.tvName.setText(medicine.getName());
        holder.tvQuantity.setText(String.valueOf(medicine.getQuantity()));
        holder.tvExpiryDate.setText(medicine.getExpiryDate());
        holder.tvPrice.setText(String.valueOf(medicine.getPrice()));
        holder.btnDelete.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn xóa thuốc này?")
                    .setPositiveButton("Có", (dialog, which) -> deleteListener.onDelete(medicine))
                    .setNegativeButton("Không", null)
                    .show();
        });
        holder.btnEdit.setOnClickListener(v -> editListener.onEdit(medicine));
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvExpiryDate, tvPrice;
        MaterialButton btnDelete, btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvExpiryDate = itemView.findViewById(R.id.tvExpiryDate);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}