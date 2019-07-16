package com.dimaoprog.newsapiapp.dagger;

import com.dimaoprog.newsapiapp.MainActivity;
import com.dimaoprog.newsapiapp.NewsApp;
import com.dimaoprog.newsapiapp.data.INewsApi;
import com.dimaoprog.newsapiapp.data.IWeatherApi;
import com.dimaoprog.newsapiapp.data.NetworkRepository;
import com.dimaoprog.newsapiapp.data.PrefsRepository;
import com.dimaoprog.newsapiapp.data.RoomRepository;
import com.dimaoprog.newsapiapp.view.loginRegistration.LoginFragment;
import com.dimaoprog.newsapiapp.view.loginRegistration.RegistrationFragment;
import com.dimaoprog.newsapiapp.view.news.NewsFragment;
import com.dimaoprog.newsapiapp.view.profile.ProfileFragment;
import com.dimaoprog.newsapiapp.view.sources.SourcesFragment;
import com.dimaoprog.newsapiapp.view.weather.WeatherFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RetrofitModule.class, DataModule.class, ViewModelModule.class})
public interface AppComponent {

    void inject(NewsApp newsApp);

    void inject(NetworkRepository networkRepository);

    void inject(PrefsRepository prefsRepository);

    void inject(RoomRepository roomRepository);

    void inject(MainActivity mainActivity);

    void inject(LoginFragment loginFragment);

    void inject(RegistrationFragment registrationFragment);

    void inject(NewsFragment newsFragment);

    void inject(ProfileFragment profileFragment);

    void inject(SourcesFragment sourcesFragment);

    void inject(WeatherFragment weatherFragment);
}