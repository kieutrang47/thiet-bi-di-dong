package com.example.smarthospital.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthospital.R;
import com.example.smarthospital.model.User;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> users;
    private OnDeleteListener deleteListener;
    private OnEditListener editListener;

    public interface OnDeleteListener {
        void onDelete(int id);
    }

    public interface OnEditListener {
        void onEdit(User user);
    }

    public UserAdapter(List<User> users, OnDeleteListener deleteListener, OnEditListener editListener) {
        this.users = users;
        this.deleteListener = deleteListener;
        this.editListener = editListener;
    }

    public void updateUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.tvEmail.setText(user.getEmail());
        holder.tvFullName.setText(user.getFullName());
        holder.tvRole.setText(user.getRole());
        holder.tvSpecialty.setText(user.getSpecialty() != null ? user.getSpecialty() : "N/A");
        holder.btnDelete.setOnClickListener(v -> {
            // Dialog xác nhận xóa
            new android.app.AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn xóa người dùng này?")
                    .setPositiveButton("Có", (dialog, which) -> deleteListener.onDelete(user.getId()))
                    .setNegativeButton("Không", null)
                    .show();
        });
        holder.btnEdit.setOnClickListener(v -> editListener.onEdit(user));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmail, tvFullName, tvRole, tvSpecialty;
        MaterialButton btnDelete, btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvSpecialty = itemView.findViewById(R.id.tvSpecialty);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}