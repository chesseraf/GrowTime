package com.example.growtime.access_hardiness_zone;

import android.content.Context;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCall {
    // This function takes a Context and callback function
    // as a parameter, which will be called
    // when the API response is received.
    public void getHard(Context context, String zip, final CallbackFunction callback) {

        // Create a Retrofit instance with the base URL and
        // a GsonConverterFactory for parsing the response.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://phzmapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an ApiService instance from the Retrofit instance.
        ApiService service = retrofit.create(ApiService.class);

        // Call the getJokes() method of the ApiService
        // to make an API request.
        Call<DataModel> call = service.getZone(zip);

        // Use the enqueue() method of the Call object to
        // make an asynchronous API request.
        call.enqueue(new Callback<DataModel>() {
            // This is an anonymous inner class that implements the Callback interface.
            @Override
            public void onResponse(Call<DataModel> call, Response<DataModel> response) {
                // This method is called when the API response is received successfully.

                if (response.isSuccessful()) {
                    // If the response is successful, parse the
                    // response body to a DataModel object.
                    DataModel jokes = response.body();

                    // Call the callback function with the DataModel
                    // object as a parameter.
                    callback.onCallback(jokes);
                }
            }

            @Override
            public void onFailure(Call<DataModel> call, Throwable t) {
                // This method is called when the API request fails.
                Toast.makeText(context, "Request Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface CallbackFunction {
        void onCallback(DataModel data);
    }
}
