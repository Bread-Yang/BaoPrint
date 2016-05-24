package com.MDGround.HaiLanPrint.activity.deliveryaddress;

import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityEditDeliveryAddressBinding;
import com.MDGround.HaiLanPrint.models.DeliveryAddress;

/**
 * Created by yoghourt on 5/24/16.
 */

public class EditDeliveryAddressActivity extends ToolbarActivity<ActivityEditDeliveryAddressBinding> {

    private DeliveryAddress mDeliveryAddress;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_edit_delivery_address;
    }

    @Override
    protected void initData() {
        mDeliveryAddress = getIntent().getParcelableExtra(Constants.KEY_DELIVERY_ADDRESS);

        if (mDeliveryAddress != null) {
            mDataBinding.cetConsignee.setText(mDeliveryAddress.getReceiver());
            mDataBinding.cetContactNumber.setText(mDeliveryAddress.getPhone());
            mDataBinding.etDetailedAddress.setText(mDeliveryAddress.getStreet());
        } else {
            mDeliveryAddress = new DeliveryAddress();

            tvTitle.setText(R.string.add_address);
        }
    }

    @Override
    protected void setListener() {

    }

    //region ACTION
    public void saveAction(View view) {

    }
    //endregion

    //region SERVER
    private void getUserAddressListRequest() {

    }
    //endregion


}
