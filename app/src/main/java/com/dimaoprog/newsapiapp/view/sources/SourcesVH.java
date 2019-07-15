package com.dimaoprog.newsapiapp.view.sources;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dimaoprog.newsapiapp.databinding.ItemSourceBinding;
import com.dimaoprog.newsapiapp.models.Source;

import static com.dimaoprog.newsapiapp.models.Source.SELECTED;
import static com.dimaoprog.newsapiapp.models.Source.UNSELECTED;

public class SourcesVH extends RecyclerView.ViewHolder {

    private ItemSourceBinding binding;

    public SourcesVH(@NonNull ItemSourceBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void onBind(Source source, SourcesAdapter.IAddRemoveSourceListener addRemoveSourceListener) {
        binding.setExpand(false);
        binding.setSource(source);
        itemView.setOnClickListener(__ -> binding.setExpand(!binding.getExpand()));
        binding.addRemove.setOnClickListener(__ -> {
            if (source.getIsSelectedSource() == SELECTED) {
                source.setIsSelectedSource(UNSELECTED);
            } else {
                source.setIsSelectedSource(SELECTED);
            }
            binding.invalidateAll();
            addRemoveSourceListener.changeIsSourceSelected(source);
            Log.d("view holder", source.getName() + " " + source.getIsSelectedSource());
        });
    }
}
