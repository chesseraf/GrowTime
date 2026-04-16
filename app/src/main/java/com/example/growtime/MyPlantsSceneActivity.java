package com.example.growtime;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.ComponentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.growtime.json_accessing.GardenPlantAdapter;
import com.example.growtime.json_accessing.MyGardenStore;
import com.example.growtime.json_accessing.StoredPlant;
import com.example.growtime.json_accessing.ZipcodeStore;
import com.example.growtime.weather.RainWateringRules;
import com.example.growtime.weather.WeatherApiConfig;
import com.example.growtime.weather.WeatherForecastRepository;

import java.util.List;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyPlantsSceneActivity extends ComponentActivity {

    private RecyclerView recyclerView;
    private View emptyView;
    private MyGardenStore gardenStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.my_plants_scene);

        gardenStore = new MyGardenStore(this);
        recyclerView = findViewById(R.id.my_garden_recycler);
        emptyView = findViewById(R.id.my_garden_empty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_my_plants);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_recommend) {
                startActivity(new Intent(this, RecommendSceneActivity.class));
                finish();
            } else if (id == R.id.nav_honors) {
                startActivity(new Intent(this, HonExtSceneActivity.class));
                finish();
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshGardenList();
    }

    private void refreshGardenList() {
        List<StoredPlant> plants = gardenStore.load();
        boolean empty = plants.isEmpty();
        emptyView.setVisibility(empty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
        if (!empty) {
            recyclerView.setAdapter(new GardenPlantAdapter(this, plants, sp -> {
                Intent intent = new Intent(this, EditPlantSceneActivity.class);
                intent.putExtra("plant_name", sp.plant.getCommon_name());
                intent.putExtra("image_url", sp.plant.getImage_url());
                intent.putExtra("indoor", sp.indoor);
                intent.putExtra("get_reminders", sp.getReminders);
                intent.putExtra("water_days", sp.water_days);
                intent.putExtra("last_watered", sp.lastWateredMillis);
                startActivity(intent);
            }, this::onMarkWatered));
        }
    }

    private void onMarkWatered(StoredPlant sp) {
        String name = sp.plant.getCommon_name();
        if (sp.indoor) {
            gardenStore.markWateredWithRainDecision(name, false);
            refreshGardenList();
            Toast.makeText(this, R.string.water_marked_indoor, Toast.LENGTH_LONG).show();
            return;
        }
        if (!WeatherApiConfig.hasApiKey() || new ZipcodeStore(this).load().trim().isEmpty()) {
            gardenStore.markWateredWithRainDecision(name, false);
            refreshGardenList();
            Toast.makeText(this, R.string.water_marked_no_rain_setup, Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, R.string.water_checking_forecast, Toast.LENGTH_SHORT).show();
        WeatherForecastRepository.fetchSevenDayTotalPrecipitation(this, new WeatherForecastRepository.Listener() {
            @Override
            public void onSuccess(double totalPrecipMm) {
                boolean skipNext = totalPrecipMm > RainWateringRules.SKIP_NEXT_WATERING_TOTAL_MM;
                gardenStore.markWateredWithRainDecision(name, skipNext);
                refreshGardenList();
                int msg = skipNext ? R.string.water_marked_skip_next : R.string.water_marked_water_next;
                Toast.makeText(MyPlantsSceneActivity.this, getString(msg, totalPrecipMm), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String message) {
                gardenStore.markWateredWithRainDecision(name, false);
                refreshGardenList();
                Toast.makeText(
                        MyPlantsSceneActivity.this,
                        getString(R.string.water_marked_forecast_failed, message),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}
