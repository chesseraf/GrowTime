package com.example.growtime;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.ComponentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.growtime.json_accessing.MyGardenStore;
import com.example.growtime.json_accessing.Plant;
import com.example.growtime.json_accessing.PlantAdapter;

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
        List<Plant> plants = gardenStore.load();
        boolean empty = plants.isEmpty();
        emptyView.setVisibility(empty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
        if (!empty) {
            recyclerView.setAdapter(new PlantAdapter(this, plants));
        }
    }
}
