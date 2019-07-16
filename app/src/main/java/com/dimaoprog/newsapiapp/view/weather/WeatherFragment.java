package com.dimaoprog.newsapiapp.view.weather;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dimaoprog.newsapiapp.NewsApp;
import com.dimaoprog.newsapiapp.R;
import com.dimaoprog.newsapiapp.databinding.WeatherFragmentBinding;
import com.dimaoprog.newsapiapp.utils.ViewModelFactory;

import javax.inject.Inject;

public class WeatherFragment extends Fragment {

    @Inject
    ViewModelFactory vmFactory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        NewsApp.getApp().getAppComponent().inject(this);
        WeatherViewModel wViewModel = ViewModelProviders.of(this, vmFactory).get(WeatherViewModel.class);
        WeatherFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.weather_fragment, container, false);
        binding.setWeatherModel(wViewModel);
        WeatherAdapter adapter = new WeatherAdapter();
        binding.rvForecast.setAdapter(adapter);
        wViewModel.getForecastList().observe(getViewLifecycleOwner(), adapter::submitList);
        return binding.getRoot();
    }
}
