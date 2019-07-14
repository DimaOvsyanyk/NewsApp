package com.dimaoprog.newsapiapp.view.weather;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dimaoprog.newsapiapp.databinding.ItemForecastDayBinding;
import com.dimaoprog.newsapiapp.models.Forecastday;

public class WeatherVH extends RecyclerView.ViewHolder {

    private ItemForecastDayBinding binding;

    public WeatherVH(@NonNull ItemForecastDayBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void onBind(Forecastday forecastday) {
        binding.setForecastDay(forecastday);
    }
}
