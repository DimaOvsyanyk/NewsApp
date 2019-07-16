package com.dimaoprog.newsapiapp.view.sources;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dimaoprog.newsapiapp.NewsApp;
import com.dimaoprog.newsapiapp.R;
import com.dimaoprog.newsapiapp.databinding.SourcesFragmentBinding;
import com.dimaoprog.newsapiapp.models.Source;
import com.dimaoprog.newsapiapp.utils.ViewModelFactory;

import javax.inject.Inject;

public class SourcesFragment extends Fragment implements SourcesAdapter.IAddRemoveSourceListener {

    @Inject
    ViewModelFactory vmFactory;

    private SourcesViewModel sViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        NewsApp.getApp().getAppComponent().inject(this);
        sViewModel = ViewModelProviders.of(this, vmFactory).get(SourcesViewModel.class);
        SourcesFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.sources_fragment, container, false);
        binding.setSourceModel(sViewModel);
        SourcesAdapter adapter = new SourcesAdapter(this);
        binding.rvSources.setAdapter(adapter);

        sViewModel.getSources().observe(getViewLifecycleOwner(), adapter::submitList);

        return binding.getRoot();
    }

    @Override
    public void changeIsSourceSelected(Source source) {
        sViewModel.makeChangesInSources(source);
    }
}
