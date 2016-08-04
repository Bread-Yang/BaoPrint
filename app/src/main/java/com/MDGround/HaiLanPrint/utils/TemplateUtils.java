package com.MDGround.HaiLanPrint.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;

import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.enumobject.ProductType;

/**
 * Created by yoghourt on 8/1/16.
 */
public class TemplateUtils {

    /**
     * 是否有定位块
     *
     * @return
     */
    public static boolean isTemplateHasModules() {
        boolean hasModules = false;

        ProductType chooseProductType = MDGroundApplication.sInstance.getChoosedProductType();

        // 杂志册,艺术册,个性月历 这三个功能块有定位块
        if (chooseProductType == ProductType.MagazineAlbum
                || chooseProductType == ProductType.ArtAlbum
                || chooseProductType == ProductType.Calendar) {
            hasModules = true;
        }

        return hasModules;
    }

    /**
     * android编辑区域的高度 = (android编辑区域的宽度 / JWork返回的page width) * JWork返回的page height
     *
     * @return
     */
    public static float getEditHeightOnAndroid(Bitmap bitmap) {
        float windowWidth = ((float) ViewUtils.screenWidth());
        float bitmapWidth = (float) bitmap.getWidth();
        return (windowWidth / bitmapWidth) * (float) bitmap.getHeight();
    }

    public static Point getEditPointOnAndroid(Bitmap bitmap) {
        float bitmapWidth = bitmap.getWidth();
        float bitmapHeight = bitmap.getHeight();

        float windowWidth = ((float) ViewUtils.screenWidth());
        float halfWindownHeight = ((float) ViewUtils.screenHeight() / 2.0f);  // 默认最大高度是屏幕的一半

        float widthScale = windowWidth / bitmapWidth;
        float heightScale = halfWindownHeight / bitmapHeight;

        Point point = new Point();
        if (widthScale < heightScale) {
            point.x = (int) windowWidth;
            point.y = (int) (widthScale * bitmapHeight);
        } else {
            point.x = (int) (heightScale * bitmapWidth);
            point.y = (int) halfWindownHeight;
        }

        return point;
    }

    /**
     * @param templateBitmap 模版的背景图片
     * @return
     */
    public static float getRateOfEditWidthOnAndroid(Bitmap templateBitmap) {
        return ((float) ViewUtils.screenWidth()) / templateBitmap.getWidth();
    }

    // 将String cast成 Matrix
    public static Matrix getMatrixByString(String maxtrixString) {
        Matrix matrix = new Matrix();
        if (!StringUtil.isEmpty(maxtrixString)) {
            float[] values = new float[9];
            matrix.getValues(values);
            StringBuffer buff = new StringBuffer();
            int i = 0;
            for (char c : maxtrixString.toCharArray()) {
                if (c != '[') {
                    if (c != ',') {
                        if (c == ']') {
                            break;
                        }
                        buff.append(c);
                    } else {
                        values[i] = Float.parseFloat(buff.toString());
                        buff = new StringBuffer();
                        i++;
                    }
                }
            }
//            values[2] = values[2] * this.scaleWin;
//            values[5] = values[5] * this.scaleWin;
            matrix.setValues(values);
        }

        return matrix;
    }

    public static String getStringByMatrix(Matrix matrix) {
        float[] values = new float[9];
        matrix.getValues(values);

        String matrixString = "[" + values[0] + "," + values[1] + "," + values[2] + "," + values[3] + ","
                + values[4] + "," + values[5] + "," + values[6] + "," + values[7] + "," + values[8] + "]";

        return matrixString;
    }
}
