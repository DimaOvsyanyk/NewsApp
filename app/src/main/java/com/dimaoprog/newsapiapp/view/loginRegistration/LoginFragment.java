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
import android.widget.Toast;

import com.dimaoprog.newsapiapp.R;
import com.dimaoprog.newsapiapp.databinding.LoginFragmentBinding;
import com.dimaoprog.newsapiapp.utils.ChangeFragmentsListener;
import com.dimaoprog.newsapiapp.view.news.NewsFragment;

public class LoginFragment extends Fragment {

    private LoginViewModel lViewModel;
    private LoginFragmentBinding binding;

    private ChangeFragmentsListener changeFrListener;

    public void setChangeFrListener(ChangeFragmentsListener changeFragmentsListener) {
        this.changeFrListener = changeFragmentsListener;
    }

    public static LoginFragment newInstance(ChangeFragmentsListener changeFrListener) {
        LoginFragment fragment = new LoginFragment();
        fragment.setChangeFrListener(changeFrListener);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        lViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false);
        binding.setLoginModel(lViewModel);
        observeLiveData();
        binding.btnRegistration.setOnClickListener(__ -> changeFrListener.openFragment(new RegistrationFragment(), true, false));
        return binding.getRoot();
    }

    private void observeLiveData() {
        lViewModel.getToastMessage().observe(getViewLifecycleOwner(), text -> {
            if (text != null) {
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                lViewModel.setToastMessage(null);
            }
        });
        lViewModel.getEmailOk().observe(getViewLifecycleOwner(), ok -> binding.etEMail.setError(ok ? null : getString(R.string.invalid_email)));
        lViewModel.getPasswordOk().observe(getViewLifecycleOwner(), ok -> binding.etPassword.setError(ok ? null : getString(R.string.invalid_pass)));
        lViewModel.getSignInCompleted().observe(getViewLifecycleOwner(), completed -> {
            if (completed) {
                changeFrListener.openFragment(NewsFragment.newInstance(changeFrListener), true, true);
                lViewModel.setSignInCompleted(false);
            }
        });
    }
}
