package com.dimaoprog.newsapiapp.view.sources;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.dimaoprog.newsapiapp.R;
import com.dimaoprog.newsapiapp.databinding.ItemSourceBinding;
import com.dimaoprog.newsapiapp.models.Source;

public class SourcesAdapter extends ListAdapter<Source, SourcesVH> {

    private IAddRemoveSourceListener addRemoveSourceListener;

    public interface IAddRemoveSourceListener {
        void changeIsSourceSelected(Source source);
    }

    protected SourcesAdapter(IAddRemoveSourceListener addRemoveSourceListener) {
        super(DIFF_CALLBACK);
        this.addRemoveSourceListener = addRemoveSourceListener;
    }

    private static final DiffUtil.ItemCallback<Source> DIFF_CALLBACK = new DiffUtil.ItemCallback<Source>() {
        @Override
        public boolean areItemsTheSame(@NonNull Source oldItem, @NonNull Source newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Source oldItem, @NonNull Source newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public SourcesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSourceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_source, parent, false);
        return new SourcesVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SourcesVH holder, int position) {
        holder.onBind(getItem(position), addRemoveSourceListener);
    }
}
