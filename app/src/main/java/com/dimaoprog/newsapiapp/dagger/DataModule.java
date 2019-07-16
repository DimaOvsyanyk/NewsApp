package com.dimaoprog.newsapiapp.dagger;

import android.app.Application;

import com.dimaoprog.newsapiapp.data.AppDatabase;
import com.dimaoprog.newsapiapp.data.INewsApi;
import com.dimaoprog.newsapiapp.data.IWeatherApi;
import com.dimaoprog.newsapiapp.data.NetworkRepository;
import com.dimaoprog.newsapiapp.data.PrefsRepository;
import com.dimaoprog.newsapiapp.data.RoomRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

    private Application app;

    public DataModule(Application app) {
        this.app = app;
    }

    @Provides
    public Application getApp() {
        return app;
    }

    @Provides
    public AppDatabase appDatabase() {
        return AppDatabase.getInstance(app);
    }

    @Provides
    public NetworkRepository getNetworkRepository(INewsApi newsApi, IWeatherApi weatherApi) {
        return new NetworkRepository(newsApi, weatherApi);
    }

    @Provides
    public PrefsRepository getPrefsRepository(Application application) {
        return new PrefsRepository(application);
    }

    @Provides
    public RoomRepository getRoomRepository(AppDatabase database) {
        return new RoomRepository(database);
    }
}
