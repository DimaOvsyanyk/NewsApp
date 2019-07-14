package com.dimaoprog.newsapiapp.view.news;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dimaoprog.newsapiapp.databinding.ItemArticleBinding;
import com.dimaoprog.newsapiapp.models.Article;
import com.dimaoprog.newsapiapp.utils.ChangeFragmentsListener;
import com.dimaoprog.newsapiapp.view.newsDetail.NewsDetailFragment;

public class NewsVH extends RecyclerView.ViewHolder {

    private ItemArticleBinding binding;

    public NewsVH(@NonNull ItemArticleBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void onBind(Article article, ChangeFragmentsListener changeFrListener) {
        binding.setArticle(article);
        itemView.setOnClickListener(__ -> changeFrListener.openFragment(NewsDetailFragment.newInstance(article.getUrl()), true, false));
    }
}
