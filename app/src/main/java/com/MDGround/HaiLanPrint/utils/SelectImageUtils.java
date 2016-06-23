package com.MDGround.HaiLanPrint.utils;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.models.MDImage;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by yoghourt on 5/16/16.
 */
public class SelectImageUtils {

    public static ArrayList<MDImage> mAlreadySelectImage = new ArrayList<>();

    public static ArrayList<MDImage> mTemplateImage = new ArrayList<>();

    private static SelectImageUtils mIntance = new SelectImageUtils();

    public interface UploadAllImageSuccessListener {
        public void uploadAllImageSuccess();
    }

    private SelectImageUtils() {

    }

    public static SelectImageUtils getInstance() {
        if (mIntance == null) {
            mIntance = new SelectImageUtils();
        }
        return mIntance;
    }

    public static void addImage(MDImage mdImage) {
        for (MDImage item : mAlreadySelectImage) {
            if (isSameImage(item, mdImage)) {
                return;
            }
        }
        mAlreadySelectImage.add(mdImage);
    }

    public static void removeImage(MDImage mdImage) {
        Iterator<MDImage> iterator = mAlreadySelectImage.iterator();
        while (iterator.hasNext()) {

            MDImage item = iterator.next();

            if (isSameImage(item, mdImage)) {
                iterator.remove();
                break;
            }
        }
    }

    public static boolean isSameImage(MDImage originalImage, MDImage compareImage) {
        if ((originalImage.getAutoID() != 0 && originalImage.getAutoID() == compareImage.getAutoID())) { // 同一张网络图片
            return true;
        }
        if (((originalImage.getImageLocalPath() != null && compareImage.getImageLocalPath() != null)
                && originalImage.getImageLocalPath().equals(compareImage.getImageLocalPath()))) {       // 同一张本地图片
            return true;
        }
        return false;
    }

    public static int getMaxSelectImageNum(ProductType productType) {
        switch (productType) {
            case PrintPhoto:
                return Constants.PRINT_PHOTO_MAX_SELECT_IMAGE_NUM;
            case PictureFrame:
                return Constants.PICTURE_FRAME_MAX_SELECT_IMAGE_NUM;
            case MagicCup:
                return Constants.MAGIC_CUP_MAX_SELECT_IMAGE_NUM;
            case Puzzle:
                return Constants.PUZZLEL_MAX_SELECT_IMAGE_NUM;
            case PhoneShell:
                return Constants.PHONE_SHELL_MAX_SELECT_IMAGE_NUM;
            case Engraving:
                return Constants.ENGRAVING_MAX_SELECT_IMAGE_NUM;
        }
        return 0;
    }

    public static int getPrintPhotoOrEngravingOrderCount() {
        int count = 0;
        for (MDImage mdImage : mAlreadySelectImage) {
            count += mdImage.getPhotoCount();
        }
        return count;
    }
}
