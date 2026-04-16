package com.example.growtime.json_accessing;

/**
 * A plant that has been saved to the user's garden, combining the base Plant
 * data with user-specific settings and tracking fields.
 */
public class StoredPlant {
    public final Plant plant;
    public boolean getReminders = true;  // whether to send watering reminders
    public boolean indoor = false;
    public int water_days;               // how often to water, in days
    public long dateAddedMillis;         // epoch ms when the plant was added to the garden
    public long lastWateredMillis;       // epoch ms of the most recent watering
    /** Outdoor: set when marking watered if 7-day forecast rain is above threshold; consumed later by reminders. */
    public boolean skipNextWateringDueToRain = false;

    public StoredPlant(Plant p) {
        plant = p;
        setWaterFrequency();
        dateAddedMillis = System.currentTimeMillis();
        // Treat the add date as the first watering so the schedule starts from today
        lastWateredMillis = dateAddedMillis;
    }

    /**
     * Converts the plant's waterness string from the plant database into a
     * concrete number of days between waterings.
     */
    private void setWaterFrequency() {
        switch (plant.getWaterness()) {
            case "minimum":  water_days = 15; break;
            case "average":  water_days = 7;  break;
            case "frequent": water_days = 3;  break;
            default:         water_days = 14; break;
        }
    }
}
