package com.MDGround.HaiLanPrint.activity.payment;

import android.content.Intent;
import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.activity.coupon.ChooseCouponActivity;
import com.MDGround.HaiLanPrint.activity.deliveryaddress.ChooseDeliveryAddressActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityPaymentPreviewBinding;
import com.MDGround.HaiLanPrint.greendao.Location;
import com.MDGround.HaiLanPrint.models.DeliveryAddress;
import com.MDGround.HaiLanPrint.utils.NavUtils;

/**
 * Created by yoghourt on 5/23/16.
 */

public class PaymentPreviewActivity extends ToolbarActivity<ActivityPaymentPreviewBinding> {

    private final int REQEUST_CODE_SELECT_DELIVERY_ADDRESS = 0x11;
    private final int REQEUST_CODE_SELECT_COUPON = 0x12;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_payment_preview;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.toMainActivity(PaymentPreviewActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQEUST_CODE_SELECT_DELIVERY_ADDRESS:
                    DeliveryAddress deliveryAddress = data.getParcelableExtra(Constants.KEY_DELIVERY_ADDRESS);

                    if (deliveryAddress != null) {
                        mDataBinding.tvName.setText(deliveryAddress.getReceiver());
                        mDataBinding.tvPhone.setText(deliveryAddress.getPhone());

                        Location province = MDGroundApplication.mDaoSession.getLocationDao().load(deliveryAddress.getProvinceID());
                        Location city = MDGroundApplication.mDaoSession.getLocationDao().load(deliveryAddress.getCityID());
                        Location county = MDGroundApplication.mDaoSession.getLocationDao().load(deliveryAddress.getDistrictID());

                        mDataBinding.tvAddress.setText(province.getLocationName() + city.getLocationName() + county.getLocationName() + deliveryAddress.getStreet());

                        mDataBinding.tvChooseFirst.setVisibility(View.GONE);
                        mDataBinding.tvName.setVisibility(View.VISIBLE);
                        mDataBinding.tvPhone.setVisibility(View.VISIBLE);
                        mDataBinding.tvAddress.setVisibility(View.VISIBLE);
                        mDataBinding.ivDeliveryAddress.setVisibility(View.VISIBLE);
                    }

                    break;
                case REQEUST_CODE_SELECT_COUPON:

                    break;
            }
        }
    }

    //region ACTION
    public void toDeliveryAddressListActivityAction(View view) {
        Intent intent = new Intent(this, ChooseDeliveryAddressActivity.class);
        startActivityForResult(intent, REQEUST_CODE_SELECT_DELIVERY_ADDRESS);
    }

    public void toChooseCouponActivityAction(View view) {
        Intent intent = new Intent(this, ChooseCouponActivity.class);
        startActivity(intent);
    }
    //endregion
}
