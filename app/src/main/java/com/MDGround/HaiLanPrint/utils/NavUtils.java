package com.MDGround.HaiLanPrint.utils;

import android.content.Context;
import android.content.Intent;

import com.MDGround.HaiLanPrint.activity.cloudphotos.CloudDetailActivity;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.models.CloudImage;

/**
 * Created by yoghourt on 5/12/16.
 */
public class NavUtils {

    public static void toCloudDetailActivity(Context context, CloudImage cloudImage) {
        Intent intent = new Intent(context, CloudDetailActivity.class);
        intent.putExtra(Constants.KEY_CLOUD_IMAGE, cloudImage);
        context.startActivity(intent);
    }
}
