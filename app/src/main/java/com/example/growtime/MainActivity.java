package com.example.growtime;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import com.example.growtime.weather.WeatherForecastRepository;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends ComponentActivity {

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Notification Channel
        NotificationHelper.createNotificationChannel(this);

        // Schedule Watering Reminder Worker
        WateringReminderWorker.schedule(this);

        // Request permission for Android 13+
        checkNotificationPermission();

        findViewById(R.id.RecommendButton).setOnClickListener(v ->
                startActivity(new Intent(this, RecommendSceneActivity.class)));

        findViewById(R.id.Honors_button).setOnClickListener(v ->
                startActivity(new Intent(this, HonExtSceneActivity.class)));

        findViewById(R.id.LocationButton).setOnClickListener(v ->
                startActivity(new Intent(this, LocationSceneActivity.class)));

        findViewById(R.id.MyGarden).setOnClickListener(v ->
                startActivity(new Intent(this, MyPlantsSceneActivity.class)));

        findViewById(R.id.addPlantHome).setOnClickListener(v ->
                startActivity(new Intent(this, AddPlantSceneActivity.class)));

        findViewById(R.id.editPlantHome).setOnClickListener(v ->
                startActivity(new Intent(this, EditPlantSceneActivity.class)));

        findViewById(R.id.testNotificationButton).setOnClickListener(v -> {
            OneTimeWorkRequest testRequest = new OneTimeWorkRequest.Builder(WateringReminderWorker.class).build();
            WorkManager.getInstance(this).enqueue(testRequest);
            Toast.makeText(this, "Manual watering check triggered", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.testWeatherApiButton).setOnClickListener(v ->
                WeatherForecastRepository.fetchSevenDayTotalPrecipitation(this,
                        new WeatherForecastRepository.Listener() {
                            @Override
                            public void onSuccess(double totalPrecipMm) {
                                Toast.makeText(
                                        MainActivity.this,
                                        getString(R.string.weather_forecast_result, totalPrecipMm),
                                        Toast.LENGTH_LONG
                                ).show();
                            }

                            @Override
                            public void onError(String message) {
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                            }
                        }));
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
}
