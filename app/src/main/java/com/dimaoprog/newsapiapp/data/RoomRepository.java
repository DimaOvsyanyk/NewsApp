package com.dimaoprog.newsapiapp.data;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.dimaoprog.newsapiapp.models.Source;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.dimaoprog.newsapiapp.models.Source.SELECTED;

public class RoomRepository {

    private static SourceDao sourceDao;
    private MutableLiveData<List<Source>> sourceList = new MutableLiveData<>();
    private Executor executor = Executors.newFixedThreadPool(5);

    private static RoomRepository instance;

    public static RoomRepository getInstance(Application application) {
        if (instance == null) {
            instance = new RoomRepository();
            AppDatabase database = AppDatabase.getInstance(application);
            sourceDao = database.sourceDao();
        }
        return instance;
    }

    public MutableLiveData<List<Source>> getSourceList() {
        sourceDao.getAllSources()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setSourceList);
        return sourceList;
    }

    public Single<String> getSelectedSources() {
        return sourceDao.getSelectedSources(SELECTED)
                .map(sourceList -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append(sourceList.get(0).getId());
                    if (sourceList.size() > 1) {
                        for (int i = 1; i < sourceList.size(); i++) {
                            sb.append(",");
                            sb.append(sourceList.get(i));
                        }
                    }
                    return sb.toString();
                });
    }

    public void setSourceList(List<Source> sourceList) {
        this.sourceList.setValue(sourceList);
    }

    public void insert(List<Source> sourceList) {
        executor.execute(() -> sourceDao.insert(sourceList));
    }

    public void update(Source source) {
        executor.execute(() -> sourceDao.update(source));
    }

    public void delete(Source source) {
        executor.execute(() -> sourceDao.delete(source));
    }

}
