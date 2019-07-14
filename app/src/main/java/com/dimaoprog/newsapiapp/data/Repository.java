package com.dimaoprog.newsapiapp.data;

import com.dimaoprog.newsapiapp.dagger.AppComponent;
import com.dimaoprog.newsapiapp.dagger.DaggerAppComponent;

public class Repository {

    private static INewsApi newsApi;
    private static Repository repository;

    public static Repository getRepository() {
        if (newsApi == null) {
            newsApi = DaggerAppComponent.create().getNewsApi();
        }
        if (repository == null) {
            repository = new Repository();
        }
        return repository;
    }
}
