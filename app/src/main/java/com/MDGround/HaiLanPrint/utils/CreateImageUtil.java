package com.MDGround.HaiLanPrint.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.PhotoTemplateAttachFrame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by yoghourt on 8/2/16.
 */
public class CreateImageUtil {

    public static final String SAVEPATH = Tools.getAppPath() + File.separator + "work";

    public static void createAll(final List<MDImage> mdImageList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mdImageList.size(); i++) {
                    final MDImage mdImage = mdImageList.get(i);

                    Bitmap backgroundBitmap = GlideUtil.loadImageAsBitmap(mdImage);
                    Bitmap bitmap = createPageBitmap(backgroundBitmap, mdImage.getPhotoTemplateAttachFrameList());

                    saveBitmapToSDCard(bitmap, i);
                }
            }
        }).start();
    }

    public static Bitmap createPageBitmap(Bitmap backgroundBitmap, List<PhotoTemplateAttachFrame> photoTemplateAttachFrameList) {

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);

        Bitmap bitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(), backgroundBitmap.getHeight(),
                Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        float rateOfEditWidth = TemplateUtils.getRateOfEditWidthOnAndroid(backgroundBitmap);

        // 背景图绘制
        canvas.drawBitmap(backgroundBitmap, 0, 0, paint);

        // 各个模块绘制
        createMould(canvas, paint, photoTemplateAttachFrameList, 1.0f, 1.0f, rateOfEditWidth);

        return bitmap;
    }

    private static void createMould(final Canvas canvas, final Paint paint, List<PhotoTemplateAttachFrame> moulds,
                                    float rate, float r, float rateOfEditWidth) {
        for (int i = 0; i < moulds.size(); i++) {
            final PhotoTemplateAttachFrame photoTemplateAttachFrame =
                    moulds.get(i);
            MDImage mdImage = photoTemplateAttachFrame.getUserSelectImage();

            Bitmap selectBitmap = GlideUtil.loadImageAsBitmap(mdImage);
            float dx = photoTemplateAttachFrame.getPositionX();
            float dy = photoTemplateAttachFrame.getPositionY();
            float width = photoTemplateAttachFrame.getWidth();
            float height = photoTemplateAttachFrame.getHeight();

            Matrix matrix = TemplateUtils.getMatrixByString(photoTemplateAttachFrame.getMatrix());

            Bitmap compositeBitmap = compositePicture(width, height, null, selectBitmap, matrix);

            canvas.drawBitmap(compositeBitmap, dx, dy, paint);
        }
    }

    private static Bitmap compositePicture(float w, float h, Bitmap mouldBmp, Bitmap photoBmp, Matrix matrix) {
        Bitmap outputBitmap;
        outputBitmap = Bitmap.createBitmap((int) w, (int) h, Config.ARGB_4444);
        outputBitmap.eraseColor(Color.parseColor("#ffffff"));

        Canvas canvas = new Canvas(outputBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
//        paint.setColor(Color.parseColor("#BAB399"));
//        canvas.drawBitmap(mouldBmp, new Rect(0, 0, mouldBmp.getWidth(), mouldBmp.getHeight()), new RectF(0.0f, 0.0f, w, h), paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawColor(-1, PorterDuff.Mode.SRC_IN);
        canvas.drawBitmap(photoBmp, matrix, paint);
//        canvas.drawBitmap(photoBmp, 0, 0, paint);
        return outputBitmap;
    }

    private static void saveBitmapToSDCard(Bitmap saveBitmap, int index) {
        File saveFoler = new File(SAVEPATH);
        if (!saveFoler.exists()) {
            saveFoler.mkdirs();
        } else {
            // 删除之前所有的文件
//            for (File file : saveFoler.listFiles()) {
//                if (!file.isDirectory()) {
//                    file.delete();
//                }
//            }
        }
        String fileName = "page_" + index + ".jpg";

        try {
            FileOutputStream out = new FileOutputStream(new File(saveFoler, fileName));

            saveBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
