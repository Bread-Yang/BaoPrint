package com.MDGround.HaiLanPrint.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.IntentCompat;

import com.MDGround.HaiLanPrint.activity.artalbum.ArtAlbumEditActivity;
import com.MDGround.HaiLanPrint.activity.calendar.CalendarEditActivity;
import com.MDGround.HaiLanPrint.activity.cloudphotos.CloudDetailActivity;
import com.MDGround.HaiLanPrint.activity.engraving.EngravingChoosePaperNumActivity;
import com.MDGround.HaiLanPrint.activity.login.LoginActivity;
import com.MDGround.HaiLanPrint.activity.lomocard.LomoCardEditActivity;
import com.MDGround.HaiLanPrint.activity.magazinealbum.MagazineEditActivity;
import com.MDGround.HaiLanPrint.activity.magiccup.MagicCupPhotoEditActivity;
import com.MDGround.HaiLanPrint.activity.main.MainActivity;
import com.MDGround.HaiLanPrint.activity.payment.PaymentPreviewActivity;
import com.MDGround.HaiLanPrint.activity.phoneshell.PhoneShellEditActivity;
import com.MDGround.HaiLanPrint.activity.photoprint.PrintPhotoChoosePaperNumActivity;
import com.MDGround.HaiLanPrint.activity.pictureframe.PictureFrameEditActivity;
import com.MDGround.HaiLanPrint.activity.poker.PokerEditActivity;
import com.MDGround.HaiLanPrint.activity.postcard.PostcardEditActivity;
import com.MDGround.HaiLanPrint.activity.puzzle.PuzzleEditActivity;
import com.MDGround.HaiLanPrint.activity.selectimage.SelectAlbumBeforeEditActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

import static com.MDGround.HaiLanPrint.utils.SelectImageUtil.mTemplateImage;

/**
 * Created by yoghourt on 5/12/16.
 */
public class NavUtils {

    private static int mLoadCompleteCount = 0;

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
        Intent intent = new Intent(context, SelectAlbumBeforeEditActivity.class);
        context.startActivity(intent);
    }

    public static void toPhotoEditActivity(final Context context) {
        ViewUtils.loading(context);
        if (SelectImageUtil.mAlreadySelectImage.size() == 0) {
            return;
        }

        boolean hasTemplate = true;
        final Intent intent = new Intent();
        switch (MDGroundApplication.mChoosedProductType) {
            // pager 1
            case PrintPhoto:
                hasTemplate = false;
                intent.setClass(context, PrintPhotoChoosePaperNumActivity.class);
                break;
            case Postcard:
                intent.setClass(context, PostcardEditActivity.class);
                break;
            case MagazineAlbum:
                intent.setClass(context, MagazineEditActivity.class);
                break;
            case ArtAlbum:
                intent.setClass(context, ArtAlbumEditActivity.class);
                break;
            case PictureFrame:
                intent.setClass(context, PictureFrameEditActivity.class);
                break;
            case Calendar:
                intent.setClass(context, CalendarEditActivity.class);
                break;
            case PhoneShell:
                intent.setClass(context, PhoneShellEditActivity.class);
                break;
            case Poker:
                intent.setClass(context, PokerEditActivity.class);
                break;
            case Puzzle:
                intent.setClass(context, PuzzleEditActivity.class);
                break;
            case MagicCup:
                intent.setClass(context, MagicCupPhotoEditActivity.class);
                break;
            case LOMOCard:
                intent.setClass(context, LomoCardEditActivity.class);
                break;
            case Engraving:
                hasTemplate = false;
                intent.setClass(context, EngravingChoosePaperNumActivity.class);
                break;
        }

        // 将所有选中图片的数量设为1
        for (MDImage mdImage : SelectImageUtil.mAlreadySelectImage) {
            mdImage.setPhotoCount(1);
        }

        if (hasTemplate) {
            // 先加载全部模版图片,再进入编辑界面
            mLoadCompleteCount = 0;
            for (MDImage mdImage : mTemplateImage) {
                Glide.with(context)
                        .load(mdImage)
                        .downloadOnly(new SimpleTarget<File>() {
                            @Override
                            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                                mLoadCompleteCount++;
                                if (mLoadCompleteCount == mTemplateImage.size()) {
                                    // 服务器返回的模板数量少于pagecount
                                    if (mTemplateImage.size() < MDGroundApplication.mChoosedTemplate.getPageCount()) {
                                        int difference = MDGroundApplication.mChoosedTemplate.getPageCount() - mTemplateImage.size();
                                        for (int i = 0; i < difference; i++) {
                                            mTemplateImage.add(new MDImage());
                                        }
                                    }
                                    // 选择的图片数量小于模板的数量
                                    if (SelectImageUtil.mAlreadySelectImage.size() < mTemplateImage.size()) {
                                        int difference = mTemplateImage.size() - SelectImageUtil.mAlreadySelectImage.size();

                                        for (int i = 0; i < difference; i++) {
                                            SelectImageUtil.mAlreadySelectImage.add(new MDImage());
                                        }
                                    }
                                    ViewUtils.dismiss();
                                    context.startActivity(intent);

                                }
                            }
                        });
            }
        } else {
            ViewUtils.dismiss();
            context.startActivity(intent);
        }
    }

    public static void toPaymentPreviewActivity(Context context) {
        Intent intent = new Intent(context, PaymentPreviewActivity.class);
        context.startActivity(intent);
    }

}
