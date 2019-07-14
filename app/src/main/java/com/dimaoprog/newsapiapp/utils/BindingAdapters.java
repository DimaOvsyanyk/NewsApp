package com.dimaoprog.newsapiapp.utils;

import android.graphics.drawable.Drawable;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dimaoprog.newsapiapp.R;

public class BindingAdapters {

    @BindingAdapter({"imageArticleUrl"})
    public static void loadArticleImage(ImageView view, String url) {
        Glide.with(view)
                .load(url)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                .placeholder(R.mipmap.ic_launcher)
                .into(view);
    }

    @BindingAdapter({"imageBackUrl", "errorBackImage"})
    public static void loadBackImage(ImageView view, String url, Drawable errorImage) {
        Glide.with(view)
                .load(url)
                .error(errorImage)
                .into(view);
    }

    @BindingAdapter({"imageAvatarUrlRound", "errorAvatarImageRound"})
    public static void loadAvatarImageRound(ImageView view, String url, Drawable errorImage) {
        Glide.with(view)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .error(errorImage)
                .into(view);
    }
}
