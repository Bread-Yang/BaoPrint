package com.MDGround.HaiLanPrint.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by yoghourt on 8/1/16.
 */
public class TemplateUtils {

    /**
     * android编辑区域的高度 = (android编辑区域的宽度 / JWork返回的page width) * JWork返回的page height
     *
     * @return
     */
    public static float getEditHeightOnAndroid(Bitmap bitmap) {
        float windowWidth = ((float) ViewUtils.screenWidth());
        float pageWidth = (float) bitmap.getWidth();
        return (windowWidth / pageWidth) * (float) bitmap.getHeight();
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
