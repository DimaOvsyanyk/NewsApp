package com.dimaoprog.newsapiapp.view.sources;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dimaoprog.newsapiapp.dagger.AppComponent;
import com.dimaoprog.newsapiapp.dagger.DaggerAppComponent;
import com.dimaoprog.newsapiapp.data.RoomRepository;
import com.dimaoprog.newsapiapp.models.Source;
import com.dimaoprog.newsapiapp.models.SourcesResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class SourcesViewModel extends AndroidViewModel {

    private ObservableBoolean processing = new ObservableBoolean();
    private CompositeDisposable disposable = new CompositeDisposable();

    private MutableLiveData<List<Source>> sources;

    public SourcesViewModel(@NonNull Application application) {
        super(application);
        //    private AppComponent component;
        RoomRepository roomRepository = RoomRepository.getInstance(application);
        sources = roomRepository.getSourceList();

//        component = DaggerAppComponent.create();
//        loadSources();
    }

//    private void loadSources() {
//        disposable.add(component.getNewsApi().getSources()
//                .map(Response::body)
//                .map(SourcesResponse::getSources)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(sources1 -> {
//                    setSources(sources1);
//                            Log.d("api response", " " + sources1.size());
//                        },
//                        onError -> Log.d("api response", onError.getMessage())
//                ));
//    }

//    @Override
//    protected void onCleared() {
//        super.onCleared();
//        disposable.clear();
//    }

    public MutableLiveData<List<Source>> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources.setValue(sources);
    }

    public ObservableBoolean getProcessing() {
        return processing;
    }

    public void setProcessing(boolean processing) {
        this.processing.set(processing);
    }
}
