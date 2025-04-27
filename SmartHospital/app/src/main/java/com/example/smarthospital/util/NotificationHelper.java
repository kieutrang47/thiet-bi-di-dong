package com.example.smarthospital.util;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.smarthospital.R;
import com.example.smarthospital.service.AlarmReceiver;
import com.example.smarthospital.ui.DoctorDashboardActivity;
import com.example.smarthospital.ui.PatientDashboardActivity;

public class NotificationHelper {
    public static final String CHANNEL_ID = "SMART_HOSPITAL_CHANNEL";
    private static final String CHANNEL_NAME = "Smart Hospital Notifications";
    private static final String ACTION_DISMISS_ALARM = "com.example.smarthospital.DISMISS_ALARM";
    private final Context context;
    private final NotificationManager notificationManager;
    private static Ringtone currentRingtone;

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for Smart Hospital");
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 500, 200, 500, 200, 500});
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendNotification(String title, String message, int notificationId, int userId) {
        Intent intent = new Intent(context, DoctorDashboardActivity.class);
        intent.putExtra("userId", userId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(notificationId, builder.build());
    }

    public void sendHighPriorityNotification(String title, String message, int notificationId, int userId, boolean isDoctorReminder) {
        Intent intent = isDoctorReminder ?
                new Intent(context, DoctorDashboardActivity.class) :
                new Intent(context, PatientDashboardActivity.class);
        intent.putExtra("userId", userId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        // Full screen intent cho thông báo bác sĩ
        PendingIntent fullScreenIntent = null;
        if (isDoctorReminder) {
            Intent fullScreenActivityIntent = new Intent(context, DoctorDashboardActivity.class);
            fullScreenActivityIntent.putExtra("userId", userId);
            fullScreenIntent = PendingIntent.getActivity(
                    context,
                    notificationId + 1,
                    fullScreenActivityIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
            );
        }

        // Nút "Tắt báo thức" cho bác sĩ
        PendingIntent dismissIntent = null;
        if (isDoctorReminder) {
            Intent dismissAlarmIntent = new Intent(ACTION_DISMISS_ALARM);
            dismissAlarmIntent.putExtra("notificationId", notificationId);
            dismissIntent = PendingIntent.getBroadcast(
                    context,
                    notificationId + 2,
                    dismissAlarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
            );
        }

        // Sử dụng âm thanh tùy chỉnh cho bác sĩ, âm thanh mặc định cho bệnh nhân
        Uri soundUri = isDoctorReminder ?
                Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm_security_breach) :
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Lặp âm thanh cho bác sĩ
        if (isDoctorReminder) {
            try {
                currentRingtone = RingtoneManager.getRingtone(context, soundUri);
                if (currentRingtone != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        currentRingtone.setLooping(true);
                    }
                    currentRingtone.play();
                    // Dừng sau 10 giây hoặc khi nhấn "Tắt"
                    new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                        stopRingtone();
                    }, 10000);
                }
            } catch (Exception e) {
                e.printStackTrace();
                soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM); // Fallback
            }

            // Lên lịch thông báo lặp lại sau 5 phút nếu không tương tác
            scheduleRepeatNotification(notificationId, userId, title, message);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(soundUri)
                .setVibrate(new long[]{0, 500, 200, 500, 200, 500})
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        if (isDoctorReminder) {
            builder.setFullScreenIntent(fullScreenIntent, true);
            builder.addAction(R.drawable.ic_notification, "Tắt báo thức", dismissIntent);
        }

        notificationManager.notify(notificationId, builder.build());
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleRepeatNotification(int notificationId, int userId, String title, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent repeatIntent = new Intent(context, AlarmReceiver.class);
        repeatIntent.putExtra("appointmentId", notificationId - 10000); // Giả sử notificationId = appointmentId + 10000
        repeatIntent.putExtra("doctorId", userId);
        repeatIntent.putExtra("title", title);
        repeatIntent.putExtra("message", message);

        PendingIntent repeatPendingIntent = PendingIntent.getBroadcast(
                context,
                notificationId + 3,
                repeatIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        long triggerTime = System.currentTimeMillis() + 5 * 60 * 1000; // 5 phút sau
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, repeatPendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, repeatPendingIntent);
        }
    }

    public static void stopRingtone() {
        if (currentRingtone != null && currentRingtone.isPlaying()) {
            currentRingtone.stop();
            currentRingtone = null;
        }
    }
}

