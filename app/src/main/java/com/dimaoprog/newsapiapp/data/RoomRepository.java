package com.dimaoprog.newsapiapp.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dimaoprog.newsapiapp.models.Source;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

import static com.dimaoprog.newsapiapp.models.Source.SELECTED;

public class RoomRepository {

    private static SourceDao sourceDao;
    private Executor executor = Executors.newFixedThreadPool(5);

    @Inject
    public RoomRepository(AppDatabase database) {
        sourceDao = database.sourceDao();
    }

    public LiveData<List<Source>> getSourceList() {
        return sourceDao.getAllSources();
    }

    public Single<List<Source>> getSelectedSources() {
        return sourceDao.getSelectedSources(SELECTED);
    }

    public Single<String> getSelectedSourcesString() {
        return sourceDao.getSelectedSources(SELECTED)
                .map(sourceList -> {
                    StringBuilder sb = new StringBuilder();
                    int i = 0;
                    for (Source source : sourceList) {
                        if (i > 0) {
                            sb.append(",");
                        }
                        sb.append(source.getId());
                        i++;
                    }
                    return sb.toString();
                });
    }

    public void insert(List<Source> sourceList) {
        executor.execute(() -> sourceDao.insert(sourceList));
    }

    public Observable<Integer> update(Source source) {
        return Observable.fromCallable(() -> sourceDao.update(source));
    }

    public void delete(Source source) {
        executor.execute(() -> sourceDao.delete(source));
    }

    public void clearSources() {
        executor.execute(() -> sourceDao.clearSources());
    }
}
