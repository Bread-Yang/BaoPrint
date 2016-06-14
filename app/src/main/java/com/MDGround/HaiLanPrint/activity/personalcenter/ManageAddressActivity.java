package com.MDGround.HaiLanPrint.activity.personalcenter;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityManageAddressBinding;

/**
 * Created by PC on 2016-06-14.
 */
public class ManageAddressActivity extends ToolbarActivity<ActivityManageAddressBinding> {
    @Override
    protected int getContentLayout() {
        return R.layout.activity_manage_address;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }
  //  intent.putExtra(Constants.KEY_DELIVERY_ADDRESS, mDeliveryAddress);
}
