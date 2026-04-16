package com.example.growtime.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {

    @GET("forecast.json")
    Call<WeatherForecastResponse> getForecast(
            @Query("key") String apiKey,
            @Query("q") String query,
            @Query("days") int days
    );
}
