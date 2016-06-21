package com.MDGround.HaiLanPrint.utils;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.restfuls.FileRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private static void uploadImageRequest(final int upload_image_index) {
        if (upload_image_index < SelectImageUtils.mAlreadySelectImage.size()) {
            final MDImage mdImage = SelectImageUtils.mAlreadySelectImage.get(upload_image_index);

            final int nextUploadIndex = upload_image_index + 1;

            if (mdImage.getImageLocalPath() != null && !StringUtil.isEmpty(mdImage.getImageLocalPath())) { // 本地图片
                File file = new File(mdImage.getImageLocalPath());

                KLog.e("mdImage.getImageLocalPath() : " + mdImage.getImageLocalPath());

                // 上传本地照片
                FileRestful.getInstance().UploadCloudPhoto(false, file, null, new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        final MDImage responseImage = response.body().getContent(MDImage.class);

                        if (mdImage.getSyntheticImageLocalPath() != null && !StringUtil.isEmpty(mdImage.getSyntheticImageLocalPath())) { // 合成图片
                            File syntheticFile = new File(mdImage.getSyntheticImageLocalPath());

                            // 上传合成图片
                            FileRestful.getInstance().UploadCloudPhoto(false, syntheticFile, null, new Callback<ResponseData>() {
                                @Override
                                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                                    MDImage responseSyntheticImage = response.body().getContent(MDImage.class);

                                    responseImage.setSyntheticPhotoID(responseSyntheticImage.getPhotoID());
                                    responseImage.setSyntheticPhotoSID(responseSyntheticImage.getPhotoSID());

                                    SelectImageUtils.mAlreadySelectImage.set(upload_image_index, responseImage);
                                    uploadImageRequest(nextUploadIndex);
                                }

                                @Override
                                public void onFailure(Call<ResponseData> call, Throwable t) {

                                }
                            });
                        } else {
                            SelectImageUtils.mAlreadySelectImage.set(upload_image_index, responseImage);
                            uploadImageRequest(nextUploadIndex);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                    }
                });
            } else {
                uploadImageRequest(nextUploadIndex);
            }
        } else {
            // 全部图片上传完之后,生成订单
//            SaveOrderRequest();
        }
    }
}
