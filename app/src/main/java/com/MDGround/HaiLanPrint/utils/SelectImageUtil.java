package com.MDGround.HaiLanPrint.utils;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.models.MDImage;

import java.util.ArrayList;

/**
 * Created by yoghourt on 5/16/16.
 */
public class SelectImageUtil {

    public static ArrayList<MDImage> mAlreadySelectImage = new ArrayList<>();

    private static SelectImageUtil mIntance = new SelectImageUtil();

    private SelectImageUtil() {

    }

    public static SelectImageUtil getInstance() {
        if (mIntance == null) {
            mIntance = new SelectImageUtil();
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
        for (MDImage item : mAlreadySelectImage) {
            if (isSameImage(item, mdImage)) {
                mAlreadySelectImage.remove(item);
                return;
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
        }
        return 0;
    }
}
