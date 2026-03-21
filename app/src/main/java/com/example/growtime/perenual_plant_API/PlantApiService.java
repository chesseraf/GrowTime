package com.example.growtime.perenual_plant_API;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlantApiService {

    @GET("api/species-list")
    Call<PlantResponse> getPlants(
            @Query("key") String apiKey,
            @Query("q") String query
    );
}
