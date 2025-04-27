package com.example.smarthospital.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.smarthospital.database.AppDatabase;
import com.example.smarthospital.model.Appointment;
import com.example.smarthospital.model.User;
import com.example.smarthospital.util.NotificationHelper;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String ACTION_DISMISS_ALARM = "com.example.smarthospital.DISMISS_ALARM";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        AppDatabase db = AppDatabase.getInstance(context);

        Log.d("AlarmReceiver", "Received intent with action: " + action);

        if (ACTION_DISMISS_ALARM.equals(action)) {
            // Dừng âm thanh và hủy thông báo
            int notificationId = intent.getIntExtra("notificationId", -1);
            Log.d("AlarmReceiver", "Dismiss alarm for notificationId: " + notificationId);
            NotificationHelper.stopRingtone();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(notificationId);
        } else if ("com.example.smarthospital.PATIENT_NOTIFICATION".equals(action)) {
            // Xử lý thông báo cho bệnh nhân
            int patientId = intent.getIntExtra("patientId", -1);
            int appointmentId = intent.getIntExtra("appointmentId", -1);
            int doctorId = intent.getIntExtra("doctorId", -1);

            Log.d("AlarmReceiver", "PATIENT_NOTIFICATION: patientId=" + patientId +
                    ", appointmentId=" + appointmentId + ", doctorId=" + doctorId);

            if (patientId != -1 && appointmentId != -1 && doctorId != -1) {
                new Thread(() -> {
                    Appointment appointment = db.appointmentDao().getAppointmentById(appointmentId);
                    User doctor = db.userDao().getUserById(doctorId);
                    Log.d("AlarmReceiver", "Queried: appointment=" + (appointment != null ? appointment.toString() : "null") +
                            ", doctor=" + (doctor != null ? doctor.getFullName() : "null"));
                    if (appointment != null && doctor != null) {
                        Log.d("AlarmReceiver", "Sending patient notification: appointmentId=" + appointmentId +
                                ", doctorName=" + doctor.getFullName());
                        NotificationHelper notificationHelper = new NotificationHelper(context);
                        notificationHelper.sendHighPriorityNotification(
                                "Lịch hẹn được chấp nhận",
                                "Bác sĩ " + doctor.getFullName() + " đã chấp nhận lịch hẹn của bạn vào " +
                                        appointment.getTime() + " ngày " + appointment.getDate() +
                                        ". Vui lòng đến đúng giờ.",
                                appointmentId + 20000,
                                patientId,
                                false
                        );
                    } else {
                        Log.e("AlarmReceiver", "Failed to send patient notification: appointment=" + appointment +
                                ", doctor=" + doctor);
                    }
                }).start();
            } else {
                Log.w("AlarmReceiver", "Invalid patient notification data: patientId=" + patientId +
                        ", appointmentId=" + appointmentId + ", doctorId=" + doctorId);
            }
        } else {
            // Xử lý thông báo nhắc nhở cho bác sĩ (bao gồm thông báo lặp lại)
            int appointmentId = intent.getIntExtra("appointmentId", -1);
            int doctorId = intent.getIntExtra("doctorId", -1);
            String title = intent.getStringExtra("title");
            String message = intent.getStringExtra("message");

            Log.d("AlarmReceiver", "Doctor reminder: appointmentId=" + appointmentId + ", doctorId=" + doctorId);

            if (appointmentId != -1 && doctorId != -1) {
                new Thread(() -> {
                    Appointment appointment = db.appointmentDao().getAppointmentById(appointmentId);
                    if (appointment != null && appointment.getDoctorId() == doctorId) {
                        String patientName = db.userDao().getUserById(appointment.getPatientId()).getFullName();
                        Log.d("AlarmReceiver", "Sending doctor reminder: appointmentId=" + appointmentId +
                                ", patientName=" + patientName);
                        NotificationHelper notificationHelper = new NotificationHelper(context);
                        notificationHelper.sendHighPriorityNotification(
                                title != null ? title : "Nhắc nhở chuẩn bị khám",
                                message != null ? message : "Chuẩn bị cho bệnh nhân " + patientName + " vào " +
                                        appointment.getTime() + " sau 30 phút",
                                appointmentId + 10000,
                                doctorId,
                                true
                        );
                    } else {
                        Log.e("AlarmReceiver", "Failed to send doctor reminder: appointment=" + appointment);
                    }
                }).start();
            } else {
                Log.w("AlarmReceiver", "Invalid doctor reminder data: appointmentId=" + appointmentId +
                        ", doctorId=" + doctorId);
            }
        }
    }
}
