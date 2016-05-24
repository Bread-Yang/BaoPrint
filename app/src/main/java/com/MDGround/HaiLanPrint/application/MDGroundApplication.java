package com.MDGround.HaiLanPrint.application;

import android.app.Application;
import android.content.Context;

import com.MDGround.HaiLanPrint.models.Measurement;
import com.MDGround.HaiLanPrint.models.User;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by yoghourt on 5/6/16.
 */
public class MDGroundApplication extends Application{

    /** 对外提供整个应用生命周期的Context **/
    public static Context mInstance;

    public static User mLoginUser;// 登陆用户

    public static Measurement mChooseMeasurement;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        ShareSDK.initSDK(this);
    }
}
