package com.example.growtime.weather;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

final class WeatherForecastClient {

    private static final String BASE_URL = "https://api.weatherapi.com/v1/";

    private static volatile Retrofit retrofit;

    private WeatherForecastClient() {}

    static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (WeatherForecastClient.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }
}
