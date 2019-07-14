package com.dimaoprog.newsapiapp.view.news;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.dimaoprog.newsapiapp.R;
import com.dimaoprog.newsapiapp.databinding.ItemArticleBinding;
import com.dimaoprog.newsapiapp.models.Article;
import com.dimaoprog.newsapiapp.utils.ChangeFragmentsListener;

public class NewsAdapter extends ListAdapter<Article, NewsVH> {

    private ChangeFragmentsListener changeFrListener;

    protected NewsAdapter(ChangeFragmentsListener changeFrListener) {
        super(DIFF_CALLBACK);
        this.changeFrListener = changeFrListener;
    }

    private static final DiffUtil.ItemCallback<Article> DIFF_CALLBACK = new DiffUtil.ItemCallback<Article>() {
        @Override
        public boolean areItemsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public NewsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArticleBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_article, parent, false);
        return new NewsVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsVH holder, int position) {
        holder.onBind(getItem(position), changeFrListener);
    }
}
