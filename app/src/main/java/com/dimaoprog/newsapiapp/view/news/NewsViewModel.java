package com.dimaoprog.newsapiapp.view.news;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dimaoprog.newsapiapp.data.NetworkRepository;
import com.dimaoprog.newsapiapp.data.PrefsRepository;
import com.dimaoprog.newsapiapp.models.Article;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.dimaoprog.newsapiapp.utils.Constants.RESPONSE_ERROR;
import static com.dimaoprog.newsapiapp.utils.Constants.RESPONSE_OK;
import static com.dimaoprog.newsapiapp.utils.Constants.TRIAL_LIMIT;

public class NewsViewModel extends AndroidViewModel {

    private PrefsRepository prefsRepository;
    private NetworkRepository netRepository;
    private ObservableBoolean processing = new ObservableBoolean();
    private CompositeDisposable disposable = new CompositeDisposable();

    private MutableLiveData<List<Article>> news = new MutableLiveData<>();

    private int page;
    private boolean firstLoading;
    private boolean hasNextPage;

    public NewsViewModel(@NonNull Application application) {
        super(application);
        netRepository = NetworkRepository.getInstance();
        prefsRepository = PrefsRepository.getInstance(application);
        loadFirstPage();
    }

    public void loadFirstPage() {
        firstLoading = true;
        page = 1;
        loadArticles();
    }

    public void loadNextPage() {
        if (hasNextPage & !processing.get()) {
            page++;
            loadArticles();
        }
    }

    private void loadArticles() {
        setProcessing(true);
        disposable.add(netRepository.getNews(prefsRepository.getSelectedSourceList(), page)
                .map(Response::body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsResponse -> {
                            if (newsResponse.getStatus().equals(RESPONSE_OK)) {
                                Log.d("api response", "ok");
                                if (firstLoading) {
                                    setNews(newsResponse.getArticles());
                                    firstLoading = false;
                                } else {
                                    addNewsToList(newsResponse.getArticles());
                                }
                                hasNextPage = (page < TRIAL_LIMIT && newsResponse.getTotalResults() > page);
                            } else if (newsResponse.getStatus().equals(RESPONSE_ERROR)) {
                                Log.d("api response", "error");
                            }
                            setProcessing(false);
                        },
                        onError -> {
                            Log.d("api response", onError.getMessage());
                            setProcessing(false);
                        }
                ));
    }

    private void addNewsToList(List<Article> newExerciseList) {
        List<Article> newsToAdd = news.getValue();
        newsToAdd.addAll(newExerciseList);
        setNews(newsToAdd);
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

    public MutableLiveData<List<Article>> getNews() {
        return news;
    }

    public void setNews(List<Article> news) {
        this.news.setValue(news);
    }
}
