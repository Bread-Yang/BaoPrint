package com.MDGround.HaiLanPrint.utils;

import android.widget.ImageView;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.bumptech.glide.Glide;

/**
 * Created by yoghourt on 5/18/16.
 */
public class GlideUtil {

    public static void loadImageByMDImage(ImageView imageView, MDImage mdImage) {
//        if (mdImage.getImageLocalPath() != null && mdImage.getImageLocalPath().contains("storage")) {
//            // 加载本地图片
//            Glide.with(MDGroundApplication.mInstance)
//                    .load(new File(mdImage.getImageLocalPath()))
//                    .centerCrop()
//                    .placeholder(R.drawable.layerlist_image_placeholder)
//                    .error(R.drawable.layerlist_image_placeholder)
//                    .dontAnimate()
//                    .into(imageView);
//        } else {
//            // 加载网络图片
//            Glide.with(MDGroundApplication.mInstance)
//                    .load(mdImage)
//                    .centerCrop()
//                    .placeholder(R.drawable.layerlist_image_placeholder)
//                    .error(R.drawable.layerlist_image_placeholder)
//                    .dontAnimate()
//                    .into(imageView);
//        }
        Glide.with(MDGroundApplication.mInstance)
                .load(mdImage)
                .centerCrop()
                .thumbnail(0.1f)
                .placeholder(R.drawable.layerlist_image_placeholder)
                .error(R.drawable.layerlist_image_placeholder)
                .dontAnimate()
                .into(imageView);
    }
}
