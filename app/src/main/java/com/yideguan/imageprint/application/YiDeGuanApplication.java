package com.yideguan.imageprint.application;

import android.app.Application;
import android.content.Context;

import com.yideguan.imageprint.enumobject.User;

/**
 * Created by yoghourt on 5/6/16.
 */
public class YiDeGuanApplication extends Application{

    /** 对外提供整个应用生命周期的Context **/
    public static Context INSTANCE;

    public static User mLoginUser;// 登陆用户

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }
}
