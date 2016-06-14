package com.MDGround.HaiLanPrint.application;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.greendao.DaoMaster;
import com.MDGround.HaiLanPrint.greendao.DaoSession;
import com.MDGround.HaiLanPrint.greendao.DatabaseOpenHelper;
import com.MDGround.HaiLanPrint.models.Measurement;
import com.MDGround.HaiLanPrint.models.PhotoTypeExplain;
import com.MDGround.HaiLanPrint.models.Template;
import com.MDGround.HaiLanPrint.models.User;
import com.MDGround.HaiLanPrint.utils.OrderUtils;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by yoghourt on 5/6/16.
 */
public class MDGroundApplication extends Application{

    /** 对外提供整个应用生命周期的Context **/
    public static Context mInstance;

    public static DaoSession mDaoSession;

    public static User mLoginUser;// 登陆用户

    public static ProductType mChoosedProductType;

    public static Measurement mChoosedMeasurement;

    public static Template mChoosedTemplate;

    public static ArrayList<PhotoTypeExplain> mPhotoTypeExplainArrayList = new ArrayList<PhotoTypeExplain>();

    public static OrderUtils mOrderutUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        initDataBase();

        ShareSDK.initSDK(this);

    }

    private void initDataBase() {
        DatabaseOpenHelper helper = new DatabaseOpenHelper(this, Constants.DATABASE_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }

    public static void resetData() {
        mChoosedProductType = null;
        mChoosedMeasurement = null;
        mChoosedTemplate = null;
    }
}
