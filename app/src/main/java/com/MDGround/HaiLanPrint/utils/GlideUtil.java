package com.MDGround.HaiLanPrint.utils;

import android.widget.ImageView;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.socks.library.KLog;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yoghourt on 5/18/16.
 */
public class GlideUtil {

    public static void loadImageByMDImage(ImageView imageView, MDImage mdImage) {
//        if (mdImage.getImageLocalPath() != null && mdImage.getImageLocalPath().contains("storage")) {
//            // 加载本地图片
//            Glide.with(MDGroundApplication.mInstance)
//                    .load(new File(mdImage.getImageLocalPath()))
//                    .centerCrop()
//                    .placeholder(R.drawable.layerlist_image_placeholder)
//                    .error(R.drawable.layerlist_image_placeholder)
//                    .dontAnimate()
//                    .into(imageView);
//        } else {
//            // 加载网络图片
//            Glide.with(MDGroundApplication.mInstance)
//                    .load(mdImage)
//                    .centerCrop()
//                    .placeholder(R.drawable.layerlist_image_placeholder)
//                    .error(R.drawable.layerlist_image_placeholder)
//                    .dontAnimate()
//                    .into(imageView);
//        }

        if (mdImage != null) {
            File cacheFile = Glide.getPhotoCacheDir(MDGroundApplication.mInstance);
            KLog.e(" cacheFile : " + cacheFile.getPath());
            KLog.e("cacheFolder size : " + getFileSize(cacheFile));

            Glide.with(MDGroundApplication.mInstance)
                    .load(mdImage)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.layerlist_image_placeholder)
                    .error(R.drawable.layerlist_image_placeholder)
                    .dontAnimate()
                    .into(imageView);

        }
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
}
