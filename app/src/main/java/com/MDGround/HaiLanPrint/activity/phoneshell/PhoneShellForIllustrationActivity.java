package com.MDGround.HaiLanPrint.activity.phoneshell;

import com.MDGround.HaiLanPrint.enumobject.ProductType;
import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityPhoneShellIllustrationBinding;
import com.MDGround.HaiLanPrint.enumobject.PhotoExplainTypeEnum;
import com.MDGround.HaiLanPrint.models.PhotoTypeExplain;
import com.MDGround.HaiLanPrint.utils.GlideUtil;

/**
 * Created by yoghourt on 5/23/16.
 */

public class PhoneShellForIllustrationActivity extends ToolbarActivity<ActivityPhoneShellIllustrationBinding> {
    @Override
    protected int getContentLayout() {
        return R.layout.activity_phone_shell_illustration;
    }

    @Override
    protected void initData() {
        for (PhotoTypeExplain photoTypeExplain : MDGroundApplication.sInstance.getPhotoTypeExplainArrayList()) {
            if (photoTypeExplain.getExplainType() == PhotoExplainTypeEnum.IntroductionPage.value()
                    && photoTypeExplain.getTypeID() == ProductType.PhoneShell.value()) {

                GlideUtil.loadImageByPhotoSIDWithDialog(mDataBinding.ivMeasurementDescription, photoTypeExplain.getPhotoSID());
                break;
            }
        }
    }

    @Override
    protected void setListener() {

    }
}
