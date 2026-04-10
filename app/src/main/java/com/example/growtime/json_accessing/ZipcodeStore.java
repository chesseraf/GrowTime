package com.example.growtime.json_accessing;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Persists a single user-wide zip code across app launches.
 * Stored separately from the plant list so it can be shared by any scene that needs it.
 */
public class ZipcodeStore {

    private static final String PREFS = "growtime_zipcode";
    private static final String KEY = "zipcode";

    private final SharedPreferences prefs;

    public ZipcodeStore(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void save(String zipcode) {
        prefs.edit().putString(KEY, zipcode).apply();
    }

    /** Returns the saved zip code, or an empty string if none has been saved yet. */
    public String load() {
        return prefs.getString(KEY, "");
    }
}
