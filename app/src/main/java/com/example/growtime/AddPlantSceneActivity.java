package com.example.growtime;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.ComponentActivity;

import com.example.growtime.json_accessing.Hardiness;
import com.example.growtime.json_accessing.MyGardenStore;
import com.example.growtime.json_accessing.Plant;
import com.example.growtime.json_accessing.ZipcodeStore;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddPlantSceneActivity extends ComponentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_plant_scene);

        TextView zipRes = findViewById(R.id.zip_res);
        EditText plantNameInput = findViewById(R.id.plant_name_input);
        EditText waterDaysInput = findViewById(R.id.water_days_input);
        CheckBox indoorCheckbox = findViewById(R.id.indoor_checkbox);
        CheckBox remindersCheckbox = findViewById(R.id.reminders_checkbox);
        Button addPlantButton = findViewById(R.id.add_plant);
        Button cancelButton = findViewById(R.id.cancel_add_plant);
        MyGardenStore myGardenStore = new MyGardenStore(this);

        String savedZip = new ZipcodeStore(this).load().trim();
        if (savedZip.isEmpty()) {
            zipRes.setText("");
        } else {
            zipRes.setText("Zip: " + savedZip);
        }

        addPlantButton.setOnClickListener(v -> {
            String name = plantNameInput.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Enter a plant name", Toast.LENGTH_SHORT).show();
                return;
            }

            int waterDays = 7;
            String waterDaysRaw = waterDaysInput.getText().toString().trim();
            if (!waterDaysRaw.isEmpty()) {
                try {
                    waterDays = Math.max(1, Integer.parseInt(waterDaysRaw));
                } catch (NumberFormatException ignored) {
                    waterDays = 7;
                }
            }

            Plant userPlant = new Plant(name, "average", "", new Hardiness(0, 0));
            boolean added = myGardenStore.addIfMissing(userPlant);
            myGardenStore.update(
                    name,
                    name,
                    waterDays,
                    indoorCheckbox.isChecked(),
                    remindersCheckbox.isChecked()
            );

            if (!added) {
                Toast.makeText(this, "Plant already in My Garden", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Plant added to My Garden", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MyPlantsSceneActivity.class));
            finish();
        });

        cancelButton.setOnClickListener(v -> finish());

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_recommend);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_recommend) {
                startActivity(new Intent(this, RecommendSceneActivity.class));
                finish();
            } else if (id == R.id.nav_honors) {
                startActivity(new Intent(this, HonExtSceneActivity.class));
                finish();
            } else if (id == R.id.nav_my_plants) {
                startActivity(new Intent(this, MyPlantsSceneActivity.class));
                finish();
            }
            return true;
        });
    }
}
