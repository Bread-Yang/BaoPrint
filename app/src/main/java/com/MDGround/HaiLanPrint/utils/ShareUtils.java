package com.MDGround.HaiLanPrint.utils;

import android.content.Context;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.constants.Constants;

import java.net.URLEncoder;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by yoghourt on 6/24/16.
 */

public class ShareUtils {

    public static String createShareURL(String workId, String userId) {
        String shareUrl = null;
        String workID = null;
        String userID = null;
        try {
            workID = EncryptUtil.encrypt(workId);
            userID = EncryptUtil.encrypt(userId);
            workID = URLEncoder.encode(workID, "UTF-8");
            userID = URLEncoder.encode(userID, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"".equals(workID) && !"".equals(userID)) {
            shareUrl = Constants.HOST + "ShareWorkPhoto.aspx?workId=" + workID + "&userId=" + userID;
        }
        return shareUrl;
    }

    // 生成图片分享
    public static Platform.ShareParams initImageShareParams(Context context, String imagePath) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setTitle(context.getString(R.string.app_name));
        shareParams.setText(context.getString(R.string.app_name));
        shareParams.setImagePath(imagePath);
        shareParams.setShareType(Platform.SHARE_TEXT);
        shareParams.setShareType(Platform.SHARE_IMAGE);
        return shareParams;
    }

    //生成分享链接
    public static Platform.ShareParams initURLShareParams(Context context, String url) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setTitle(context.getString(R.string.app_name));
        shareParams.setText(url);
        shareParams.setTitleUrl(url);
        shareParams.setUrl(url);
        shareParams.setSite(context.getString(R.string.app_name));
        shareParams.setSiteUrl(url);
        shareParams.setShareType(Platform.SHARE_TEXT);
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        return shareParams;
    }

    public static void shareToWechat(Context context, Platform.ShareParams shareParams) {
        if (shareParams != null) {
            Platform wechatPlatform = ShareSDK.getPlatform(context, "WechatMoments");
            wechatPlatform.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {

                }

                @Override
                public void onCancel(Platform platform, int i) {

                }
            });
            wechatPlatform.share(shareParams);
        }
    }

    public static void shareToQQ(Context context, Platform.ShareParams shareParams) {
        if (shareParams != null) {
            Platform qqPlatform = ShareSDK.getPlatform(context, "QQ");
            qqPlatform.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {

                }

                @Override
                public void onCancel(Platform platform, int i) {

                }
            });
            qqPlatform.share(shareParams);
        }
    }

    public static void shareToWeibo(Context context, Platform.ShareParams shareParams) {
        if (shareParams != null) {
            Platform weiboPlatform = ShareSDK.getPlatform(context, "SinaWeibo");
            weiboPlatform.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {

                }

                @Override
                public void onCancel(Platform platform, int i) {

                }
            });
            weiboPlatform.share(shareParams);
        }
    }

}
