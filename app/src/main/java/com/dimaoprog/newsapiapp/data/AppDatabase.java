package com.dimaoprog.newsapiapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dimaoprog.newsapiapp.models.Source;

@Database(entities = {Source.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract SourceDao sourceDao();

    public synchronized static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "NewsAppDatabase").build();
        }
        return instance;
    }
}
