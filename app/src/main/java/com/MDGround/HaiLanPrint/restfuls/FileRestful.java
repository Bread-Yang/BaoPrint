package com.MDGround.HaiLanPrint.restfuls;

import android.graphics.Bitmap;
import android.util.Base64;

import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.enumobject.UploadType;
import com.MDGround.HaiLanPrint.enumobject.restfuls.BusinessType;
import com.MDGround.HaiLanPrint.models.User;
import com.MDGround.HaiLanPrint.restfuls.Interceptor.ProgressRequestBody;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.socks.library.KLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import retrofit2.Callback;

/**
 * Created by yoghourt on 5/6/16.
 */
public class FileRestful extends BaseRestful {

    private static FileRestful mIntance = new FileRestful();

    @Override
    protected BusinessType getBusinessType() {
        return BusinessType.FILE;
    }

    @Override
    protected String getHost() {
        return Constants.FILE_HOST;
    }


    private FileRestful() {

    }

    public static FileRestful getInstance() {
        if (mIntance == null) {
            mIntance = new FileRestful();
        }
        return mIntance;
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        String dataStr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        try {
            dataStr = URLEncoder.encode(dataStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return dataStr;
    }

    // 获取图片
    public ResponseData GetPhoto(int PhotoID) {
        JsonObject obj = new JsonObject();
        obj.addProperty("PhotoID", PhotoID);

        return synchronousPost("GetPhoto", obj);
    }

    // 上传图片到云相册
    public void UploadCloudPhoto(final boolean isShare, final File photo,
                                 final ProgressRequestBody.UploadCallbacks uploadCallbacks,
                                 final Callback<ResponseData> callback) {
        if (photo == null) {
            KLog.e("photo是空的");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = ViewUtils.getSmallBitmap(photo.getPath());
                if (bitmap != null) {
                    String photoData = bitmapToString(bitmap);
                    String fileName = photo.getName();

                    JsonObject obj = new JsonObject();
                    obj.addProperty("Shared", isShare);
                    obj.addProperty("PhotoData", photoData);
                    obj.addProperty("FileName", fileName);

                    uploadImagePost("UploadCloudPhoto", obj, uploadCallbacks, callback);
                } else {
                    callback.onResponse(null, null);
                }
            }
        }).start();
    }

    // 上传照片使用接口(云相册除外）
    public void UploadPhoto(final UploadType uploadType,
                            final Bitmap bitmap,
                            final Callback<ResponseData> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String photoData = bitmapToString(bitmap);

                JsonObject obj = new JsonObject();
                obj.addProperty("UploadType", uploadType.value());
                obj.addProperty("PhotoData", photoData);

                uploadImagePost("UploadCloudPhoto", obj, null, callback);
            }
        }).start();
    }

    //上传头像的接口
    public void SaveUserPhoto(final int UserID, final File photo, final User userInfo, final ProgressRequestBody.UploadCallbacks uploadCallbacks,
                              final Callback<ResponseData> callback) {
        if (photo == null) {
            KLog.e("photo是空");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                long fileSize = photo.length();
                Bitmap bitmap = ViewUtils.getSmallBitmap(photo.getPath());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                String dataStr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                try {
                    dataStr = URLEncoder.encode(dataStr, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                JsonObject obj = new JsonObject();
                obj.addProperty("UserID", UserID);
                obj.addProperty("PhotoData", dataStr);
                obj.add("UserInfo", new Gson().toJsonTree(userInfo));

                KLog.e("上传的JSON" + obj.toString());
                uploadImagePost("SaveUserPhoto ", obj, uploadCallbacks, callback);
            }
        }

        ).start();
    }
}


