package com.example.shoppinglist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AppService extends Service {

    private static final String CHANNEL_ID = "ShoppingListChannel";
    private static final int NOTIFICATION_ID = 123;
    private static final int NOTIFICATION_INTERVAL_MS = 60000;
    //private static final int PERMISSION_REQUEST_POST_NOTIFICATIONS = 1;

    private Handler mainHandler;
    private Runnable notificationRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        mainHandler = new Handler(Looper.getMainLooper());
        notificationRunnable = new Runnable() {
            @Override
            public void run() {
                sendNotification();
                // Wysłanie wiadomości co minutę
                mainHandler.postDelayed(this, NOTIFICATION_INTERVAL_MS);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mainHandler.post(notificationRunnable);
        return START_STICKY;
    }

    // metoda wywołana przed zniszczeniem usuługi
    // usuwa oczekujące zadania z handlera
    @Override
    public void onDestroy() {
        super.onDestroy();
        mainHandler.removeCallbacks(notificationRunnable);
    }

    // nie używane
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @SuppressLint("InlinedApi")
    private void sendNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Budowa powiadomienia
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.bell)
                .setContentTitle("Lista zakupów.")
                .setContentText("Nie zapomnij sprawdzić swojej listy zakupów!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Sprawdzenie uprawnień do wysyłania powiadomień
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Intent permissionIntent = new Intent(this, PermissionRequestActivity.class);
            permissionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(permissionIntent);
            return;
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        // Utworzenie kanału do powiadomień
        CharSequence name = "Kanał powiadomień aplikacji";
        String description = "Kanał do powiadomień o liście zakupów.";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public static class PermissionRequestActivity extends Activity {
        static final int PERMISSION_REQUEST_POST_NOTIFICATIONS = 1;

        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Prośba o uprawnienia do wysyłania powiadomień
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_POST_NOTIFICATIONS);
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
            if (requestCode == PERMISSION_REQUEST_POST_NOTIFICATIONS) {
                finish();
            }
        }
    }

}