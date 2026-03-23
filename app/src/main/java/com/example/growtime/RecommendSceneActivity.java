package com.example.growtime;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.ComponentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

import com.example.growtime.access_hardiness_zone.ApiCall;
import com.example.growtime.access_hardiness_zone.DataModel;
import com.example.growtime.json_accessing.AccessJson;
import com.example.growtime.json_accessing.CheckPlant;
import com.example.growtime.json_accessing.Plant;
import com.example.growtime.json_accessing.PlantAdapter;

import java.util.List;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RecommendSceneActivity extends ComponentActivity {

    EditText zipcode_input;
    TextView zip_res;
    TextView hard;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recommend_scene);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        zipcode_input = findViewById(R.id.zipcode_input);
        zip_res = findViewById(R.id.zip_res);
        hard = findViewById(R.id.hard_zone);

        Button button = findViewById(R.id.sub_butt);
        button.setOnClickListener(v -> {
            updateText(v);
            // showZone(v);
            showPlants(v);
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_recommend);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_honors) {
                startActivity(new Intent(this, HonExtSceneActivity.class));
                finish();
            } else if (id == R.id.nav_my_plants) {
                startActivity(new Intent(this, MyPlantsSceneActivity.class));
                finish();
            }
            return true;
        });
    }

    public void updateText(View view) {
        String zip = zipcode_input.getText().toString();
        zip_res.setText("Zip: " + zip);
    }

    public void showZone(View view) {
        String zip = zipcode_input.getText().toString();
        new ApiCall().getHard(RecommendSceneActivity.this, zip, new ApiCall.CallbackFunction() {
            @Override
            public void onCallback(DataModel data) {
                if (data != null) {
                    hard.setText(data.getZone());
                }
            }
        });
    }

/*    public void showPlants(View view) {
        String zip = zipcode_input.getText().toString();
        new ApiCall().getHard(RecommendSceneActivity.this, zip, new ApiCall.CallbackFunction() {
            @Override
            public void onCallback(DataModel data) {
                if (data != null) {
                    String z = data.getZone();
                    int zone = extractZoneNumber(z);

                    // ✅ Create AccessJson object
                    AccessJson accessJson = new AccessJson(RecommendSceneActivity.this);

                    // ✅ Get plant list
                    List<Plant> plants = accessJson.parseJSON();

                    CheckPlant check = new CheckPlant();
                    List<Plant> suitable = check.AddPlant(plants, zone);

                    // ✅ Display in RecyclerView
                    displaySuitable(suitable);

                }
            }
        });
    }*/

    public void showPlants(View view) {
        String zip = zipcode_input.getText().toString();
        new ApiCall().getHard(RecommendSceneActivity.this, zip, new ApiCall.CallbackFunction() {
            @Override
            public void onCallback(DataModel data) {
                if (data != null) {
                    String z = data.getZone();
                    Log.d("DEBUG", "Zone string: " + z);

                    int zone = extractZoneNumber(z);
                    Log.d("DEBUG", "Zone number: " + zone);

                    AccessJson accessJson = new AccessJson(RecommendSceneActivity.this);
                    List<Plant> plants = accessJson.parseJSON();
                    Log.d("DEBUG", "Plants parsed: " + plants.size());

                    CheckPlant check = new CheckPlant();
                    List<Plant> suitable = check.AddPlant(plants, zone);
                    Log.d("DEBUG", "Suitable plants: " + suitable.size());

                    displaySuitable(suitable);
                } else {
                    Log.d("DEBUG", "data is null");
                }
            }
        });
    }

    public int extractZoneNumber(String zone) {
        return Integer.parseInt(zone.replaceAll("[^0-9]", ""));
    }

    public void displaySuitable(List<Plant> p) {
        runOnUiThread(() -> {
            PlantAdapter adapter = new PlantAdapter(this, p);
            recyclerView.setAdapter(adapter);
        });
    }
}