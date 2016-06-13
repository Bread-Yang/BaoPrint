package com.MDGround.HaiLanPrint.activity.engraving;

import com.MDGround.HaiLanPrint.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityEngravingMeasurementDescriptionBinding;
import com.MDGround.HaiLanPrint.enumobject.PhotoExplainTypeEnum;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.PhotoTypeExplain;
import com.bumptech.glide.Glide;

import static com.MDGround.HaiLanPrint.application.MDGroundApplication.mPhotoTypeExplainArrayList;

/**
 * Created by yoghourt on 5/23/16.
 */

public class EngravingMeasurementDescriptionActivity extends ToolbarActivity<ActivityEngravingMeasurementDescriptionBinding> {
    @Override
    protected int getContentLayout() {
        return R.layout.activity_engraving_measurement_description;
    }

    @Override
    protected void initData() {
        for (PhotoTypeExplain photoTypeExplain : mPhotoTypeExplainArrayList) {
            if (photoTypeExplain.getExplainType() == PhotoExplainTypeEnum.MeasurementDescription.value()
                    && photoTypeExplain.getTypeID() == ProductType.Engraving.value()) {

                MDImage mdImage = new MDImage();
                mdImage.setPhotoSID(photoTypeExplain.getPhotoSID());

//                GlideUtil.loadImageByPhotoSID(mDataBinding.ivBanner, photoTypeExplain.getPhotoSID());

                Glide.with(this).load(mdImage).into(mDataBinding.ivMeasurementDescription);
                break;
            }
        }
    }

    @Override
    protected void setListener() {

    }
}
