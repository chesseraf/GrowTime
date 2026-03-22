package com.example.growtime.json_accessing;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;


public class AccessJson {

    private final Context context;

    public AccessJson(Context context) {
        this.context = context;
    }

    public String loadJSONFromAsset() {
        try (InputStream is = context.getAssets().open("plant_data.json")) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Plant> parseJSON() {
        List<Plant> plantList = new ArrayList<>();

        try {
            String jsonString = loadJSONFromAsset();
            if (jsonString == null) return plantList;

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                // Get basic fields
                String commonName = obj.getString("common_name");
                String watering = obj.getString("watering");
                String imageUrl = obj.getString("image_url");

                // Get hardiness object
                JSONObject hardinessObj = obj.getJSONObject("hardiness");
                int min = hardinessObj.getInt("min");
                int max = hardinessObj.getInt("max");

                Hardiness hardiness = new Hardiness(min, max);

                // Create Plant object
                Plant plant = new Plant(commonName, watering, imageUrl, hardiness);

                plantList.add(plant);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return plantList;
    }

}
