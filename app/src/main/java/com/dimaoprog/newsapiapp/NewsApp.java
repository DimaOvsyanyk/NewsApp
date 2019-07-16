package com.dimaoprog.newsapiapp;

import android.app.Application;

import com.dimaoprog.newsapiapp.dagger.AppComponent;
import com.dimaoprog.newsapiapp.dagger.DaggerAppComponent;
import com.dimaoprog.newsapiapp.dagger.DataModule;
import com.dimaoprog.newsapiapp.dagger.RetrofitModule;

public class NewsApp extends Application {

    private static NewsApp app;
    private AppComponent appComponent;

    public static NewsApp getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        appComponent = DaggerAppComponent.builder()
                .retrofitModule(new RetrofitModule())
                .dataModule(new DataModule(this))
                .build();
        appComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
