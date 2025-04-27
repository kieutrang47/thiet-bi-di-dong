package com.example.smarthospital.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthospital.R;
import com.example.smarthospital.database.AppDatabase;
import com.example.smarthospital.model.Appointment;
import com.example.smarthospital.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private List<Appointment> appointments;
    private OnActionListener actionListener;
    private OnViewRecordListener viewRecordListener;
    private AppDatabase db;
    private Map<Integer, User> userMap;

    public interface OnActionListener {
        void onAction(Appointment appointment, String action);
    }

    public interface OnViewRecordListener {
        void onViewRecord(Appointment appointment);
    }

    public AppointmentAdapter(List<Appointment> appointments, AppDatabase db) {
        this.appointments = appointments;
        this.db = db;
        this.userMap = new HashMap<>();
        loadUsers();
    }

    private void loadUsers() {
        new Thread(() -> {
            List<User> users = db.userDao().getAllUsers();
            for (User user : users) {
                userMap.put(user.getId(), user);
            }
        }).start();
    }

    public void setOnActionListener(OnActionListener listener) {
        this.actionListener = listener;
    }

    public void setOnViewRecordListener(OnViewRecordListener listener) {
        this.viewRecordListener = listener;
    }

    public void updateAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        User patient = userMap.get(appointment.getPatientId());
        User doctor = userMap.get(appointment.getDoctorId());
        String patientName = patient != null ? patient.getFullName() : "Không xác định";
        String doctorName = doctor != null ? doctor.getFullName() : "Không xác định";

        holder.tvDoctor.setText("Bác sĩ: " + doctorName);
        holder.tvDate.setText("Ngày: " + appointment.getDate());
        holder.tvTime.setText("Giờ: " + appointment.getTime());
        holder.tvReason.setText("Lý do: " + appointment.getReason());
        holder.tvStatus.setText("Trạng thái: " + appointment.getStatus());

        holder.btnAccept.setVisibility(appointment.getStatus().equals("pending") ? View.VISIBLE : View.GONE);
        holder.btnReject.setVisibility(appointment.getStatus().equals("pending") ? View.VISIBLE : View.GONE);
        holder.btnViewRecord.setVisibility(appointment.getStatus().equals("accepted") || appointment.getStatus().equals("completed") ? View.VISIBLE : View.GONE);

        holder.btnAccept.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onAction(appointment, "accepted");
            }
        });
        holder.btnReject.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onAction(appointment, "rejected");
            }
        });
        holder.btnViewRecord.setOnClickListener(v -> {
            if (viewRecordListener != null) {
                viewRecordListener.onViewRecord(appointment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDoctor, tvDate, tvTime, tvReason, tvStatus;
        Button btnAccept, btnReject, btnViewRecord;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDoctor = itemView.findViewById(R.id.tvDoctor);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvReason = itemView.findViewById(R.id.tvReason);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
            btnViewRecord = itemView.findViewById(R.id.btnViewRecord);
        }
    }
}


