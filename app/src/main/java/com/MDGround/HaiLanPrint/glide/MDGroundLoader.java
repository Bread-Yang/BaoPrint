package com.MDGround.HaiLanPrint.glide;

import android.content.Context;

import com.MDGround.HaiLanPrint.models.CloudImage;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;

import java.io.InputStream;

/**
 * Created by yoghourt on 3/21/16.
 */
public class MDGroundLoader implements ModelLoader<CloudImage, InputStream> {

    private Context mContext;

    public MDGroundLoader(Context context) {
        this.mContext = context;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(CloudImage model, int width, int height) {
        return new MDGroundFetcher(model, mContext);
    }

    public static class Factory implements ModelLoaderFactory<CloudImage, InputStream> {
        // 缓存
        private final ModelCache<CloudImage, CloudImage> mModelCache = new ModelCache<>(500);

        @Override
        public ModelLoader<CloudImage, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new MDGroundLoader(context);
        }

        @Override
        public void teardown() {

        }
    }
}
