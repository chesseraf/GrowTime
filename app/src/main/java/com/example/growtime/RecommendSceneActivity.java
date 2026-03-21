package com.example.growtime;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
/*import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;*/
import androidx.activity.ComponentActivity;

// import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

import com.example.growtime.access_hardiness_zone.ApiCall;
import com.example.growtime.access_hardiness_zone.DataModel;
import com.example.growtime.perenual_plant_API.PlantResponse;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.growtime.perenual_plant_API.PlantApiService;
import com.example.growtime.perenual_plant_API.PlantDataModel;
import com.example.growtime.perenual_plant_API.PlantRetrofitClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecommendSceneActivity extends ComponentActivity {
    private static final String API_KEY = "sk-pjES69bee1ad3a91615648";
    EditText zipcode_input;
    TextView zip_res;
    TextView hard;

    TextView plantResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recommend_scene);

        zipcode_input = findViewById(R.id.zipcode_input);
        zip_res = findViewById(R.id.zip_res);
        hard = findViewById(R.id.hard_zone);
        plantResults = findViewById(R.id.plant_results);

        Button button = findViewById(R.id.sub_butt);

        button.setOnClickListener(v -> {
            updateText(v);
            showZone(v);
            viewSuitablePlants(v);
        });
    }

    public void updateText(View view){
        String zip = zipcode_input.getText().toString();
        String res = "Zip: " + zip;
        zip_res.setText(res);
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

    public void viewSuitablePlants(View view) {
        plantResults.setText("Loading...");

        PlantApiService plantApi = PlantRetrofitClient.getClient().create(PlantApiService.class);
        String zip = zipcode_input.getText().toString();

        new ApiCall().getHard(this, zip, new ApiCall.CallbackFunction() {
            @Override
            public void onCallback(DataModel data) {
                if (data == null) {
                    plantResults.setText("Failed to get zone");
                    return;
                }

                String zoneStr = data.getZone();
                int userZone = extractZoneNumber(zoneStr);

                runOnUiThread(() -> hard.setText("Zone: " + zoneStr));

                // ✅ ONLY ONE API CALL
                Call<PlantResponse> call = plantApi.getPlants(API_KEY, "", RandomHelper());

                call.enqueue(new Callback<PlantResponse>() {
                    @Override
                    public void onResponse(Call<PlantResponse> call, Response<PlantResponse> response) {

                        if (!response.isSuccessful() || response.body() == null) {
                            plantResults.setText("API error: " + response.code());
                            return;
                        }

                        List<String> found = new ArrayList<>();

                        for (PlantDataModel plant : response.body().getData()) {
                            if (plant.getHardiness() == null) continue;

                            String minStr = plant.getHardiness().getMin();
                            String maxStr = plant.getHardiness().getMax();

                            if (minStr == null || maxStr == null) continue;

                            try {
                                int min = extractZoneNumber(minStr);
                                int max = extractZoneNumber(maxStr);

                                if (userZone >= min && userZone <= max) {
                                    String name = plant.getCommonName();

                                    if (name != null && !found.contains(name)) {
                                        found.add(name);
                                    }
                                }

                            } catch (Exception ignored) {}

                            if (found.size() >= 3) break;
                        }

                        if (found.isEmpty()) {
                            plantResults.setText("No plants found for your zone.");
                        } else {
                            plantResults.setText(String.join("\n", found));
                        }
                    }

                    @Override
                    public void onFailure(Call<PlantResponse> call, Throwable t) {
                        plantResults.setText("Network error: " + t.getMessage());
                    }
                });
            }
        });
    }
    public int extractZoneNumber(String zone) {
        return Integer.parseInt(zone.replaceAll("[^0-9]", ""));
    }

    public int RandomHelper() {
        Random random = new Random();
        int randomPage = 0;
        return randomPage = random.nextInt(50) + 1;
    }
}