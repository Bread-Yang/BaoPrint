package com.MDGround.HaiLanPrint.activity.personalcenter;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityPersonalInformationBinding;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.User;
import com.MDGround.HaiLanPrint.utils.GlideUtil;

/**
 * Created by yoghourt on 5/30/16.
 */

public class PersonalInformationActivity extends ToolbarActivity<ActivityPersonalInformationBinding> {

    @Override
    protected int getContentLayout() {
        return R.layout.activity_personal_information;
    }

    @Override
    protected void initData() {
        User user = MDGroundApplication.mLoginUser;

        // 用户头像
        MDImage mdImage = new MDImage();
        mdImage.setPhotoID(user.getPhotoID());
        mdImage.setPhotoSID(user.getPhotoSID());
        GlideUtil.loadImageByMDImage(mDataBinding.civAvatar, mdImage);

        // 昵称
        mDataBinding.tvNickname.setText(user.getUserNickName());
        mDataBinding.tvPhone.setText(user.getPhone());
        mDataBinding.tvAccountName.setText(user.getUserName());
    }

    @Override
    protected void setListener() {

    }

    //region ACTION
    //endregion
}
