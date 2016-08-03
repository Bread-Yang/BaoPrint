package com.MDGround.HaiLanPrint.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.glide.MDGroundLoader;
import com.MDGround.HaiLanPrint.glide.transformation.RotateTransformation;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.SimpleResource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.socks.library.KLog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by yoghourt on 5/18/16.
 */
public class GlideUtil {

    public static void loadImageByMDImage(ImageView imageView, MDImage mdImage, boolean showPlaceHolder) {

        if (mdImage != null) {
//            File cacheFile = Glide.getPhotoCacheDir(MDGroundApplication.sInstance);
//            KLog.e(" cacheFile : " + cacheFile.getPath());
//            KLog.e("cacheFolder size : " + getFileSize(cacheFile));

            Glide.with(MDGroundApplication.sInstance)
                    .load(mdImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(showPlaceHolder ? R.drawable.layerlist_image_placeholder : 0)
                    .error(R.drawable.layerlist_image_placeholder)
                    .dontAnimate()
                    .into(imageView);

        }
    }

    public static void loadImageRotated(ImageView imageView, MDImage mdImage, float rotateAngle) {
        if (mdImage != null) {
            Glide.with(MDGroundApplication.sInstance)
                    .load(mdImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.layerlist_image_placeholder)
                    .dontAnimate()
                    .transform(new RotateTransformation(imageView.getContext(), rotateAngle))
                    .into(imageView);
        }
    }

    public static void loadImageByMDImageWithDialog(final ImageView imageView, MDImage mdImage) {
        ViewUtils.loading(imageView.getContext());
        Glide.with(imageView.getContext())
                .load(mdImage)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        imageView.setImageBitmap(bitmap);
                        ViewUtils.dismiss();
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        ViewUtils.dismiss();
                    }
                });
    }

    public static void loadImageByPhotoSID(ImageView imageView, int photoSID, boolean showPlaceHolder) {
        MDImage mdImage = new MDImage();
        mdImage.setPhotoSID(photoSID);
        GlideUtil.loadImageByMDImage(imageView, mdImage, showPlaceHolder);
    }

    public static void loadImageByPhotoSIDWithDialog(ImageView imageView, int photoSID) {
        MDImage mdImage = new MDImage();
        mdImage.setPhotoSID(photoSID);
        GlideUtil.loadImageByMDImageWithDialog(imageView, mdImage);
    }

    public static void getImageOriginalSize(Context context, MDImage mdImage,
                                            SimpleTarget<Options> simpleTarget) {
        GenericRequestBuilder<MDImage, InputStream, Options, Options> SIZE_REQUEST = Glide // cache for effectiveness (re-use in lists for example) and readability at usage
                .with(context)
                .using(new MDGroundLoader(context), InputStream.class)
                .from(MDImage.class)
                .as(Options.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new BitmapSizeDecoder())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE);

        SIZE_REQUEST
                .load(mdImage)
                .into(new SimpleTarget<Options>() { // Target.SIZE_ORIGINAL is hidden in ctor
                    @Override
                    public void onResourceReady(Options resource, GlideAnimation glideAnimation) {
                        KLog.e("图片的original size是: ", String.format(Locale.ROOT, "%dx%d", resource.outWidth, resource.outHeight));
                    }
                })
        ;
    }

    public static void loadImageAsBitmap(MDImage mdImage, Target target) {
        Glide.with(MDGroundApplication.sInstance)
                .load(mdImage)
                .asBitmap()
                .atMost()
                .override(300, 300) // 图片如果超过300 * 300, 则压缩
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(target);
    }

    public static Bitmap loadImageAsBitmap(MDImage mdImage) {
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(MDGroundApplication.sInstance)
                    .load(mdImage)
                    .asBitmap()
                    .atMost()
                    .override(300, 300) // 图片如果超过300 * 300, 则压缩
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static long getFileSize(final File file) {
        if (file == null || !file.exists())
            return 0;
        if (!file.isDirectory())
            return file.length();
        final List<File> dirs = new LinkedList<File>();
        dirs.add(file);
        long result = 0;
        while (!dirs.isEmpty()) {
            final File dir = dirs.remove(0);
            if (!dir.exists())
                continue;
            final File[] listFiles = dir.listFiles();
            if (listFiles == null || listFiles.length == 0)
                continue;
            for (final File child : listFiles) {
                result += child.length();
                if (child.isDirectory())
                    dirs.add(child);
            }
        }
        return result;
    }

    static class BitmapSizeDecoder implements ResourceDecoder<File, Options> {
        @Override
        public Resource<Options> decode(File source, int width, int height) throws IOException {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            KLog.e("source.getAbsolutePath() : " + source.getAbsolutePath());
            BitmapFactory.decodeFile(source.getAbsolutePath(), options);
            return new SimpleResource<>(options);
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

    static class Size {
        int width, height;
    }

    static class BitmapSizeTranscoder implements ResourceTranscoder<Bitmap, Size> {
        @Override
        public Resource<Size> transcode(Resource<Bitmap> toTranscode) {
            Bitmap bitmap = toTranscode.get();
            Size size = new Size();
            size.width = bitmap.getWidth();
            size.height = bitmap.getHeight();
            return new SimpleResource<>(size);
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }
}
