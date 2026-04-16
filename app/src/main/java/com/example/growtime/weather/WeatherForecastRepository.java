package com.example.growtime.weather;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.growtime.R;
import com.example.growtime.json_accessing.ZipcodeStore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Context app = context.getApplicationContext();
        if (!WeatherApiConfig.hasApiKey()) {
            postMain(app, () -> listener.onError(app.getString(R.string.weather_no_api_key)));
            return;
        }
        String zip = new ZipcodeStore(app).load().trim();
        if (zip.isEmpty()) {
            postMain(app, () -> listener.onError(app.getString(R.string.weather_no_zip)));
            return;
        }

        WeatherApiService service = WeatherForecastClient.getRetrofit().create(WeatherApiService.class);
        Call<WeatherForecastResponse> call = service.getForecast(
                WeatherApiConfig.getApiKey(),
                zip,
                FORECAST_DAYS
        );

        call.enqueue(new Callback<WeatherForecastResponse>() {
            @Override
            public void onResponse(Call<WeatherForecastResponse> call, Response<WeatherForecastResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    postMain(app, () -> listener.onError(app.getString(R.string.weather_http_error)));
                    return;
                }
                double totalMm = sumPrecipitationMm(response.body());
                postMain(app, () -> listener.onSuccess(totalMm));
            }

            @Override
            public void onFailure(Call<WeatherForecastResponse> call, Throwable t) {
                String msg = t.getMessage() != null ? t.getMessage() : app.getString(R.string.weather_http_error);
                postMain(app, () -> listener.onError(msg));
            }
        });
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
