package com.example.growtime.perenual_plant_API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlantRetrofitClient {

    private static final String BASE_URL = "https://perenual.com/";

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
