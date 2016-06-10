package com.MDGround.HaiLanPrint.activity.payment;

import android.content.Intent;
import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.coupon.ChooseCouponActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityPaymentPreviewBinding;

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

    public void toChooseCouponActivityAction(View view) {
        Intent intent = new Intent(this, ChooseCouponActivity.class);
        startActivity(intent);
    }
}
