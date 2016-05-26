package com.MDGround.HaiLanPrint.views;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.databinding.DialogRegionPickerBinding;
import com.MDGround.HaiLanPrint.utils.ViewUtils;

/**
 * Created by yoghourt on 5/25/16.
 */

public class RegionPickerDialog extends Dialog {

    private DialogRegionPickerBinding mDataBinding;

    public RegionPickerDialog(Context context) {
        super(context, R.style.customDialog);
    }

    public RegionPickerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected RegionPickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_region_picker, null, false);

        setContentView(mDataBinding.getRoot());

        Window window = getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, ViewUtils.dp2px(300)); // 填充满屏幕的宽度
        window.setWindowAnimations(R.style.action_sheet_animation); // 添加动画
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM; // 使dialog在底部显示
        window.setAttributes(wlp);
    }
}
