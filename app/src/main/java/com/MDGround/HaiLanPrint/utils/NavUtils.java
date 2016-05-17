package com.MDGround.HaiLanPrint.utils;

import android.content.Context;
import android.content.Intent;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.activity.cloudphotos.CloudDetailActivity;
import com.MDGround.HaiLanPrint.activity.selectimage.SelectImageActivity;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.models.Album;
import com.MDGround.HaiLanPrint.models.MDImage;

/**
 * Created by yoghourt on 5/12/16.
 */
public class NavUtils {

    public static void toCloudDetailActivity(Context context, MDImage image) {
        Intent intent = new Intent(context, CloudDetailActivity.class);
        intent.putExtra(Constants.KEY_CLOUD_IMAGE, image);
        context.startActivity(intent);
    }

    public static void toSelectImageActivity(Context context, Album album, ProductType productType) {
        Intent intent = new Intent(context, SelectImageActivity.class);
        intent.putExtra(Constants.KEY_ALBUM, album);
        intent.putExtra(Constants.KEY_PRODUCT_TYPE, productType);
        context.startActivity(intent);
    }

}
