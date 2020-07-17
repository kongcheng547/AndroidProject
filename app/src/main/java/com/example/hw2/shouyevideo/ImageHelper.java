package com.example.hw2.shouyevideo;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageHelper {
    public static void displayWebImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }
}
