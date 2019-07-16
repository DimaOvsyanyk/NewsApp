package com.dimaoprog.newsapiapp.data;

import com.dimaoprog.newsapiapp.models.NewsResponse;
import com.dimaoprog.newsapiapp.models.SourcesResponse;
import com.dimaoprog.newsapiapp.models.WeatherResponse;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.Response;

import static com.dimaoprog.newsapiapp.utils.Constants.API_KEY_WEATHER;

public class NetworkRepository {

    private INewsApi newsApi;
    private IWeatherApi weatherApi;

    @Inject
    public NetworkRepository(INewsApi newsApi, IWeatherApi weatherApi) {
        this.newsApi = newsApi;
        this.weatherApi = weatherApi;
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
