package com.example.growtime;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.ComponentActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.growtime.access_hardiness_zone.ApiCall;
import com.example.growtime.access_hardiness_zone.DataModel;
import com.example.growtime.json_accessing.AccessJson;
import com.example.growtime.json_accessing.Plant;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HonExtSceneActivity extends ComponentActivity {

    TextView p_name;
    TextView hon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hon_ext_scene);

        p_name = findViewById(R.id.plant_name);
        hon = findViewById(R.id.hon_result);

        Button button = findViewById(R.id.sub_hon);
        button.setOnClickListener(v -> {
            analyze(v);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_honors);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_recommend) {
                startActivity(new Intent(this, RecommendSceneActivity.class));
                finish();
            } else if (id == R.id.nav_my_plants) {
                startActivity(new Intent(this, MyPlantsSceneActivity.class));
                finish();
            }
            return true;
        });
    }

    public void analyze(View view) {
        String plant_name = p_name.getText().toString();
        String l_plant = plant_name.toLowerCase();
        String temp_zip = "01844";

        new ApiCall().getHard(HonExtSceneActivity.this, temp_zip, new ApiCall.CallbackFunction() {
            @Override
            public void onCallback(DataModel data) {
                if (data != null) {
                    String z = data.getZone();
                    Log.d("DEBUG", "Zone string: " + z);

                    int zone = extractZoneNumber(z);
                    Log.d("DEBUG", "Zone number: " + zone);

                    AccessJson accessJson = new AccessJson(HonExtSceneActivity.this);
                    List<Plant> plants = accessJson.parseJSON();
                    Log.d("DEBUG", "Plants parsed: " + plants.size());

                    iterate_json(plants, l_plant, zone);
                }
            }
        });
    }

    public int extractZoneNumber(String zone) {
        return Integer.parseInt(zone.replaceAll("[^0-9]", ""));
    }

    public void iterate_json(List<Plant> p, String n, int zone) {
        // CheckPlant pl = new CheckPlant();
        for (int i = 0; i < p.size(); i++) {
            int min = p.get(i).getH().getMin();
            int max = p.get(i).getH().getMax();
            if (p.get(i).getCommon_name().equals(n)) {
                if (zone >= min && zone <= max) {
                    hon.setText(n + " can be suitably grown here\n");
                } else if (zone < min) {
                    hon.setText("It's too cold for " + n + " to be grown here\n");
                } else {
                    hon.setText("It's too hot for " + n + " to be grown here\n");
                }
            }
        }
    }
}
