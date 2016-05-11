package com.MDGround.HaiLanPrint.glide;

import android.content.Context;
import android.util.Base64;

import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.restfuls.FileRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.socks.library.KLog;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yoghourt on 3/21/16.
 */
public class MDGroundFetcher implements DataFetcher<InputStream> {

    private Context mContext;

    private final LocalMedia mLocalMedia;

    private InputStream mInputStream;

    public MDGroundFetcher(LocalMedia mLocalMedia, Context context) {
        this.mLocalMedia = mLocalMedia;
        this.mContext = context;
    }

    @Override
    public InputStream loadData(Priority priority) throws Exception {
        String url = mLocalMedia.getYiDeGuanImageURL().substring(MedicalConstant.IMAGE_URI_PREFIX.length());

        KLog.e("执行了这里");

        if (url == null || url.equals("")) {
            KLog.e("图片url为空");
            return null;
        }

        String data[] = url.split("\\.");
        if (data.length < 2) {
            KLog.e("图片url不完整");
            return null;
        }

        int clinicID = Integer.parseInt(data[0]);
        int fileID = Integer.parseInt(data[1]);

        ResponseData responseData = FileRestful.getInstance().GetPhoto(fileID);
        if (responseData == null) {
            KLog.e("Glide请求图片失败");
            return null;
        }

        if (responseData.getCode() == ResponseCode.Normal.getValue()) {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(responseData.getContent(), Base64.DEFAULT);
            mInputStream = new ByteArrayInputStream(bitmapArray);
            return mInputStream;
        }

        return null;
    }

    @Override
    public void cleanup() {
        if (mInputStream != null) {
            try {
                mInputStream.close();
            } catch (IOException e) {
                //e.printStackTrace();
            } finally {
                mInputStream = null;
            }
        }
    }

    /**
     * 在UI线程中调用，返回用于区别数据的唯一id
     *
     * @return
     */
    @Override
    public String getId() {
        return mLocalMedia.getYiDeGuanImageURL();
    }

    /**
     * 在UI线程中调用，取消加载任务
     */
    @Override
    public void cancel() {

    }
}
