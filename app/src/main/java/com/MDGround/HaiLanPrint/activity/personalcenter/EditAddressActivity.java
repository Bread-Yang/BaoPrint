package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.content.Intent;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.databinding.ActivityEditorAddressBinding;
import com.MDGround.HaiLanPrint.models.DeliveryAddress;

/**
 * Created by PC on 2016-06-14.
 */
public class EditAddressActivity extends ToolbarActivity<ActivityEditorAddressBinding>{
    @Override
    protected int getContentLayout() {
        return R.layout.activity_editor_address;
    }

    @Override
    protected void initData() {
        Intent intent=getIntent();
        String fromWhere=intent.getStringExtra(ManageAddressActivity.FROM_HERE);
        if(fromWhere.equals(ManageAddressActivity.FOR_UPATE)){
            DeliveryAddress address=intent.getParcelableExtra(ManageAddressActivity.ADDRESS);
           // mDataBinding.etAddress.setText(address.getCityID());
            mDataBinding.etContacts.setText(address.getPhone());
            mDataBinding.etReciever.setText(address.getReceiver());
           mDataBinding.etRegio.setText(address.getStreet());
//            DeliveryAddress address=bundle.get()
        }else{

        }

    }

    @Override
    protected void setListener() {

    }
}
