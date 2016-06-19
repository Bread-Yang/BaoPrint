package com.MDGround.HaiLanPrint.activity.pictureframe;

import android.content.Intent;
import android.view.View;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.template.SelectTemplateActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityPictureFrameStartBinding;
import com.MDGround.HaiLanPrint.enumobject.PhotoExplainTypeEnum;
import com.MDGround.HaiLanPrint.models.PhotoTypeExplain;
import com.MDGround.HaiLanPrint.utils.GlideUtil;

import static com.MDGround.HaiLanPrint.application.MDGroundApplication.mPhotoTypeExplainArrayList;

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
        for (PhotoTypeExplain photoTypeExplain : mPhotoTypeExplainArrayList) {
            if (photoTypeExplain.getExplainType() == PhotoExplainTypeEnum.IntroductionPage.value()
                    && photoTypeExplain.getTypeID() == ProductType.PictureFrame.value()) {
                GlideUtil.loadImageByPhotoSIDWithDialog(mDataBinding.ivIntroductionPage, photoTypeExplain.getPhotoSID());
                break;
            }
        }
    }

    @Override
    protected void setListener() {

    }

    public void nextStepAction(View view) {
        Intent intent = new Intent(this, SelectTemplateActivity.class);
        startActivity(intent);
    }
}
