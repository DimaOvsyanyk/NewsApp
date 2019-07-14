package com.dimaoprog.newsapiapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dimaoprog.newsapiapp.models.Source;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface SourceDao {


    @Query("SELECT * FROM sources ORDER BY is_selected DESC")
    Flowable<List<Source>> getAllSources();

    @Query("SELECT * FROM sources WHERE is_selected = :isSelected")
    Single<List<Source>> getSelectedSources(int isSelected);

    @Insert
    void insert(List<Source> sourceList);

    @Update
    void update(Source source);

    @Delete
    void delete(Source source);

}
