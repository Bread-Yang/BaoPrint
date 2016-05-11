package com.MDGround.HaiLanPrint.glide;

import android.content.Context;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.mdground.yizhida.MedicalConstant;
import com.yongchun.library.model.LocalMedia;

import java.io.InputStream;

/**
 * Created by yoghourt on 3/21/16.
 */
public class MDGroundLoader implements ModelLoader<LocalMedia,InputStream> {

    private Context mContext;

    public MDGroundLoader(Context context) {
        this.mContext = context;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(LocalMedia model, int width, int height) {
        String url = model.getYiDeGuanImageURL();

        if (url.startsWith(MedicalConstant.IMAGE_URI_PREFIX)) {
            return new MDGroundFetcher(model, mContext);
        }
        return null;
    }

    public static class Factory implements ModelLoaderFactory<LocalMedia, InputStream> {
        // 缓存
        private final ModelCache<LocalMedia, LocalMedia> mModelCache = new ModelCache<>(500);

        @Override
        public ModelLoader<LocalMedia, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new MDGroundLoader(context);
        }

        @Override
        public void teardown() {

        }
    }
}
