package com.example.growtime;

import android.content.Context;
import android.util.Log;

import com.example.growtime.json_accessing.Hardiness;
import com.example.growtime.json_accessing.MyGardenStore;
import com.example.growtime.json_accessing.Plant;
import com.example.growtime.json_accessing.StoredPlant;
import com.example.growtime.weather.RainWateringRules;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestConfig {
    // When true, app generates logs for a figure on launch
    // This does not interfere with anything else, but kept false just in case
    private static final boolean TESTING_ENABLED = false;

    private static final double[] RAIN_MM_PER_DAY = {
            0.0,  2.5,  10.0, 0.0,  0.0,
            3.0,  0.0,  5.0,  0, 0.0,
            0.0,  0.0,  5.0,  0.0,  0.0,
            12.0, 0.0,  4.0,  0.0,  0.0
    };

    private static long startTimeMs = -1;
    private static int day_to_mili = 250;

    public static boolean isTestingEnabled() {
        return TESTING_ENABLED;
    }

    private static void log(String message) {
        if (TESTING_ENABLED) {
            Log.d("output", message);
        }
    }

    public static void runTest(MyGardenStore store) {
        startTimeMs = System.currentTimeMillis();

        List<StoredPlant> ogPlants = store.load();
        List<StoredPlant> testPlants = new ArrayList<>();
        for (char name = 'a'; name < 'd'; name++ )
            testPlants.add(new StoredPlant((new Plant(String.valueOf(name),
                    "frequent", "non-url", new Hardiness(0, 1)))));

        testPlants.get(0).indoor = true;
        testPlants.get(0).water_days = 5;
        testPlants.get(2).getReminders = false;

        store.saveAll(testPlants);

        for (int i = 0; i < 20; i++) {
            testNotifierLogic(store);
            if (i < 19) {
                try {
                    Thread.sleep(day_to_mili);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        store.saveAll(ogPlants);
    }

    private static int dayNumFromTime(Long milis) {
        return (int)(milis - startTimeMs + day_to_mili/2) / day_to_mili;
    }

    private static void testNotifierLogic(MyGardenStore gardenStore) {
        List<StoredPlant> plants = gardenStore.load();
        if (plants.isEmpty()) return;

        boolean hasOutdoor = false;
        for (StoredPlant sp : plants) {
            if (!sp.indoor) {
                hasOutdoor = true;
                break;
            }
        }

        long now = System.currentTimeMillis();
        int currentDay = dayNumFromTime(now);

        double rainMm = 0;
        if (hasOutdoor) {
            int end = Math.min(currentDay + 7, RAIN_MM_PER_DAY.length);
            for (int i = currentDay; i < end; i++) {
                rainMm += RAIN_MM_PER_DAY[i];
            }
        }

        List<String> plantsToWater = new ArrayList<>();
        List<String> plantsRainReset = new ArrayList<>();
        boolean rainDetected = rainMm >= RainWateringRules.SKIP_NEXT_WATERING_TOTAL_MM;
        boolean modified = false;

        for (StoredPlant sp : plants) {
            if (!sp.getReminders) continue;

            if (!sp.indoor) {
                if (sp.skipNextWateringDueToRain || rainDetected) {
                    sp.lastWateredMillis = now;
                    sp.skipNextWateringDueToRain = false;
                    plantsRainReset.add(sp.plant.getCommon_name());
                    modified = true;
                    continue;
                }
            }

            int waterFrequency = sp.water_days;
            int dayWatered = dayNumFromTime(sp.lastWateredMillis);
            if (currentDay - dayWatered >= waterFrequency) {
                modified = true;
                plantsToWater.add(sp.plant.getCommon_name());
                sp.lastWateredMillis = now;
            }
        }

        if (modified) {
            gardenStore.saveAll(plants);
        }
        log(currentDay + " : " + String.join(" ", plantsToWater));
    }
}
