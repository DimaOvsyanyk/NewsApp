package com.dimaoprog.newsapiapp.data;

import com.dimaoprog.newsapiapp.models.NewsResponse;
import com.dimaoprog.newsapiapp.models.SourcesResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import static com.dimaoprog.newsapiapp.utils.Constants.API_KEY_NEWS;

public interface INewsApi {

    @Headers(API_KEY_NEWS)
    @GET("everything")
    Single<Response<NewsResponse>> getNews(@Query("sources") String sources,
                                           @Query("page") int page);

    @Headers(API_KEY_NEWS)
    @GET("sources?language=en")
    Single<Response<SourcesResponse>> getSources();

}
