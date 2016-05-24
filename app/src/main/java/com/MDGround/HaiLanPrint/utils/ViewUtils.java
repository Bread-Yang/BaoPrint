package com.MDGround.HaiLanPrint.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;

/**
 * Created by shadow on 15/11/7.
 */
public class ViewUtils {

    private static ProgressDialog progressDialog;

    public static Uri imageUrl;

    public static int screenWidth() {
        Context context = MDGroundApplication.mInstance;
        if (context == null) {
            return 0;
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Configuration configuration = context.getResources().getConfiguration();
        return configuration.orientation == Configuration.ORIENTATION_PORTRAIT ? dm.widthPixels : dm.heightPixels;
    }

    public static int screenHeight() {
        Context context = MDGroundApplication.mInstance;
        if (context == null) {
            return 0;
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Configuration configuration = context.getResources().getConfiguration();
        return configuration.orientation == Configuration.ORIENTATION_PORTRAIT ? dm.heightPixels : dm.widthPixels;
    }

    public static int screenDensity() {
        Context context = MDGroundApplication.mInstance;
        if (context == null) {
            return 0;
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) dm.density;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        final float scale = MDGroundApplication.mInstance.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        final float scale = MDGroundApplication.mInstance.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static void toast(String str) {
        Toast.makeText(MDGroundApplication.mInstance, str, Toast.LENGTH_SHORT).show();
    }

    public static void toast(int resId) {
        Toast.makeText(MDGroundApplication.mInstance, resId, Toast.LENGTH_SHORT).show();
    }

    public static void loading(Context context) {
        try {
            dismiss();
            String message = context.getString(R.string.loading);
            progressDialog = ProgressDialog.show(context, null, message);
        } catch (Throwable e) {

        }
    }

    public static void dismiss() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Throwable e) {

        }

    }

    public static void closeKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public static void openKeyboard(Activity context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        editText.requestFocus();
        imm.showSoftInput(editText, 0);
    }

    public static String getString(int resId) {
        return MDGroundApplication.mInstance.getString(resId);
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 720, 1080);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }


    public static void copy(String str) {
        ClipboardManager clipboardManager = (ClipboardManager) MDGroundApplication.mInstance.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(str);
        ViewUtils.toast(str);
    }

    public static void isShowPassword(boolean isShow, EditText editText) {
        if (isShow) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        editText.setSelection(editText.length());
    }
}
