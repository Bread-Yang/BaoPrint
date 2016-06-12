package com.MDGround.HaiLanPrint.activity.payment;

import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityPaymentPreviewBinding;
import com.MDGround.HaiLanPrint.utils.NavUtils;

/**
 * Created by yoghourt on 5/23/16.
 */

public class PaymentSuccessActivity extends ToolbarActivity<ActivityPaymentPreviewBinding> {

    @Override
    protected int getContentLayout() {
        return R.layout.activity_payment_success;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    //region ACTION
    public void toMainActivityAction(View view) {
        NavUtils.toMainActivity(this);
    }
    //endregion
}
