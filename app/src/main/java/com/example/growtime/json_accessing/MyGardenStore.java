package com.example.growtime.json_accessing;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Persists plants the user adds from recommendations into "My Garden".
 */
public class MyGardenStore {

    private static final String PREFS = "growtime_my_garden";
    private static final String KEY_PLANTS = "plants_json";

    private final SharedPreferences prefs;

    public MyGardenStore(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public List<Plant> load() {
        List<Plant> out = new ArrayList<>();
        String raw = prefs.getString(KEY_PLANTS, null);
        if (raw == null || raw.isEmpty()) {
            return out;
        }
        try {
            JSONArray arr = new JSONArray(raw);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String commonName = obj.getString("common_name");
                String watering = obj.getString("waterness");
                String imageUrl = obj.getString("image_url");
                JSONObject hardinessObj = obj.getJSONObject("hardiness");
                int min = Integer.parseInt(hardinessObj.getString("min"));
                int max = Integer.parseInt(hardinessObj.getString("max"));
                out.add(new Plant(commonName, watering, imageUrl, new Hardiness(min, max)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * @return true if the plant was newly added, false if it was already in the garden
     */
    public boolean addIfMissing(Plant plant) {
        List<Plant> current = load();
        String name = plant.getCommon_name();
        for (Plant p : current) {
            if (p.getCommon_name().equalsIgnoreCase(name)) {
                return false;
            }
        }
        current.add(plant);
        saveAll(current);
        return true;
    }

    private void saveAll(List<Plant> plants) {
        JSONArray arr = new JSONArray();
        for (Plant p : plants) {
            try {
                arr.put(plantToJson(p));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        prefs.edit().putString(KEY_PLANTS, arr.toString()).apply();
    }

    private static JSONObject plantToJson(Plant p) throws JSONException {
        JSONObject o = new JSONObject();
        o.put("common_name", p.getCommon_name());
        o.put("waterness", p.getWaterness());
        o.put("image_url", p.getImage_url());
        JSONObject h = new JSONObject();
        h.put("min", String.valueOf(p.getH().getMin()));
        h.put("max", String.valueOf(p.getH().getMax()));
        o.put("hardiness", h);
        return o;
    }
}
