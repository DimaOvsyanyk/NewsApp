package com.dimaoprog.newsapiapp.view.weather;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dimaoprog.newsapiapp.data.NetworkRepository;
import com.dimaoprog.newsapiapp.data.PrefsRepository;
import com.dimaoprog.newsapiapp.models.CurrentWeather;
import com.dimaoprog.newsapiapp.models.Forecastday;
import com.dimaoprog.newsapiapp.models.Location;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class WeatherViewModel extends ViewModel {

    private PrefsRepository prefsRepository;
    private NetworkRepository netRepository;
    private ObservableBoolean processing = new ObservableBoolean();
    private CompositeDisposable disposable = new CompositeDisposable();
    private ObservableField<CurrentWeather> currentWeather = new ObservableField<>();
    private ObservableField<Location> location = new ObservableField<>();
    private MutableLiveData<List<Forecastday>> forecastList = new MutableLiveData<>();

    @Inject
    public WeatherViewModel(PrefsRepository prefsRepository, NetworkRepository netRepository) {
        this.prefsRepository = prefsRepository;
        this.netRepository = netRepository;
        loadWeather();
    }

    private void loadWeather() {
        setProcessing(true);
        disposable.add(netRepository.getWeather(getCorrectCity(), 10)
                .map(Response::body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weatherResponse -> {
                            setCurrentWeather(weatherResponse.getCurrentWeather());
                            setLocation(weatherResponse.getLocation());
                            setForecastList(weatherResponse.getForecast().getForecastday());
                            setProcessing(false);
                        },
                        onError -> {
                            Log.d("api response", onError.getMessage());
                            setProcessing(false);
                        }
                ));
    }

    private String getCorrectCity() {
        String userCity = prefsRepository.getUserCity();
        switch (userCity) {
            case "Kyiv":
                return "kiev";
            case "Dnipro":
                return "dnipropetrovsk";
            case "Krakow":
                return "krakov";
            default:
                return userCity;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public ObservableBoolean getProcessing() {
        return processing;
    }

    public void setProcessing(boolean processing) {
        this.processing.set(processing);
    }

    public ObservableField<CurrentWeather> getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather.set(currentWeather);
    }

    public MutableLiveData<List<Forecastday>> getForecastList() {
        return forecastList;
    }

    public void setForecastList(List<Forecastday> forecastList) {
        this.forecastList.setValue(forecastList);
    }

    public ObservableField<Location> getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location.set(location);
    }
}
