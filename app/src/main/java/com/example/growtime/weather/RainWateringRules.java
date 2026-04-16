package com.example.growtime.weather;

/**
 * When total forecast rain for the next 7 days exceeds this (mm), the next watering cycle
 * is skipped for outdoor plants (see {@code skipNextWateringDueToRain} on {@code StoredPlant}).
 */
public final class RainWateringRules {

    public static final double SKIP_NEXT_WATERING_TOTAL_MM = 10.0;

    private RainWateringRules() {}
}
