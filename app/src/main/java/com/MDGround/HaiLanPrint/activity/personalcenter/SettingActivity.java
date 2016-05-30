package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivitySettingBinding;
import com.MDGround.HaiLanPrint.utils.NavUtils;

/**
 * Created by yoghourt on 5/30/16.
 */

public class SettingActivity extends ToolbarActivity<ActivitySettingBinding> {

    @Override
    protected int getContentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    public void logoutAction(View view) {
        NavUtils.toLoginActivity(this);
    }
}
