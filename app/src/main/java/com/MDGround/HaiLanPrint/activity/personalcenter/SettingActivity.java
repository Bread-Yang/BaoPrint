package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.CompoundButton;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivitySettingBinding;
import com.MDGround.HaiLanPrint.utils.DeviceUtil;
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
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("是否确认要退出登录");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                  return;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeviceUtil.logoutUser();
                NavUtils.toLoginActivity(SettingActivity.this);
            }
        });
        builder.show();

    }
    //endregion
    //region ACTION
    public void toAboutUsActivity(View view){
        Intent intent=new Intent(this,AboutUsAcctivity.class);
        startActivity(intent);
    }
    //endregion
}
