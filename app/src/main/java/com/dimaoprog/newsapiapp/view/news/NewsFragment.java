package com.dimaoprog.newsapiapp.view.news;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dimaoprog.newsapiapp.NewsApp;
import com.dimaoprog.newsapiapp.R;
import com.dimaoprog.newsapiapp.databinding.NewsFragmentBinding;
import com.dimaoprog.newsapiapp.utils.ChangeFragmentsListener;
import com.dimaoprog.newsapiapp.utils.ViewModelFactory;

import javax.inject.Inject;

public class NewsFragment extends Fragment {

    @Inject
    ViewModelFactory vmFactory;

    private NewsViewModel nViewModel;
    private NewsFragmentBinding binding;
    private NewsAdapter adapter;

    private ChangeFragmentsListener changeFrListener;

    private void setChangeFrListener(ChangeFragmentsListener changeFragmentsListener) {
        this.changeFrListener = changeFragmentsListener;
    }

    public static NewsFragment newInstance(ChangeFragmentsListener changeFrListener) {
        NewsFragment fragment = new NewsFragment();
        fragment.setChangeFrListener(changeFrListener);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        NewsApp.getApp().getAppComponent().inject(this);
        nViewModel = ViewModelProviders.of(this, vmFactory).get(NewsViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.news_fragment, container, false);
        setUpRV();
        nViewModel.getNews().observe(getViewLifecycleOwner(), news -> {
            adapter.submitList(news);
            Log.d("api response", "size " + news.size());
            adapter.notifyItemInserted(binding.rvNews.getLayoutManager().getItemCount() + 1);
            binding.swipeRefresh.setRefreshing(false);
        });
        binding.swipeRefresh.setOnRefreshListener(() -> nViewModel.loadFirstPage());
        binding.fabUp.setOnClickListener(__ -> binding.rvNews.scrollToPosition(0));
        return binding.getRoot();
    }

    private void setUpRV(){
        adapter = new NewsAdapter(changeFrListener);
        binding.rvNews.setAdapter(adapter);
        binding.rvNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = llManager.getChildCount();
                int totalItemCount = llManager.getItemCount();
                int firstVisibleItemPositions = llManager.findFirstVisibleItemPosition();
                if ((visibleItemCount + firstVisibleItemPositions) >= (totalItemCount - 10) && firstVisibleItemPositions >= 0) {
                    nViewModel.loadNextPage();
                }
            }
        });
    }


}
