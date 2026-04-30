package com.example.growtime;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.growtime.json_accessing.MyGardenStore;
import com.example.growtime.json_accessing.StoredPlant;
import com.example.growtime.weather.RainWateringRules;
import com.example.growtime.weather.WeatherForecastRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WateringReminderWorker extends Worker {

    public WateringReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        MyGardenStore gardenStore = new MyGardenStore(context);
        List<StoredPlant> plants = gardenStore.load();
        if (plants.isEmpty()) {
            return Result.success();
        }

        boolean hasOutdoor = false;
        for (StoredPlant sp : plants) {
            if (!sp.indoor) {
                hasOutdoor = true;
                break;
            }
        }

        double rainMm = 0;
        if (hasOutdoor) {
            try {
                rainMm = WeatherForecastRepository.fetchSevenDayTotalPrecipitationSync(context);
            } catch (Exception e) {
                // If weather fetch fails, we continue without rain-reset logic for this cycle
                e.printStackTrace();
            }
        }

        List<String> plantsToWater = new ArrayList<>();
        List<String> plantsRainReset = new ArrayList<>();
        boolean rainDetected = rainMm >= RainWateringRules.SKIP_NEXT_WATERING_TOTAL_MM;
        long now = System.currentTimeMillis();
        boolean modified = false;

        for (StoredPlant sp : plants) {
            if (!sp.getReminders) continue;

            // Handle rain reset for outdoor plants
            if (!sp.indoor) {
                if (sp.skipNextWateringDueToRain || rainDetected) {
                    // Reset the watering timer as if it was watered now
                    sp.lastWateredMillis = now;
                    sp.skipNextWateringDueToRain = false; // Reset the flag after use
                    plantsRainReset.add(sp.plant.getCommon_name());
                    modified = true;
                    continue; // Skip reminder check since we just "watered" it
                }
            }

            // Check if reminder is due
            long intervalMillis = TimeUnit.DAYS.toMillis(sp.water_days);
            if (now - sp.lastWateredMillis >= intervalMillis) {
                plantsToWater.add(sp.plant.getCommon_name());
            }
        }

        if (modified) {
            gardenStore.saveAll(plants);
        }

        if (!plantsRainReset.isEmpty()) {
            NotificationHelper.sendRainResetNotification(context, plantsRainReset);
        }

        if (!plantsToWater.isEmpty()) {
            NotificationHelper.sendWateringReminder(context, plantsToWater);
        }

        return Result.success();
    }

    public static void schedule(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                WateringReminderWorker.class, 4, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "WateringReminder",
                androidx.work.ExistingPeriodicWorkPolicy.KEEP,
                request);
    }
}
