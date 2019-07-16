package com.dimaoprog.newsapiapp.view.profile;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.dimaoprog.newsapiapp.NewsApp;
import com.dimaoprog.newsapiapp.R;
import com.dimaoprog.newsapiapp.databinding.ProfileFragmentBinding;
import com.dimaoprog.newsapiapp.utils.ViewModelFactory;

import javax.inject.Inject;

public class ProfileFragment extends Fragment {

    @Inject
    ViewModelFactory vmFactory;

    private ProfileViewModel pViewModel;
    private ProfileFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        NewsApp.getApp().getAppComponent().inject(this);
        pViewModel = ViewModelProviders.of(this, vmFactory).get(ProfileViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false);
        binding.setProfileModel(pViewModel);
        observeLiveData();


        return binding.getRoot();
    }

    private void observeLiveData() {
        pViewModel.getNewPassOk().observe(getViewLifecycleOwner(), ok -> binding.etNewPassword.setError(ok ? null : "invalid password"));
        pViewModel.getOldPassOk().observe(getViewLifecycleOwner(), ok -> binding.etOldPassword.setError(ok ? null : "incorrect password"));
        pViewModel.getFirstNameOk().observe(getViewLifecycleOwner(), ok -> binding.etFirstName.setError(ok ? null : "very short name"));
        pViewModel.getSecondNameOk().observe(getViewLifecycleOwner(), ok -> binding.etSecondName.setError(ok ? null : "very short second name"));
        pViewModel.getTelNumberOk().observe(getViewLifecycleOwner(), ok -> binding.etTelNumber.setError(ok ? null : "use format +31234567890"));
        pViewModel.getToastMessage().observe(getViewLifecycleOwner(), text -> Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show());
        pViewModel.getChangesSaved().observe(getViewLifecycleOwner(), saved -> {
            if (saved) {
                Toast.makeText(getContext(), "Changes saved", Toast.LENGTH_SHORT).show();
            }
        });
        pViewModel.getFirstDataLoaded().observe(getViewLifecycleOwner(), loaded -> {
            if (loaded) {
                binding.invalidateAll();
                setUpCountrySpinner();
            }
        });
    }

    private void setUpCountrySpinner() {
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.countries, android.R.layout.simple_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.countrySpinner.setAdapter(countryAdapter);
        binding.countrySpinner.setSelection(countryAdapter.getPosition(pViewModel.getCountry()));
        binding.countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setUpCitySpinner(position);
                pViewModel.setCountry(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpCitySpinner(int countryIndex) {
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(getContext(),
                getCityArrayResource(countryIndex), android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.citySpinner.setAdapter(cityAdapter);
        binding.citySpinner.setSelection(cityAdapter.getPosition(pViewModel.getCity()));
        binding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pViewModel.setCity(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private int getCityArrayResource(int countryIndex) {
        switch (countryIndex) {
            case 0:
                return R.array.Ukraine;
            case 1:
                return R.array.Poland;
            case 2:
                return R.array.Germany;
            default:
                return 0;
        }
    }


}
