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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoghourt on 8/2/16.
 */
public class CreateImageUtil {

    public static final String SAVEPATH = Tools.getAppPath() + File.separator + "work";

    public interface onCreateAllComposteImageCompleteListner {
        void onComplete(List<String> allCompositeImageLocalPathList);
    }

    // 有定位块的模版图片生成
    public static void createAllPageHasModules(final onCreateAllComposteImageCompleteListner listner) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> allCompositeImageLocalPathList = new ArrayList<String>();
                for (int pageIndex = 0; pageIndex < SelectImageUtils.sTemplateImage.size(); pageIndex++) {
                    Bitmap bitmap = createPageBitmapHasModules(pageIndex);

                    String filePath = saveBitmapToSDCard(bitmap, pageIndex);
                    allCompositeImageLocalPathList.add(filePath);
                }

                if (listner != null) {
                    listner.onComplete(allCompositeImageLocalPathList);
                }
            }
        }).start();
    }

    // 没有定位块的模版图片生成
    public static void createAllPageWithoutModules(final onCreateAllComposteImageCompleteListner listner) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> allCompositeImageLocalPathList = new ArrayList<String>();
                for (int pageIndex = 0; pageIndex < SelectImageUtils.sTemplateImage.size(); pageIndex++) {
                    Bitmap bitmap = createPageBitmapWhthoutModules(pageIndex);

                    String filePath = saveBitmapToSDCard(bitmap, pageIndex);
                    allCompositeImageLocalPathList.add(filePath);
                }

                if (listner != null) {
                    listner.onComplete(allCompositeImageLocalPathList);
                }
            }
        }).start();
    }

    public static Bitmap createPageBitmapWhthoutModules(int pageIndex) {
        MDImage templateImage = SelectImageUtils.sTemplateImage.get(pageIndex);

        List<PhotoTemplateAttachFrame> photoTemplateAttachFrameList = templateImage.getPhotoTemplateAttachFrameList();

        Bitmap backgroundBitmap = GlideUtil.loadImageAsBitmap(templateImage);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);

        Bitmap bitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(), backgroundBitmap.getHeight(),
                Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        float rateOfEditWidth = TemplateUtils.getRateOfEditWidthOnAndroid(backgroundBitmap);

        // 用户编辑模块绘制
        MDImage userSelectImage = SelectImageUtils.sAlreadySelectImage.get(pageIndex);

        Bitmap selectBitmap = GlideUtil.loadImageAsBitmap(userSelectImage);

        Matrix matrix = TemplateUtils.getMatrixByString(templateImage.getWorkPhoto().getMatrix());

        Bitmap compositeBitmap = compositePicture(backgroundBitmap.getWidth(), backgroundBitmap.getHeight(),
                null, selectBitmap, matrix);

        canvas.drawBitmap(compositeBitmap, 0, 0, paint);

        // 背景图绘制
        canvas.drawBitmap(backgroundBitmap, 0, 0, paint);

        return bitmap;
    }

    public static Bitmap createPageBitmapHasModules(int pageIndex) {
        MDImage mdImage = SelectImageUtils.sTemplateImage.get(pageIndex);

        List<PhotoTemplateAttachFrame> photoTemplateAttachFrameList = mdImage.getPhotoTemplateAttachFrameList();

        Bitmap backgroundBitmap = GlideUtil.loadImageAsBitmap(mdImage);

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
        createMould(pageIndex, canvas, paint, photoTemplateAttachFrameList, 1.0f, 1.0f, rateOfEditWidth);

        return bitmap;
    }

    private static void createMould(int pageIndex, final Canvas canvas,
                                    final Paint paint, List<PhotoTemplateAttachFrame> moulds,
                                    float rate, float r, float rateOfEditWidth) {
        for (int moduleIndex = 0; moduleIndex < moulds.size(); moduleIndex++) {
            final PhotoTemplateAttachFrame photoTemplateAttachFrame =
                    moulds.get(moduleIndex);
            MDImage mdImage = SelectImageUtils.getMdImageByPageIndexAndModuleIndex(pageIndex, moduleIndex);

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

    private static Bitmap compositePicture(float width, float height, Bitmap mouldBmp, Bitmap photoBmp, Matrix matrix) {
        Bitmap outputBitmap;
        outputBitmap = Bitmap.createBitmap((int) width, (int) height, Config.ARGB_4444);
        outputBitmap.eraseColor(Color.parseColor("#ffffff"));

        int photoBmpWidth = photoBmp.getWidth();
        int photoBmpHeight = photoBmp.getHeight();

        float scale = width / ((float) photoBmpWidth) > height / ((float) photoBmpHeight)
                ? width / ((float) photoBmpWidth)
                : height / ((float) photoBmpHeight);

        if (scale != 0.0f) {
            Bitmap scalePhotoBmp;
            Matrix photoMatrix = new Matrix();
            photoMatrix.setScale(scale, scale);
            try {
                scalePhotoBmp = Bitmap.createBitmap(photoBmp, 0, 0, photoBmpWidth, photoBmpHeight, photoMatrix, true);
            } catch (OutOfMemoryError e) {
//                    BitMapUtil.oom();
                try {
                    scalePhotoBmp = Bitmap.createBitmap(photoBmp, 0, 0, photoBmpWidth, photoBmpHeight, photoMatrix, true);
                } catch (OutOfMemoryError e2) {
//                        BitMapUtil.oom();
                    scalePhotoBmp = Bitmap.createBitmap(photoBmp, 0, 0, photoBmpWidth, photoBmpHeight, photoMatrix, true);
                }
            }
            photoBmp = scalePhotoBmp;

            float photoBitmapWidth = (float) photoBmp.getWidth();
            float photoBitmapHeight = (float) photoBmp.getHeight();
            float dx = (width - photoBitmapWidth) / 2.0f;
            float dy = (height - photoBitmapHeight) / 2.0f;
//            matrix.preTranslate(dx, dy);
        }

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

    private static String saveBitmapToSDCard(Bitmap saveBitmap, int index) {
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
        String filePath = SAVEPATH + File.separator + fileName;

        File file = new File(filePath);

        try {
            FileOutputStream out = new FileOutputStream(file);

            saveBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }
}
