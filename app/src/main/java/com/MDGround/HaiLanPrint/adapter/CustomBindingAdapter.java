package com.MDGround.HaiLanPrint.adapter;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.models.CloudImage;
import com.bumptech.glide.Glide;

/**
 * Created by yoghourt on 5/12/16.
 */
public class CustomBindingAdapter {

    @BindingAdapter("bind:loadImageByCloudImage")
    public static void loadImageByCloudImage(ImageView imageView, CloudImage cloudImage) {

        Glide.with(MDGroundApplication.mInstance)
                .load(cloudImage)
                .into(imageView); // 加载图片
    }
}

