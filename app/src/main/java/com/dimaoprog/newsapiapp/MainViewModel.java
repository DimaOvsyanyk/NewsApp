package com.dimaoprog.newsapiapp;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.dimaoprog.newsapiapp.data.NetworkRepository;
import com.dimaoprog.newsapiapp.data.PrefsRepository;
import com.dimaoprog.newsapiapp.data.RoomRepository;
import com.dimaoprog.newsapiapp.models.Source;
import com.dimaoprog.newsapiapp.models.SourcesResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.dimaoprog.newsapiapp.models.Source.SELECTED;
import static com.dimaoprog.newsapiapp.models.Source.UNSELECTED;
import static com.dimaoprog.newsapiapp.utils.Constants.DAYS_INTERVAL_SOURCES_UPDATE;

public class MainViewModel extends AndroidViewModel {

    private NetworkRepository netRepository;
    private PrefsRepository prefsRepository;
    private RoomRepository roomRepository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private List<Source> currentSourceList = new ArrayList<>();
    private boolean isFirstLoading;

    public MainViewModel(@NonNull Application application) {
        super(application);
        prefsRepository = PrefsRepository.getInstance(application);
        isFirstLoading = prefsRepository.needFirstTimeLoading();
        if (isFirstLoading || checkNeedRefreshSourceList()) {
            netRepository = NetworkRepository.getInstance();
            roomRepository = RoomRepository.getInstance(application);
            if (isFirstLoading) {
                getNewSourceList();
            }else {
                refreshSourceList();
            }
        }
    }

    private boolean checkNeedRefreshSourceList() {
        return ((System.currentTimeMillis() - prefsRepository.getLastTimeSourceUpdate()) /
                (24 * 60 * 60 * 1000) > DAYS_INTERVAL_SOURCES_UPDATE);
    }

    private void getNewSourceList() {
        disposable.add(netRepository.getSources()
                .map(Response::body)
                .map(SourcesResponse::getSources)
                .map(this::makeSourcesSelected)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::workWithLoadedSources,
                        onError -> Log.d("api response", onError.getMessage())
                ));
    }

    private void refreshSourceList() {
        disposable.add(roomRepository.getSelectedSources()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sourceList -> {
                            Log.d("room response", "selected " + sourceList.size());
                            currentSourceList = sourceList;
                            roomRepository.clearSources();
                            getNewSourceList();
                        },
                        onError -> Log.d("room response", onError.getMessage())
                ));
    }

    private List<Source> makeSourcesSelected(List<Source> newSourceList) {
        if (isFirstLoading) {
            for (int i = 0; i < newSourceList.size(); i++) {
                if (i < 4) {
                    newSourceList.get(i).setIsSelectedSource(SELECTED);
                } else {
                    newSourceList.get(i).setIsSelectedSource(UNSELECTED);
                }
            }
        } else {
            for (Source source : newSourceList) {
                if (isSourceInSelection(source.getId())) {
                    source.setIsSelectedSource(SELECTED);
                }
            }
        }
        return newSourceList;
    }

    private boolean isSourceInSelection(String id) {
        for (Source source : currentSourceList) {
            if (id.equals(source.getId())) {
                return true;
            }
        }
        return false;
    }

    private void workWithLoadedSources(List<Source> sourceList) {
        if (isFirstLoading) {
            prefsRepository.firstTimeLoaded();
            isFirstLoading = false;
        }
        roomRepository.insert(sourceList);
        prefsRepository.setNewTimeSourceUpdate();
        prefsRepository.setSelectedSourceList(convertListToString(sourceList));
    }

    private String convertListToString(List<Source> sourceList) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Source source : sourceList) {
            if (source.getIsSelectedSource() == SELECTED) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(source.getId());
                i++;
            }
        }
        Log.d("main", sb.toString() + " " + i);
        return sb.toString();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.clear();
        }
    }
}
