package com.dimaoprog.newsapiapp.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

public class PrefsRepository {

    private static final String APP_PREFS = "appPrefs";
    private static final String FIRST_TIME_LOAD = "firstTimeLoad";
    private static final String SOURCE_LAST_UPDATE_DATE = "sourceLastUpdateDate";
    private static final String SELECTED_SOURCE_LIST = "selectedSourceList";
    private static final String USER_CITY = "userCity";

    private SharedPreferences sharedPrefs;

    @Inject
    public PrefsRepository(Application application) {
        sharedPrefs = application.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
    }

    public boolean needFirstTimeLoading() {
        return !sharedPrefs.contains(FIRST_TIME_LOAD);
    }

    public void firstTimeLoaded() {
        sharedPrefs.edit().putBoolean(FIRST_TIME_LOAD, true).apply();
    }

    public void setNewTimeSourceUpdate() {
        sharedPrefs.edit().putLong(SOURCE_LAST_UPDATE_DATE, System.currentTimeMillis()).apply();
    }

    public long getLastTimeSourceUpdate() {
        return sharedPrefs.getLong(SOURCE_LAST_UPDATE_DATE, 0);
    }

    public void setSelectedSourceList(String selectedSourceList) {
        sharedPrefs.edit().putString(SELECTED_SOURCE_LIST, selectedSourceList).apply();
    }

    public String getSelectedSourceList() {
        return sharedPrefs.getString(SELECTED_SOURCE_LIST, "");
    }

    public void setUserCity(String city) {
        sharedPrefs.edit().putString(USER_CITY, city).apply();
    }

    public String getUserCity() {
        return sharedPrefs.getString(USER_CITY, "");
    }

}
