//package com.example.smarthospital.service;
//
//import android.app.Notification;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//import androidx.core.app.NotificationCompat;
//
//import com.example.smarthospital.database.AppDatabase;
//import com.example.smarthospital.model.Appointment;
//import com.example.smarthospital.model.User;
//import com.example.smarthospital.ui.PatientDashboardActivity;
//import com.example.smarthospital.util.NotificationHelper;
//
//public class PatientNotificationService extends Service {
//    private static final String TAG = "PatientNotificationService";
//    private static final int SERVICE_NOTIFICATION_ID = 1001;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        // Tạo thông báo foreground để giữ service chạy
//        Notification notification = new NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID)
//                .setContentTitle("Smart Hospital")
//                .setContentText("Đang xử lý thông báo lịch hẹn")
//                .setSmallIcon(android.R.drawable.ic_dialog_info)
//                .build();
//        startForeground(SERVICE_NOTIFICATION_ID, notification);
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent != null && "com.example.smarthospital.PATIENT_NOTIFICATION".equals(intent.getAction())) {
//            int patientId = intent.getIntExtra("patientId", -1);
//            int appointmentId = intent.getIntExtra("appointmentId", -1);
//            int doctorId = intent.getIntExtra("doctorId", -1);
//
//            Log.d(TAG, "Received PATIENT_NOTIFICATION: patientId=" + patientId + ", appointmentId=" + appointmentId + ", doctorId=" + doctorId);
//
//            new Thread(() -> {
//                try {
//                    AppDatabase db = AppDatabase.getInstance(this);
//                    Appointment appointment = db.appointmentDao().getAppointmentById(appointmentId);
//                    User doctor = db.userDao().getUserById(doctorId);
//
//                    if (appointment != null && doctor != null && appointment.getPatientId() == patientId) {
//                        Log.d(TAG, "Queried: appointment=" + appointment + ", doctor=" + doctor.getFullName());
//                        sendPatientNotification(appointment, doctor.getFullName());
//                        Log.d(TAG, "Sent patient notification: appointmentId=" + appointmentId);
//                    } else {
//                        Log.e(TAG, "Failed to send patient notification: appointment=" + appointment + ", doctor=" + doctor);
//                    }
//                } catch (Exception e) {
//                    Log.e(TAG, "Error processing PATIENT_NOTIFICATION", e);
//                }
//            }).start();
//        }
//        return START_STICKY;
//    }
//
//    private void sendPatientNotification(Appointment appointment, String doctorName) {
//        Intent intent = new Intent(this, PatientDashboardActivity.class);
//        intent.putExtra("userId", appointment.getPatientId());
//        PendingIntent pendingIntent = PendingIntent.getActivity(
//                this, appointment.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//
//        Notification notification = new NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID)
//                .setContentTitle("Lịch hẹn được chấp nhận")
//                .setContentText("Bác sĩ " + doctorName + " đã chấp nhận lịch hẹn của bạn vào " +
//                        appointment.getTime() + " ngày " + appointment.getDate() + ". Vui lòng đến đúng giờ.")
//                .setSmallIcon(android.R.drawable.ic_dialog_info)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .setVibrate(new long[]{0, 500, 200, 500, 200, 500})
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .build();
//
//        NotificationHelper.getInstance(this).notify(appointment.getId(), notification);
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        stopForeground(true);
//    }
//}