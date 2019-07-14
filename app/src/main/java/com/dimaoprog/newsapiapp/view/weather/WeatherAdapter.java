package com.dimaoprog.newsapiapp.view.weather;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.dimaoprog.newsapiapp.R;
import com.dimaoprog.newsapiapp.databinding.ItemForecastDayBinding;
import com.dimaoprog.newsapiapp.models.Forecastday;

public class WeatherAdapter extends ListAdapter<Forecastday, WeatherVH> {

    protected WeatherAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Forecastday> DIFF_CALLBACK = new DiffUtil.ItemCallback<Forecastday>() {
        @Override
        public boolean areItemsTheSame(@NonNull Forecastday oldItem, @NonNull Forecastday newItem) {
            return oldItem.getDateEpoch() == newItem.getDateEpoch();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Forecastday oldItem, @NonNull Forecastday newItem) {
            return oldItem.getDay().equals(newItem.getDay());
        }
    };

    @NonNull
    @Override
    public WeatherVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemForecastDayBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_forecast_day, parent, false);
        return new WeatherVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherVH holder, int position) {
        holder.onBind(getItem(position));
    }
}
