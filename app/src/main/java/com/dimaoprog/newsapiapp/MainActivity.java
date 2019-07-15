package com.dimaoprog.newsapiapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
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
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ChangeFragmentsListener {

    private MainViewModel mainViewModel;
    private FirebaseAuth auth;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

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

    @Override
    public void openFragment(Fragment fragment, boolean addToBackStack, boolean clearBackStack) {
        checkDrawerLockMode();
        if (clearBackStack) {
            clearBackStack();
        }
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
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
            openFragment(NewsFragment.newInstance(this), false, true);
        } else if (id == R.id.nav_sources) {
            openFragment(new SourcesFragment(), false, true);
        } else if (id == R.id.nav_weather) {
            openFragment(new WeatherFragment(), false, true);
        } else if (id == R.id.nav_profile) {
            openFragment(new ProfileFragment(), false, true);
        } else if (id == R.id.nav_exit) {
            auth.signOut();
            openFragment(LoginFragment.newInstance(this), false, true);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
