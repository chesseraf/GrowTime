package com.example.growtime;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.ComponentActivity;

import com.bumptech.glide.Glide;
import com.example.growtime.json_accessing.MyGardenStore;
import com.example.growtime.json_accessing.ZipcodeStore;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EditPlantSceneActivity extends ComponentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.edit_plant_scene);

        String originalName = getIntent().getStringExtra("plant_name");
        String imageUrl = getIntent().getStringExtra("image_url");
        boolean currentIndoor = getIntent().getBooleanExtra("indoor", false);
        boolean currentReminders = getIntent().getBooleanExtra("get_reminders", true);
        int currentWaterDays = getIntent().getIntExtra("water_days", 7);

        TextView zipView = findViewById(R.id.zip_res);
        zipView.setText(new ZipcodeStore(this).load());

        ImageView plantImage = findViewById(R.id.edit_plant_image);
        Glide.with(this).load(imageUrl).into(plantImage);

        EditText nameField = findViewById(R.id.edit_plant_name);
        nameField.setText(originalName);

        EditText waterDaysField = findViewById(R.id.edit_water_days);
        waterDaysField.setText(String.valueOf(currentWaterDays));

        CheckBox checkboxIndoor = findViewById(R.id.checkbox_indoor);
        CheckBox checkboxReminders = findViewById(R.id.checkbox_reminders);
        checkboxIndoor.setChecked(currentIndoor);
        checkboxReminders.setChecked(currentReminders);

        MyGardenStore gardenStore = new MyGardenStore(this);

        Button saveButton = findViewById(R.id.Save_edit);
        saveButton.setOnClickListener(v -> {
            String newName = nameField.getText().toString().trim();
            if (newName.isEmpty()) newName = originalName;

            int newWaterDays = currentWaterDays;
            try {
                newWaterDays = Integer.parseInt(waterDaysField.getText().toString().trim());
            } catch (NumberFormatException ignored) {}

            gardenStore.update(originalName, newName, newWaterDays,
                    checkboxIndoor.isChecked(), checkboxReminders.isChecked());
            startActivity(new Intent(this, MyPlantsSceneActivity.class));
            finish();
        });

        Button cancelButton = findViewById(R.id.cancel_edit);
        cancelButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MyPlantsSceneActivity.class));
            finish();
        });

        Button deleteButton = findViewById(R.id.delete_plant);
        deleteButton.setOnClickListener(v -> {
            gardenStore.remove(originalName);
            startActivity(new Intent(this, MyPlantsSceneActivity.class));
            finish();
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
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
