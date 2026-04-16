package com.example.growtime.weather;

import com.example.growtime.BuildConfig;

/**
 * WeatherAPI.com key from {@code local.properties} → {@link BuildConfig#WEATHER_API_KEY}.
 * Do not commit real keys; set {@code WEATHER_API_KEY=} in project-root {@code local.properties}.
 */
public final class WeatherApiConfig {

    private WeatherApiConfig() {}

    public static String getApiKey() {
        return BuildConfig.WEATHER_API_KEY;
    }

    public static boolean hasApiKey() {
        String key = BuildConfig.WEATHER_API_KEY;
        return key != null && !key.isEmpty();
    }
}
