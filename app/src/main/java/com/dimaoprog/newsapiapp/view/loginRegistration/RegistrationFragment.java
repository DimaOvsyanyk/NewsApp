package com.dimaoprog.newsapiapp.view.loginRegistration;

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

import com.dimaoprog.newsapiapp.R;
import com.dimaoprog.newsapiapp.databinding.RegistrationFragmentBinding;

import java.util.Objects;

public class RegistrationFragment extends Fragment {

    private RegistrationViewModel rViewModel;
    private RegistrationFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rViewModel = ViewModelProviders.of(this).get(RegistrationViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.registration_fragment, container, false);
        binding.setRegistrationModel(rViewModel);
        setUpCountrySpinner();
        observeMessages();
        return binding.getRoot();
    }

    private void observeMessages() {
        rViewModel.getEmailOk().observe(getViewLifecycleOwner(), ok -> binding.etEMail.setError(ok ? null : "invalid e-mail"));
        rViewModel.getPassOk().observe(getViewLifecycleOwner(), ok -> binding.etPassword.setError(ok ? null : "invalid password"));
        rViewModel.getPassMatchOk().observe(getViewLifecycleOwner(), ok -> binding.etPasswordCheck.setError(ok ? null : "password not identical"));
        rViewModel.getFirstNameOk().observe(getViewLifecycleOwner(), ok -> binding.etFirstName.setError(ok ? null : "very short name"));
        rViewModel.getSecondNameOk().observe(getViewLifecycleOwner(), ok -> binding.etSecondName.setError(ok ? null : "very short second name"));
        rViewModel.getTelNumberOk().observe(getViewLifecycleOwner(), ok -> binding.etTelNumber.setError(ok ? null : "use format +31234567890"));
        rViewModel.getBirthDayOk().observe(getViewLifecycleOwner(), ok -> binding.etBirthday.setError(ok ? null : "use format dd.mm.yyyy"));
        rViewModel.getToastMessage().observe(getViewLifecycleOwner(), text -> Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show());
        rViewModel.getRegistrationCompleted().observe(getViewLifecycleOwner(), completed -> {
            if (completed) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });
    }

    private void setUpCountrySpinner() {
        ArrayAdapter<?> countryAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.countries, android.R.layout.simple_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.countrySpinner.setAdapter(countryAdapter);
        binding.countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setUpCitySpinner(position);
                rViewModel.setCountry(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpCitySpinner(int countryIndex) {
        ArrayAdapter<?> cityAdapter = ArrayAdapter.createFromResource(getContext(),
                getCityArrayResource(countryIndex), android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.citySpinner.setAdapter(cityAdapter);
        binding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rViewModel.setCity(parent.getItemAtPosition(position).toString());
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
