package com.MDGround.HaiLanPrint.restfuls;

import android.util.Base64;

import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.enumobject.restfuls.BusinessType;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.socks.library.KLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    public void UploadCloudPhoto(boolean isShare, File photo, Callback<ResponseData> callback) {
        if (photo == null) {
            return;
        }

        long fileSize = photo.length();

        byte[] buffer = null;
        FileInputStream in = null;
        try {
            // 一次读多个字节
            in = new FileInputStream(photo);
            buffer = new byte[(int) fileSize];
            int offset = 0;
            int numRead = 0;

            while (offset < buffer.length && (numRead = in.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numRead;
            }
            // 确保所有数据均被读取
            if (offset != buffer.length) {
                throw new IOException("Could not completely read uploadFile " + photo.getName());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (buffer == null || buffer.length == 0) {
            KLog.e("读取图片失败");
            return;
        }

        String fileName = photo.getName();

        String dataStr = Base64.encodeToString(buffer, Base64.DEFAULT);
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

        asynchronousPost("LoginUser", obj.toString(), callback);
    }
}