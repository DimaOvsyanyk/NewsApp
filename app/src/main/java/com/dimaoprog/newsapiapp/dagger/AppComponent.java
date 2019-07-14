package com.dimaoprog.newsapiapp.dagger;

import com.dimaoprog.newsapiapp.data.INewsApi;
import com.dimaoprog.newsapiapp.data.IWeatherApi;

import dagger.Component;

@Component(modules = RetrofitModule.class)
public interface AppComponent {

    INewsApi getNewsApi();

    IWeatherApi getWeatherApi();
}