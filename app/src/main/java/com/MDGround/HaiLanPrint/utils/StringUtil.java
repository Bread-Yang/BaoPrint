package com.MDGround.HaiLanPrint.utils;

import android.text.TextUtils;

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

    public static String getOrderStats(int orderStatus) {
        switch (orderStatus) {
            case 1:
                return "已付款";
            case 2:
                return "已发货";
            case 4:
                return "已完成";
            case 8:
                return "退款中";
            case 16:
                return "已退款";
            case 32:
                return "退款失败";
        }
        return null;
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
