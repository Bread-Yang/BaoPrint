package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.CompoundButton;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivitySettingBinding;
import com.MDGround.HaiLanPrint.utils.FileUtils;
import com.MDGround.HaiLanPrint.utils.NavUtils;

/**
 * Created by yoghourt on 5/30/16.
 */

public class SettingActivity extends ToolbarActivity<ActivitySettingBinding> {
   // 13924010907
    @Override
    protected int getContentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {
        mDataBinding.tvVersion.setText("V " + getVersion());
    }

    @Override
    protected void setListener() {
        mDataBinding.cbWifiUpdateEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
//			int versionCode = info.versionCode;
//			return version + "." + versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //region ACTION
    public void logoutAction(View view) {
        FileUtils.setObject(Constants.KEY_ALREADY_LOGIN_USER, null); // 清空之前的user
        NavUtils.toLoginActivity(this);
    }
    //endregion
}
