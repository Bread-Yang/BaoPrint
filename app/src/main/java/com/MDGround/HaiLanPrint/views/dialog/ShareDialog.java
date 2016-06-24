package com.MDGround.HaiLanPrint.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.databinding.DialogShareBinding;
import com.MDGround.HaiLanPrint.greendao.Location;
import com.MDGround.HaiLanPrint.utils.ShareUtils;

import cn.sharesdk.framework.Platform;

/**
 * Created by yoghourt on 5/25/16.
 */

public class ShareDialog extends Dialog {

    private Context mContext;

    private DialogShareBinding mDataBinding;

    private OnRegionSelectListener onRegionSelectListener;

    private Platform.ShareParams mShareParams;

    public ShareDialog(Context context) {
        super(context, R.style.customDialogStyle);
        mContext = context;
    }

    public ShareDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    //region INTERFACE
    public interface OnRegionSelectListener {
        public void onRegionSelect(Location province, Location city, Location county);
    }
    //endregion

    protected ShareDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_share, null, false);

        setContentView(mDataBinding.getRoot());

        Window window = getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT); // 填充满屏幕的宽度
        window.setWindowAnimations(R.style.action_sheet_animation); // 添加动画
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM; // 使dialog在底部显示
        window.setAttributes(wlp);

        setListener();
    }

    public void initImageShareParams(String imagePath) {
        mShareParams = ShareUtils.initImageShareParams(mContext, imagePath);
    }

    //分享链接
    public void initURLShareParams(String url) {
        mShareParams = ShareUtils.initURLShareParams(mContext, url);
    }

    private void setListener() {
        mDataBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mDataBinding.rltShareToWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToWechat();
            }
        });

        mDataBinding.rltShareToQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToQQ();
            }
        });

        mDataBinding.rltShareToWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToWeibo();
            }
        });
    }

    private void shareToWechat() {
        dismiss();
        ShareUtils.shareToWechat(mContext, mShareParams);
    }

    private void shareToQQ() {
        dismiss();
        ShareUtils.shareToQQ(mContext, mShareParams);
    }

    private void shareToWeibo() {
        dismiss();
        ShareUtils.shareToWeibo(mContext, mShareParams);
    }
}
