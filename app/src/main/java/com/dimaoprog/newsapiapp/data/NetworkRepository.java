package com.dimaoprog.newsapiapp.data;

import com.dimaoprog.newsapiapp.dagger.DaggerAppComponent;
import com.dimaoprog.newsapiapp.models.NewsResponse;
import com.dimaoprog.newsapiapp.models.SourcesResponse;
import com.dimaoprog.newsapiapp.models.WeatherResponse;

import io.reactivex.Single;
import retrofit2.Response;

import static com.dimaoprog.newsapiapp.utils.Constants.API_KEY_WEATHER;

public class NetworkRepository {

    private static INewsApi newsApi;
    private static IWeatherApi weatherApi;
    private static NetworkRepository instance;

    public static NetworkRepository getInstance() {
        if (instance == null) {
            instance = new NetworkRepository();
            newsApi = DaggerAppComponent.create().getNewsApi();
            weatherApi = DaggerAppComponent.create().getWeatherApi();
        }
        return instance;
    }

    public Single<Response<WeatherResponse>> getWeather(String city, int days) {
        return weatherApi.getWeather(city, days, API_KEY_WEATHER);
    }

    public Single<Response<NewsResponse>> getNews(String sources, int page) {
        return newsApi.getNews(sources, page);
    }


    public Single<Response<SourcesResponse>> getSources() {
        return newsApi.getSources();
    }

}
