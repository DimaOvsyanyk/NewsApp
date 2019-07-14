package com.dimaoprog.newsapiapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.util.Log;
import android.view.MenuItem;

import com.dimaoprog.newsapiapp.dagger.DaggerAppComponent;
import com.dimaoprog.newsapiapp.data.INewsApi;
import com.dimaoprog.newsapiapp.data.PrefsRepository;
import com.dimaoprog.newsapiapp.data.RoomRepository;
import com.dimaoprog.newsapiapp.models.Source;
import com.dimaoprog.newsapiapp.models.SourcesResponse;
import com.dimaoprog.newsapiapp.utils.ChangeFragmentsListener;
import com.dimaoprog.newsapiapp.view.loginRegistration.LoginFragment;
import com.dimaoprog.newsapiapp.view.news.NewsFragment;
import com.dimaoprog.newsapiapp.view.profile.ProfileFragment;
import com.dimaoprog.newsapiapp.view.sources.SourcesFragment;
import com.dimaoprog.newsapiapp.view.weather.WeatherFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.dimaoprog.newsapiapp.models.Source.SELECTED;
import static com.dimaoprog.newsapiapp.models.Source.UNSELECTED;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ChangeFragmentsListener {

    private FirebaseAuth auth;
    private DrawerLayout drawer;
    private PrefsRepository prefsRepository;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        prefsRepository = PrefsRepository.getInstance(getApplicationContext());
        if (prefsRepository.needFirstTimeLoading()) {
            loadSourcesFirstTime();
        }
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (savedInstanceState == null) {
            if (currentUser != null) {
                openFragment(NewsFragment.newInstance(this), false, false);
                navigationView.setCheckedItem(R.id.nav_news);
            } else {
                openFragment(LoginFragment.newInstance(this), false, false);
            }
        }
    }

    private void loadSourcesFirstTime() {
        INewsApi newsApi = DaggerAppComponent.create().getNewsApi();
        disposable.add(newsApi.getSources()
                .map(Response::body)
                .map(SourcesResponse::getSources)
                .map(this::makeSomeSourcesSelected)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::workWithFirstLoadedSources,
                        onError -> Log.d("api response", onError.getMessage())
                ));
    }

    private void workWithFirstLoadedSources(List<Source> sourceList) {
        RoomRepository roomRepository = RoomRepository.getInstance(getApplication());
        roomRepository.insert(sourceList);
        prefsRepository.firstTimeLoaded();
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

    private List<Source> makeSomeSourcesSelected(List<Source> sourceList) {
        for (int i = 0; i < sourceList.size(); i++) {
            if (i < 4) {
                sourceList.get(i).setIsSelectedSource(SELECTED);
            } else {
                sourceList.get(i).setIsSelectedSource(UNSELECTED);
            }
        }
        return sourceList;
    }

    @Override
    public void openFragment(Fragment fragment, boolean addToBackStack, boolean clearBackStack) {
        checkDrawerLockMode();
        if (clearBackStack) {
            clearBackStack();
        }
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fr_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void clearBackStack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void checkDrawerLockMode() {
        if (auth.getCurrentUser() != null) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_news) {
            openFragment(NewsFragment.newInstance(this), true, true);
        } else if (id == R.id.nav_sources) {
            openFragment(new SourcesFragment(), true, true);
        } else if (id == R.id.nav_weather) {
            openFragment(new WeatherFragment(), true, true);
        } else if (id == R.id.nav_profile) {
            openFragment(new ProfileFragment(), true, true);
        } else if (id == R.id.nav_exit) {
            auth.signOut();
            openFragment(LoginFragment.newInstance(this), false, true);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.clear();
        }
    }
}
