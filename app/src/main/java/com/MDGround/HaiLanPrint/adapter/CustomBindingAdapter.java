package com.MDGround.HaiLanPrint.adapter;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by yoghourt on 5/12/16.
 */
public class CustomBindingAdapter {

    @BindingAdapter("bind:loadImageByMDImage")
    public static void loadImageByMDImage(ImageView imageView, MDImage mdImage) {
        if (mdImage.getImageLocalPath() != null && mdImage.getImageLocalPath().contains("storage")) {
            // 加载本地图片
            Glide.with(MDGroundApplication.mInstance)
                    .load(new File(mdImage.getImageLocalPath()))
                    .centerCrop()
                    .placeholder(R.drawable.layerlist_image_placeholder)
                    .error(R.drawable.layerlist_image_placeholder)
                    .dontAnimate()
                    .into(imageView);
        } else {
            // 加载网络图片
            Glide.with(MDGroundApplication.mInstance)
                    .load(mdImage)
                    .centerCrop()
                    .placeholder(R.drawable.layerlist_image_placeholder)
                    .error(R.drawable.layerlist_image_placeholder)
                    .dontAnimate()
                    .into(imageView);
        }
    }
}

