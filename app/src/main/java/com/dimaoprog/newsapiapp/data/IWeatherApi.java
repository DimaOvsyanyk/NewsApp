package com.dimaoprog.newsapiapp.data;

import com.dimaoprog.newsapiapp.models.WeatherResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IWeatherApi {

    @GET("forecast.json")
    Single<Response<WeatherResponse>> getWeather(@Query("q") String city,
                                                 @Query("days") int days,
                                                 @Query("key") String apiKey);

}
