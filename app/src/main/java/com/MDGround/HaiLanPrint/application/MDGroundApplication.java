package com.MDGround.HaiLanPrint.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.enumobject.ProductType;
import com.MDGround.HaiLanPrint.greendao.DaoMaster;
import com.MDGround.HaiLanPrint.greendao.DaoSession;
import com.MDGround.HaiLanPrint.greendao.DatabaseOpenHelper;
import com.MDGround.HaiLanPrint.models.Measurement;
import com.MDGround.HaiLanPrint.models.PhotoTypeExplain;
import com.MDGround.HaiLanPrint.models.Template;
import com.MDGround.HaiLanPrint.models.User;
import com.MDGround.HaiLanPrint.utils.FileUtils;
import com.MDGround.HaiLanPrint.utils.OrderUtils;
import com.socks.library.KLog;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.SMSSDK;

/**
 * Created by yoghourt on 5/6/16.
 */
public class MDGroundApplication extends Application {

    /**
     * 对外提供整个应用生命周期的Context
     **/
    public static MDGroundApplication sInstance;

    public static DaoSession sDaoSession;

    public static OrderUtils sOrderutUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        initDataBase();

        ShareSDK.initSDK(this);

        SMSSDK.initSDK(this, Constants.SMS_APP_KEY, Constants.SMS_APP_SECRECT);

        XGPushManager.registerPush(getApplicationContext(), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                KLog.e("信鸽注册成功");
            }

            @Override
            public void onFail(Object o, int i, String s) {
                KLog.e("信鸽注册失败");
            }
        });

//        initExceptionHandler();
    }

    private void initDataBase() {
        DatabaseOpenHelper helper = new DatabaseOpenHelper(this, Constants.DATABASE_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        sDaoSession = daoMaster.newSession();
    }

    private void initExceptionHandler() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

    public void resetData() {
        setChoosedProductType(null);
        setChoosedMeasurement(null);
        setChoosedTemplate(null);
    }

    public User getLoginUser() {
        return (User) FileUtils.getObject(Constants.KEY_ALREADY_LOGIN_USER);
    }

    public void setLoginUser(User loginUser) {
        FileUtils.setObject(Constants.KEY_ALREADY_LOGIN_USER, loginUser);
    }

    public ProductType getChoosedProductType() {
        return (ProductType) FileUtils.getObject(Constants.KEY_ALREADY_CHOOSED_PRODUCT_TYPE);
    }

    public void setChoosedProductType(ProductType choosedProductType) {
        FileUtils.setObject(Constants.KEY_ALREADY_CHOOSED_PRODUCT_TYPE, choosedProductType);
    }

    public Measurement getChoosedMeasurement() {
        return (Measurement) FileUtils.getObject(Constants.KEY_ALREADY_CHOOSED_MEASUREMENT);
    }

    public void setChoosedMeasurement(Measurement choosedMeasurement) {
        FileUtils.setObject(Constants.KEY_ALREADY_CHOOSED_MEASUREMENT, choosedMeasurement);
    }

    public Template getChoosedTemplate() {
        return (Template) FileUtils.getObject(Constants.KEY_ALREADY_CHOOSED_TEMPLATE);
    }

    public void setChoosedTemplate(Template choosedTemplate) {
        FileUtils.setObject(Constants.KEY_ALREADY_CHOOSED_TEMPLATE, choosedTemplate);
    }

    public ArrayList<PhotoTypeExplain> getPhotoTypeExplainArrayList() {
        return (ArrayList<PhotoTypeExplain>) FileUtils.getObject(Constants.KEY_PHOTO_TYPE_EXPLAIN_ARRAYLIST);
    }

    public void setPhotoTypeExplainArrayList(ArrayList<PhotoTypeExplain> photoTypeExplainArrayList) {
        FileUtils.setObject(Constants.KEY_PHOTO_TYPE_EXPLAIN_ARRAYLIST, photoTypeExplainArrayList);
    }
}
