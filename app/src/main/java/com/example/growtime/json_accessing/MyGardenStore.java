package com.example.growtime.json_accessing;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Persists the user's garden as a JSON array in SharedPreferences.
 * Each entry is a StoredPlant (plant data + user settings like indoor, reminders, water schedule).
 */
public class MyGardenStore {

    private static final String PREFS = "growtime_my_garden";
    private static final String KEY_PLANTS = "plants_json";

    private final SharedPreferences prefs;

    public MyGardenStore(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    /** Loads all stored plants. Returns an empty list if nothing has been saved yet. */
    public List<StoredPlant> load() {
        List<StoredPlant> out = new ArrayList<>();
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
                Plant plant = new Plant(commonName, watering, imageUrl, new Hardiness(min, max));
                StoredPlant sp = new StoredPlant(plant);
                sp.getReminders = obj.getBoolean("getReminders");
                sp.indoor = obj.getBoolean("indoor");
                sp.water_days = obj.getInt("water_days");
                sp.dateAddedMillis = obj.optLong("date_added", sp.dateAddedMillis);
                sp.lastWateredMillis = obj.optLong("last_watered", sp.lastWateredMillis);
                sp.skipNextWateringDueToRain = obj.optBoolean("skip_next_water_rain", false);
                out.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * Adds the plant to the garden if it is not already present (matched by name).
     * @return true if the plant was newly added, false if it was already in the garden.
     */
    public boolean addIfMissing(Plant plant) {
        List<StoredPlant> current = load();
        String name = plant.getCommon_name();
        for (StoredPlant sp : current) {
            if (sp.plant.getCommon_name().equalsIgnoreCase(name)) {
                return false;
            }
        }
        current.add(new StoredPlant(plant));
        saveAll(current);
        return true;
    }

    /**
     * Updates the editable fields of a stored plant identified by its original name.
     * A new StoredPlant is constructed rather than mutating in-place because
     * StoredPlant.plant is final.
     */
    public void update(String originalName, String newName, int waterDays, boolean indoor, boolean getReminders) {
        List<StoredPlant> current = load();
        for (int i = 0; i < current.size(); i++) {
            StoredPlant sp = current.get(i);
            if (sp.plant.getCommon_name().equalsIgnoreCase(originalName)) {
                Plant updated = new Plant(newName, sp.plant.getWaterness(), sp.plant.getImage_url(), sp.plant.getH());
                StoredPlant replacement = new StoredPlant(updated);
                replacement.getReminders = getReminders;
                replacement.indoor = indoor;
                replacement.water_days = waterDays;
                replacement.dateAddedMillis = sp.dateAddedMillis;
                replacement.lastWateredMillis = sp.lastWateredMillis;
                replacement.skipNextWateringDueToRain = sp.skipNextWateringDueToRain;
                current.set(i, replacement);
                break;
            }
        }
        saveAll(current);
    }

    /** Records the current time as the last-watered date for the named plant. */
    public void updateLastWatered(String commonName) {
        markWateredWithRainDecision(commonName, false);
    }

    /**
     * Sets last watered to now and stores whether the next watering cycle should be skipped
     * (e.g. outdoor plant with enough forecast rain). Notifications can read {@code skipNextWateringDueToRain} later.
     */
    public void markWateredWithRainDecision(String commonName, boolean skipNextWateringDueToRain) {
        List<StoredPlant> current = load();
        for (StoredPlant sp : current) {
            if (sp.plant.getCommon_name().equalsIgnoreCase(commonName)) {
                sp.lastWateredMillis = System.currentTimeMillis();
                sp.skipNextWateringDueToRain = skipNextWateringDueToRain;
                break;
            }
        }
        saveAll(current);
    }

    /** Permanently removes a plant from the garden by name. */
    public void remove(String commonName) {
        List<StoredPlant> current = load();
        current.removeIf(sp -> sp.plant.getCommon_name().equalsIgnoreCase(commonName));
        saveAll(current);
    }

    private void saveAll(List<StoredPlant> plants) {
        JSONArray arr = new JSONArray();
        for (StoredPlant sp : plants) {
            try {
                arr.put(storedPlantToJson(sp));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        prefs.edit().putString(KEY_PLANTS, arr.toString()).apply();
    }

    private static JSONObject storedPlantToJson(StoredPlant sp) throws JSONException {
        JSONObject o = new JSONObject();
        Plant p = sp.plant;
        o.put("common_name", p.getCommon_name());
        o.put("waterness", p.getWaterness());
        o.put("image_url", p.getImage_url());
        JSONObject h = new JSONObject();
        h.put("min", String.valueOf(p.getH().getMin()));
        h.put("max", String.valueOf(p.getH().getMax()));
        o.put("hardiness", h);
        o.put("getReminders", sp.getReminders);
        o.put("indoor", sp.indoor);
        o.put("water_days", sp.water_days);
        o.put("date_added", sp.dateAddedMillis);
        o.put("last_watered", sp.lastWateredMillis);
        o.put("skip_next_water_rain", sp.skipNextWateringDueToRain);
        return o;
    }
}
