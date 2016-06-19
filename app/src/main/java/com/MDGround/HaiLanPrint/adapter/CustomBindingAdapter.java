package com.MDGround.HaiLanPrint.adapter;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.utils.GlideUtil;

/**
 * Created by yoghourt on 5/12/16.
 */
public class CustomBindingAdapter {

    @BindingAdapter("bind:loadImageByMDImage")
    public static void

    loadImageByMDImage(ImageView imageView, MDImage mdImage) {
        GlideUtil.loadImageByMDImage(imageView, mdImage, true);
    }

    @BindingAdapter("bind:loadImageByPhotoSID")
    public static void loadImageByPhotoSID(ImageView imageView, int photoSID) {
        GlideUtil.loadImageByPhotoSID(imageView, photoSID, true);
    }
}

