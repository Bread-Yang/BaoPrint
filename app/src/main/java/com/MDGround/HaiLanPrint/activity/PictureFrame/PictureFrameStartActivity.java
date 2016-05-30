package com.MDGround.HaiLanPrint.activity.pictureframe;

import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityPictureFrameStartBinding;
import com.MDGround.HaiLanPrint.utils.NavUtils;

/**
 * Created by yoghourt on 5/18/16.
 */
public class PictureFrameStartActivity extends ToolbarActivity<ActivityPictureFrameStartBinding> {
    @Override
    protected int getContentLayout() {
        return R.layout.activity_picture_frame_start;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    public void nextStepAction(View view) {
        NavUtils.toSelectAlbumActivity(view.getContext());
    }
}
