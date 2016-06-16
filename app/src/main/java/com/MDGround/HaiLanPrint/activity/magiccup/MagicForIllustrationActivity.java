package com.MDGround.HaiLanPrint.activity.magiccup;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityMagicIllustrationBinding;
import com.MDGround.HaiLanPrint.enumobject.PhotoExplainTypeEnum;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.PhotoTypeExplain;
import com.bumptech.glide.Glide;

import static com.MDGround.HaiLanPrint.application.MDGroundApplication.mPhotoTypeExplainArrayList;

/**
 * Created by yoghourt on 5/23/16.
 */

public class MagicForIllustrationActivity extends ToolbarActivity<ActivityMagicIllustrationBinding> {

    @Override
    protected int getContentLayout() {
        return R.layout.activity_magic_illustration;
    }

    @Override
    protected void initData() {
        for (PhotoTypeExplain photoTypeExplain : mPhotoTypeExplainArrayList) {
            if (photoTypeExplain.getExplainType() == PhotoExplainTypeEnum.IntroductionPage.value()
                    && photoTypeExplain.getTypeID() == ProductType.MagicCup.value()) {

                MDImage mdImage = new MDImage();
                mdImage.setPhotoSID(photoTypeExplain.getPhotoSID());

                Glide.with(this).load(mdImage).into(mDataBinding.ivMeasurementDescription);
                break;
            }
        }
    }

    @Override
    protected void setListener() {

    }
}
