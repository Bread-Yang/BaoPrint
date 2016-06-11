package com.MDGround.HaiLanPrint.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.IntentCompat;

import com.MDGround.HaiLanPrint.activity.cloudphotos.CloudDetailActivity;
import com.MDGround.HaiLanPrint.activity.engraving.EngravingChoosePaperNumActivity;
import com.MDGround.HaiLanPrint.activity.login.LoginActivity;
import com.MDGround.HaiLanPrint.activity.magiccup.MagicCupPhotoEditActivity;
import com.MDGround.HaiLanPrint.activity.main.MainActivity;
import com.MDGround.HaiLanPrint.activity.payment.PaymentPreviewActivity;
import com.MDGround.HaiLanPrint.activity.photoedit.PhotoEditActivity;
import com.MDGround.HaiLanPrint.activity.photoprint.PrintPhotoChoosePaperNumActivity;
import com.MDGround.HaiLanPrint.activity.pictureframe.PictureFrameTempalteDetailActivity;
import com.MDGround.HaiLanPrint.activity.selectimage.SelectAlbumActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.OrderWork;

/**
 * Created by yoghourt on 5/12/16.
 */
public class NavUtils {

    public static void toLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void toMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void toCloudDetailActivity(Context context, MDImage image) {
        Intent intent = new Intent(context, CloudDetailActivity.class);
        intent.putExtra(Constants.KEY_CLOUD_IMAGE, image);
        context.startActivity(intent);
    }

    public static void toSelectAlbumActivity(Context context) {
        Intent intent = new Intent(context, SelectAlbumActivity.class);
        context.startActivity(intent);
    }

    public static void toPhotoEditActivity(Context context) {
        if (SelectImageUtil.mAlreadySelectImage.size() == 0) {
            return;
        }
        Intent intent = new Intent();
        switch (MDGroundApplication.mChoosedProductType) {
            // pager 1
            case PrintPhoto:
                intent.setClass(context, PrintPhotoChoosePaperNumActivity.class);
                break;
            case Postcard:
                intent.setClass(context, PhotoEditActivity.class);
                break;
            case MagazineAlbum:
                intent.setClass(context, PhotoEditActivity.class);
                break;
            case PictureFrame:
                intent.setClass(context, PictureFrameTempalteDetailActivity.class);
                break;
            case PhoneShell:
                intent.setClass(context, PhotoEditActivity.class);
                break;
            case MagicCup:
                intent.setClass(context, MagicCupPhotoEditActivity.class);
                break;
            case Puzzle:
                intent.setClass(context, PhotoEditActivity.class);
                break;
            case LOMOCard:
                intent.setClass(context, PhotoEditActivity.class);
                break;
            case Engraving:
                intent.setClass(context, EngravingChoosePaperNumActivity.class);
                break;
        }
        context.startActivity(intent);
    }

    public static void toPaymentPreviewActivity(Context context, OrderWork orderWork) {
        Intent intent = new Intent(context, PaymentPreviewActivity.class);
        intent.putExtra(Constants.KEY_ORDER_WORK, orderWork);
        context.startActivity(intent);
    }

}
