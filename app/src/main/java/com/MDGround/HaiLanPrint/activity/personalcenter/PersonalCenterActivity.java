package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.content.Intent;
import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.coupon.MyCouponActivity;
import com.MDGround.HaiLanPrint.activity.messagecenter.MessageCenterActivity;
import com.MDGround.HaiLanPrint.activity.myworks.MyWorksActivity;
import com.MDGround.HaiLanPrint.activity.orders.MyOrdersActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityPersonalCenterBinding;
import com.MDGround.HaiLanPrint.greendao.Location;
import com.MDGround.HaiLanPrint.models.MDImage;
import com.MDGround.HaiLanPrint.models.User;
import com.MDGround.HaiLanPrint.utils.GlideUtil;
import com.MDGround.HaiLanPrint.utils.StringUtil;

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
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = MDGroundApplication.sInstance.getLoginUser();
        MDImage mdImage = new MDImage();
        mdImage.setPhotoID(user.getPhotoID());
        mdImage.setPhotoSID(user.getPhotoSID());
        GlideUtil.loadImageByMDImage(mDataBinding.civAvatar, mdImage, false);
        mDataBinding.tvName.setText(user.getUserNickName());
        if (!StringUtil.isEmpty(user.getPhone())) {
            mDataBinding.tvPhoneNum.setText(user.getPhone());
        } else {
            mDataBinding.tvPhoneNum.setText(R.string.not_bound);
        }
        Location city = MDGroundApplication.sDaoSession.getLocationDao().load((long) user.getCityID());
        Location county = MDGroundApplication.sDaoSession.getLocationDao().load((long) user.getDistrictID());
        if (city != null && county != null) {
            mDataBinding.tvCity.setText(city.getLocationName() + " " + county.getLocationName());
        } else {
            mDataBinding.tvCity.setText(R.string.not_filled);
        }
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

    //设置
    public void toSettingActivityAction(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    //积分查询
    public void tOMyCreditActivity(View v) {
        Intent intent = new Intent(this, MyCreditActivity.class);
        startActivity(intent);
    }

    //我的作品
    public void toMyWorksActivity(View view) {
        Intent intent = new Intent(this, MyWorksActivity.class);
        startActivity(intent);
    }

    //去消息中心
    public void toMessageCenter(View view) {
        Intent intent = new Intent(this, MessageCenterActivity.class);
        startActivity(intent);
    }
    //endregion

}
