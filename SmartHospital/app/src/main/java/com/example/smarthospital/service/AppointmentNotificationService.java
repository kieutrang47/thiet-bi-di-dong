package com.example.smarthospital.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.example.smarthospital.database.AppDatabase;
import com.example.smarthospital.model.Appointment;
import com.example.smarthospital.util.NotificationHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentNotificationService extends Service {
    private AppDatabase db;
    private int doctorId;
    private Handler handler;
    private Runnable checkAppointmentsRunnable;
    private static final long CHECK_INTERVAL = 60 * 1000; // Check every minute
    private static final String ACTION_NEW_APPOINTMENT = "com.example.smarthospital.NEW_APPOINTMENT";

    @Override
    public void onCreate() {
        super.onCreate();
        db = AppDatabase.getInstance(this);
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            doctorId = intent.getIntExtra("doctorId", -1);
            if (ACTION_NEW_APPOINTMENT.equals(intent.getAction())) {
                handleNewAppointment(intent.getIntExtra("appointmentId", -1));
            }
            startCheckingAppointments();
            scheduleReminders();
        }
        return START_STICKY;
    }

    private void handleNewAppointment(int appointmentId) {
        new Thread(() -> {
            Appointment appointment = db.appointmentDao().getAppointmentById(appointmentId);
            if (appointment != null && appointment.getDoctorId() == doctorId) {
                String patientName = db.userDao().getUserById(appointment.getPatientId()).getFullName();
                NotificationHelper notificationHelper = new NotificationHelper(this);
                notificationHelper.sendNotification(
                        "Lịch hẹn mới",
                        "Bệnh nhân " + patientName + " đã đặt lịch khám vào " + appointment.getDate() + " " + appointment.getTime(),
                        appointmentId,
                        doctorId
                );
            }
        }).start();
    }

    private void startCheckingAppointments() {
        checkAppointmentsRunnable = new Runnable() {
            @Override
            public void run() {
                checkForNewAppointments();
                handler.postDelayed(this, CHECK_INTERVAL);
            }
        };
        handler.post(checkAppointmentsRunnable);
    }

    private void checkForNewAppointments() {
        new Thread(() -> {
            List<Appointment> appointments = db.appointmentDao().getAppointmentsByDoctor(doctorId);
            for (Appointment appointment : appointments) {
                if ("pending".equals(appointment.getStatus())) {
                    handleNewAppointment(appointment.getId());
                }
            }
        }).start();
    }

    private void scheduleReminders() {
        new Thread(() -> {
            List<Appointment> appointments = db.appointmentDao().getAppointmentsByDoctor(doctorId);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Calendar now = Calendar.getInstance();
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(now.getTime());

            for (Appointment appointment : appointments) {
                if (!"accepted".equals(appointment.getStatus())) continue;
                try {
                    String appointmentDateTime = appointment.getDate() + " " + appointment.getTime();
                    Date date = dateFormat.parse(appointmentDateTime);
                    if (date == null || !appointment.getDate().equals(today)) continue;

                    Calendar appointmentCal = Calendar.getInstance();
                    appointmentCal.setTime(date);
                    long reminderTime = appointmentCal.getTimeInMillis() - 30 * 60 * 1000; // 30 minutes before

                    if (reminderTime > now.getTimeInMillis()) {
                        scheduleReminder(appointment, reminderTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleReminder(Appointment appointment, long reminderTime) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("appointmentId", appointment.getId());
        intent.putExtra("doctorId", doctorId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                appointment.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                reminderTime,
                pendingIntent
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null && checkAppointmentsRunnable != null) {
            handler.removeCallbacks(checkAppointmentsRunnable);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}