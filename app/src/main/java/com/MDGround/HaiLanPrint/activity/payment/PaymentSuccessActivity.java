package com.MDGround.HaiLanPrint.activity.payment;

import android.content.Intent;
import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.orders.MyOrdersActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityPaymentSuccessBinding;
import com.MDGround.HaiLanPrint.models.OrderInfo;
import com.MDGround.HaiLanPrint.utils.NavUtils;
import com.MDGround.HaiLanPrint.utils.StringUtil;

/**
 * Created by yoghourt on 5/23/16.
 */

public class PaymentSuccessActivity extends ToolbarActivity<ActivityPaymentSuccessBinding> {

    @Override
    protected int getContentLayout() {
        return R.layout.activity_payment_success;
    }

    @Override
    protected void initData() {
        OrderInfo orderInfo = getIntent().getParcelableExtra(Constants.KEY_ORDER_INFO);

        mDataBinding.tvPaidAmount.setText(getString(R.string.yuan_amount, StringUtil.toYuanWithoutUnit(orderInfo.getTotalFeeReal())));
        mDataBinding.tvOrderNum.setText(getString(R.string.order_number, orderInfo.getOrderNo()));

        if (MDGroundApplication.mChoosedMeasurement != null) {
            switch (MDGroundApplication.mChoosedProductType) {
                case PrintPhoto:
                case PictureFrame:
                case Engraving:
                    mDataBinding.lltShare.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    protected void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.toMainActivity(PaymentSuccessActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        NavUtils.toMainActivity(PaymentSuccessActivity.this);
    }

    //region ACTION
    public void toMainActivityAction(View view) {
        NavUtils.toMainActivity(this);
    }

    public void toMyOrdersActivityAction(View view) {
        Intent intent = new Intent(this, MyOrdersActivity.class);
        intent.putExtra(Constants.KEY_FROM_PAYMENT_SUCCESS, true);
        startActivity(intent);
    }
    //endregion
}
