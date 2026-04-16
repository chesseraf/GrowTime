package com.example.growtime.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Subset of WeatherAPI.com {@code forecast.json} used for 7-day precipitation totals.
 */
public class WeatherForecastResponse {

    public Forecast forecast;

    public static class Forecast {
        @SerializedName("forecastday")
        public List<ForecastDay> forecastday;
    }

    public static class ForecastDay {
        public Day day;
    }

    public static class Day {
        @SerializedName("totalprecip_mm")
        public double totalprecip_mm;
    }
}
