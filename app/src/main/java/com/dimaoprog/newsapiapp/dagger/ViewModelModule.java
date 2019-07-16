package com.dimaoprog.newsapiapp.dagger;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.dimaoprog.newsapiapp.MainViewModel;
import com.dimaoprog.newsapiapp.utils.ViewModelFactory;
import com.dimaoprog.newsapiapp.view.loginRegistration.LoginViewModel;
import com.dimaoprog.newsapiapp.view.loginRegistration.RegistrationViewModel;
import com.dimaoprog.newsapiapp.view.news.NewsViewModel;
import com.dimaoprog.newsapiapp.view.profile.ProfileViewModel;
import com.dimaoprog.newsapiapp.view.sources.SourcesViewModel;
import com.dimaoprog.newsapiapp.view.weather.WeatherViewModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.Binds;
import dagger.MapKey;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel loginViewModel(LoginViewModel loginViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel.class)
    abstract ViewModel registrationViewModel(RegistrationViewModel registrationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel.class)
    abstract ViewModel newsViewModel(NewsViewModel newsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    abstract ViewModel profileViewModel(ProfileViewModel profileViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SourcesViewModel.class)
    abstract ViewModel sourcesViewModel(SourcesViewModel sourcesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(WeatherViewModel.class)
    abstract ViewModel weatherViewModel(WeatherViewModel weatherViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel mainViewModel(MainViewModel mainViewModel);

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    @interface ViewModelKey {
        Class<? extends ViewModel> value();
    }
}
