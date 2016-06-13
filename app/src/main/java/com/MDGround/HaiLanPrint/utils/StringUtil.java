package com.MDGround.HaiLanPrint.utils;

import android.text.TextUtils;

import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.greendao.Location;
import com.MDGround.HaiLanPrint.models.DeliveryAddress;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Created by yoghourt on 5/6/16.
 */
public class StringUtil {

    public static boolean isEmpty(String string) {
        if (string == null || "".equals(string) || TextUtils.isEmpty(string) || "[]".equals(string) || "null".equals(string) || "[null]".equals(string)) {
            return true;
        }
        return false;
    }

    public static String toYuanWithoutUnit(float amount) {
        return String.format("%.02f", amount / 100);
    }

    public static String toYuanWithUnit(float amount) {
        return String.format("%.02f", amount / 100) + "元";
    }

    public static String getCompleteAddress(DeliveryAddress deliveryAddress) {
        Location province = MDGroundApplication.mDaoSession.getLocationDao().load(deliveryAddress.getProvinceID());
        Location city = MDGroundApplication.mDaoSession.getLocationDao().load(deliveryAddress.getCityID());
        Location county = MDGroundApplication.mDaoSession.getLocationDao().load(deliveryAddress.getDistrictID());

        String completeAddress = province.getLocationName() + city.getLocationName() + county.getLocationName() + deliveryAddress.getStreet();

        return completeAddress;
    }

    public static <T> T getInstanceByJsonString(String jsonString, TypeToken<T> type) {
        if (type == null) {
            return null;
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        if (jsonString == null) {
            return null;
        } else {
            return gson.fromJson(jsonString, type.getType());
        }
    }

    public static <T> T getInstanceByJsonString(String jsonString, Class<? extends T> clazz) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        if (jsonString == null) {
            return null;
        } else {
            return gson.fromJson(jsonString, clazz);
        }
    }
}
