package com.example.growtime;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;

public class NotificationHelper {
    public static final String CHANNEL_ID = "growtime_notifications";
    public static final String CHANNEL_NAME = "GrowTime Reminders";
    public static final String CHANNEL_DESC = "Notifications for plant care reminders";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static void sendWateringReminder(Context context, List<String> plantNames) {
        if (plantNames == null || plantNames.isEmpty()) return;

        String title = context.getString(R.string.notification_watering_reminder_title);
        String message;
        if (plantNames.size() == 1) {
            message = context.getString(R.string.notification_watering_reminder_single, plantNames.get(0));
        } else {
            message = context.getString(R.string.notification_watering_reminder_multiple, String.join(", ", plantNames));
        }
        sendNotification(context, title, message);
    }

    public static void sendRainResetNotification(Context context, List<String> plantNames) {
        if (plantNames == null || plantNames.isEmpty()) return;

        String title = context.getString(R.string.notification_rain_reset_title);
        String message;
        if (plantNames.size() == 1) {
            message = context.getString(R.string.notification_rain_reset_single, plantNames.get(0));
        } else {
            message = context.getString(R.string.notification_rain_reset_multiple, String.valueOf(plantNames.size()));
        }
        sendNotification(context, title, message);
    }

    public static void sendNotification(Context context, String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Use a default icon for now
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        try {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        } catch (SecurityException e) {
            // Handle the case where permission is not granted
            e.printStackTrace();
        }
    }
}
