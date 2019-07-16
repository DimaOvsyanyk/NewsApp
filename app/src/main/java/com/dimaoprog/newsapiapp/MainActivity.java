package com.dimaoprog.newsapiapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;
import android.widget.TextView;

import com.dimaoprog.newsapiapp.databinding.ActivityMainBinding;
import com.dimaoprog.newsapiapp.databinding.NavHeaderMainBinding;
import com.dimaoprog.newsapiapp.utils.ChangeFragmentsListener;
import com.dimaoprog.newsapiapp.utils.ViewModelFactory;
import com.dimaoprog.newsapiapp.view.loginRegistration.LoginFragment;
import com.dimaoprog.newsapiapp.view.news.NewsFragment;
import com.dimaoprog.newsapiapp.view.profile.ProfileFragment;
import com.dimaoprog.newsapiapp.view.sources.SourcesFragment;
import com.dimaoprog.newsapiapp.view.weather.WeatherFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ChangeFragmentsListener {

    @Inject
    ViewModelFactory vmFactory;

    private MainViewModel mainViewModel;
    private FirebaseAuth auth;
    private ActivityMainBinding binding;
    private NavHeaderMainBinding bindingHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        bindingHeader = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0));
        NewsApp.getApp().getAppComponent().inject(this);
        mainViewModel = ViewModelProviders.of(this, vmFactory).get(MainViewModel.class);
        setUpNavigationViews();

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            openFragment(NewsFragment.newInstance(this), false, false);
        } else {
            openFragment(LoginFragment.newInstance(this), false, false);
        }

    }

    private void setUpNavigationViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);
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
        if (fragment.getClass().equals(NewsFragment.class)) {
            binding.navView.setCheckedItem(R.id.nav_news);
        }
    }

    public void clearBackStack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void checkDrawerLockMode() {
        if (auth.getCurrentUser() != null) {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            auth.updateCurrentUser(auth.getCurrentUser());
            bindingHeader.setUserName(auth.getCurrentUser().getDisplayName());
        } else {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
