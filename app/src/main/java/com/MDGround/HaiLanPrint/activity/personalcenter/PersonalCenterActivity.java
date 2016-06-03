package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.content.Intent;
import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.coupon.MyCouponActivity;
import com.MDGround.HaiLanPrint.activity.orders.MyOrdersActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityPersonalCenterBinding;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.utils.GlideUtil;

/**
 * Created by yoghourt on 5/30/16.
 */

public class PersonalCenterActivity extends ToolbarActivity<ActivityPersonalCenterBinding> {

    @Override
    protected int getContentLayout() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void initData() {
        // 用户头像
        MDImage mdImage = new MDImage();
        mdImage.setPhotoID(MDGroundApplication.mLoginUser.getPhotoID());
        mdImage.setPhotoSID(MDGroundApplication.mLoginUser.getPhotoSID());
        GlideUtil.loadImageByMDImage(mDataBinding.civAvatar, mdImage);

        // 用户名
        mDataBinding.tvName.setText(MDGroundApplication.mLoginUser.getUserName());
    }

    @Override
    protected void setListener() {

    }

    //region ACTION
    public void toPersonalInformationAction(View view) {
        Intent intent = new Intent(this, PersonalInformationActivity.class);
        startActivity(intent);
    }

    public void toMyOrdersActivityAction(View view) {
        Intent intent = new Intent(this, MyOrdersActivity.class);
        startActivity(intent);
    }

    public void toMyCouponActivityAction(View view) {
        Intent intent = new Intent(this, MyCouponActivity.class);
        startActivity(intent);
    }

    public void toSettingActivityAction(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
    //endregion
}
