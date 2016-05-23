package com.MDGround.HaiLanPrint.utils;

import android.content.Context;
import android.content.Intent;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.activity.cloudphotos.CloudDetailActivity;
import com.MDGround.HaiLanPrint.activity.photoedit.PhotoEditActivity;
import com.MDGround.HaiLanPrint.activity.photoprint.PrintPhotoChoosePaperNumActivity;
import com.MDGround.HaiLanPrint.activity.selectimage.SelectAlbumActivity;
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

    public static void toSelectAlbumActivity(Context context, ProductType productTyp) {
        Intent intent = new Intent(context, SelectAlbumActivity.class);
        intent.putExtra(Constants.KEY_PRODUCT_TYPE, productTyp);
        context.startActivity(intent);
    }

    public static void toSelectImageActivity(Context context, Album album, ProductType productType) {
        Intent intent = new Intent(context, SelectImageActivity.class);
        intent.putExtra(Constants.KEY_ALBUM, album);
        intent.putExtra(Constants.KEY_PRODUCT_TYPE, productType);
        context.startActivity(intent);
    }

    public static void toPhotoEditActivity(Context context, ProductType productType) {
        if (SelectImageUtil.mAlreadySelectImage.size() == 0) {
            return;
        }
        Intent intent = new Intent();
        switch (productType) {
            case PrintPhoto:
                intent.setClass(context, PrintPhotoChoosePaperNumActivity.class);
                break;
            case MagicCup:
                intent.setClass(context, PhotoEditActivity.class);
                break;
            case Puzzle:
                intent.setClass(context, PhotoEditActivity.class);
                break;
            case LOMOCard:
                intent.setClass(context, PhotoEditActivity.class);
                break;
        }
        context.startActivity(intent);
    }

}
