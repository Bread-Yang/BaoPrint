package com.yideguan.imageprint.utils;

import android.text.TextUtils;

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
}
