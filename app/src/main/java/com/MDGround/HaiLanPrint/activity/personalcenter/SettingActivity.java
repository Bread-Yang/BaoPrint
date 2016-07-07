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
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivitySettingBinding;
import com.MDGround.HaiLanPrint.utils.DeviceUtil;
import com.MDGround.HaiLanPrint.utils.FileUtils;
import com.MDGround.HaiLanPrint.utils.NavUtils;
import com.MDGround.HaiLanPrint.utils.PreferenceUtils;
import com.MDGround.HaiLanPrint.views.dialog.NotifyDialog;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by yoghourt on 5/30/16.
 */

public class SettingActivity extends ToolbarActivity<ActivitySettingBinding> {

    private NotifyDialog mNotifyDialog;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {
        boolean isOnlyWifiUpdate = PreferenceUtils.getPrefBoolean(Constants.KEY_ONLY_WIFI_UPDATE, false);
        mDataBinding.cbOnlyWifiUpdate.setChecked(isOnlyWifiUpdate);

        mDataBinding.tvVersion.setText("V " + getVersion());
        mDataBinding.tvCache.setText(getCacheSize());
    }

    @Override
    protected void setListener() {
        mDataBinding.cbOnlyWifiUpdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtils.setPrefBoolean(Constants.KEY_ONLY_WIFI_UPDATE, isChecked);
            }
        });
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    private String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getCacheSize() {
        String applicationDirectory = getApplicationInfo().dataDir;
        String glideCacheDirectory = applicationDirectory + File.separator + "cache" + File.separator + Constants.GLIDE_DISK_CACHE_FILE_NAME;
        File file = new File(glideCacheDirectory);
        if (file != null && file.exists()) {
            long fileSizeInBytes = FileUtils.getFileSize(file);
            long fileSizeInKB = fileSizeInBytes / 1024;
            long fileSizeInMB = fileSizeInKB / 1024;
            DecimalFormat form = new DecimalFormat("0.00");
            return form.format(fileSizeInMB) + "M";
        } else {
            return "0M";
        }
    }

    //region ACTION
    public void clearCacheAction(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_clear_cache);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(SettingActivity.this).clearDiskCache();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDataBinding.tvCache.setText(getCacheSize());
                            }
                        });
                    }
                }).start();
            }
        });
        builder.show();

//        if (mNotifyDialog == null) {
//            mNotifyDialog = new NotifyDialog(this);
//            mNotifyDialog.setOnSureClickListener(new NotifyDialog.OnSureClickListener() {
//                @Override
//                public void onSureClick() {
//                    mNotifyDialog.dismiss();
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Glide.get(SettingActivity.this).clearDiskCache();
//
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mDataBinding.tvCache.setText(getCacheSize());
//                                }
//                            });
//                        }
//                    }).start();
//                }
//            });
//            mNotifyDialog.show();
//
//            mNotifyDialog.setTvContent(getString(R.string.confirm_clear_cache));
//        }
    }

    public void toAboutUsActivityAction(View view) {
        Intent intent = new Intent(this, AboutUsActivity.class);
        startActivity(intent);
    }

    public void logoutAction(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_log_out);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeviceUtil.logoutUser();
                NavUtils.toLoginActivity(SettingActivity.this);
            }
        });
        builder.show();
    }
    //endregion
}
