package com.cxsplay.imageselect.util;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by chuxiaoshan on 16/12/13.
 * CustomBinder
 */

public class CustomBinder {

    @BindingAdapter({"imageUrl", "placeHolder", "errorDrawable"})
    public static void loadImage(ImageView iv, String url, Drawable holder, Drawable error) {
        Glide.with(iv.getContext())
                .load(url)
                .crossFade()
                .error(error)
                .centerCrop()
                .placeholder(holder)
                .into(iv);
    }
}
