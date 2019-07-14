package com.dimaoprog.newsapiapp.view.newsDetail;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dimaoprog.newsapiapp.R;
import com.dimaoprog.newsapiapp.databinding.NewsDetailFragmentBinding;

public class NewsDetailFragment extends Fragment {

    private NewsDetailFragmentBinding binding;
    private static final String ARTICLE_URL = "article url";

    public static NewsDetailFragment newInstance(String articleUrl) {
        Bundle args = new Bundle();
        args.putString(ARTICLE_URL, articleUrl);
        NewsDetailFragment fragment = new NewsDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.news_detail_fragment, container, false);
        binding.webView.setWebViewClient(new NewsWebClient());
        binding.webView.loadUrl(getArguments().getString(ARTICLE_URL));
        binding.fabBack.setOnClickListener(__ -> webViewBack());
        return binding.getRoot();
    }

    private void webViewBack() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack();
        } else {
            getActivity().onBackPressed();
        }
    }


}
