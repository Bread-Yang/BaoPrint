package com.MDGround.HaiLanPrint.restfuls;

import android.graphics.Bitmap;
import android.util.Base64;

import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.enumobject.restfuls.BusinessType;
import com.MDGround.HaiLanPrint.models.User;
import com.MDGround.HaiLanPrint.restfuls.Interceptor.ProgressRequestBody;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.socks.library.KLog;

import org.json.JSONException;
import org.json.JSONObject;

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

    // 获取图片
    public ResponseData GetPhoto(int PhotoID) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("PhotoID", PhotoID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return synchronousPost("GetPhoto", obj.toString());
    }

    // 上传图片
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

                long fileSize = photo.length();

//        byte[] buffer = null;
//        FileInputStream in = null;
//        try {
//            // 一次读多个字节
//            in = new FileInputStream(photo);
//            buffer = new byte[(int) fileSize];
//            int offset = 0;
//            int numRead = 0;
//
//            while (offset < buffer.length && (numRead = in.read(buffer, offset, buffer.length - offset)) >= 0) {
//                offset += numRead;
//            }
//            // 确保所有数据均被读取
//            if (offset != buffer.length) {
//                throw new IOException("Could not completely read uploadFile " + photo.getName());
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        if (buffer == null || buffer.length == 0) {
//            KLog.e("读取图片失败");
//            return;
//        }
//                String dataStr = Base64.encodeToString(buffer, Base64.DEFAULT);

                Bitmap bitmap = ViewUtils.getSmallBitmap(photo.getPath());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                String dataStr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

                String fileName = photo.getName();

                try {
                    dataStr = URLEncoder.encode(dataStr, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                JSONObject obj = new JSONObject();
                try {
                    obj.put("Shared", isShare);
                    obj.put("PhotoData", dataStr);
                    obj.put("FileName", fileName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                uploadImagePost("UploadCloudPhoto", obj.toString(), uploadCallbacks, callback);
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
                JSONObject obj = new JSONObject();
                try {
                    obj.put("UserID", UserID);
                    obj.put("PhotoData", dataStr);
                    String jsonString = convertObjectToString(userInfo);
                    KLog.e("userInfo是   "+jsonString);
                    JSONObject jsonObject = new JSONObject(jsonString);
                    obj.put("UserInfo", jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                KLog.e("上传的JSON"+obj.toString());
                uploadImagePost("SaveUserPhoto ",obj.toString(),uploadCallbacks,callback);
            }
        }

        ).start();
    }
}


