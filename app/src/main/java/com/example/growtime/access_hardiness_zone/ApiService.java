package com.example.growtime.access_hardiness_zone;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/{zip}.json")
    Call<DataModel> getZone(@Path("zip") String zip);
}
