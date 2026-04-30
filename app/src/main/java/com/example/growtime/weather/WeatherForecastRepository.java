package com.example.growtime.weather;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.growtime.R;
import com.example.growtime.json_accessing.ZipcodeStore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

/**
 * Fetches WeatherAPI.com forecast for the zip saved in {@link ZipcodeStore} and sums daily precipitation.
 */
public final class WeatherForecastRepository {

    private static final int FORECAST_DAYS = 7;

    public interface Listener {
        void onSuccess(double totalPrecipMm);

        void onError(String message);
    }

    private WeatherForecastRepository() {}

    public static void fetchSevenDayTotalPrecipitation(Context context, Listener listener) {
        new Thread(() -> {
            try {
                double totalMm = fetchSevenDayTotalPrecipitationSync(context);
                postMain(context, () -> listener.onSuccess(totalMm));
            } catch (Exception e) {
                postMain(context, () -> listener.onError(e.getMessage()));
            }
        }).start();
    }

    /** Synchronous version for background workers. */
    public static double fetchSevenDayTotalPrecipitationSync(Context context) throws Exception {
        Context app = context.getApplicationContext();
        if (!WeatherApiConfig.hasApiKey()) {
            throw new Exception(app.getString(R.string.weather_no_api_key));
        }
        String zip = new ZipcodeStore(app).load().trim();
        if (zip.isEmpty()) {
            throw new Exception(app.getString(R.string.weather_no_zip));
        }

        WeatherApiService service = WeatherForecastClient.getRetrofit().create(WeatherApiService.class);
        Call<WeatherForecastResponse> call = service.getForecast(
                WeatherApiConfig.getApiKey(),
                zip,
                FORECAST_DAYS
        );

        Response<WeatherForecastResponse> response = call.execute();
        if (!response.isSuccessful() || response.body() == null) {
            throw new IOException(app.getString(R.string.weather_http_error));
        }
        return sumPrecipitationMm(response.body());
    }

    private static double sumPrecipitationMm(WeatherForecastResponse body) {
        double sum = 0;
        if (body.forecast == null || body.forecast.forecastday == null) {
            return sum;
        }
        for (WeatherForecastResponse.ForecastDay fd : body.forecast.forecastday) {
            if (fd != null && fd.day != null) {
                sum += fd.day.totalprecip_mm;
            }
        }
        return sum;
    }

    private static void postMain(Context app, Runnable r) {
        new Handler(Looper.getMainLooper()).post(r);
    }
}
