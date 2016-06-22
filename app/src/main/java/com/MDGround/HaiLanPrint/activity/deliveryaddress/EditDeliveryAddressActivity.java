package com.MDGround.HaiLanPrint.activity.deliveryaddress;

import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityEditDeliveryAddressBinding;
import com.MDGround.HaiLanPrint.greendao.Location;
import com.MDGround.HaiLanPrint.models.DeliveryAddress;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.dialog.RegionPickerDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yoghourt on 5/24/16.
 */

public class EditDeliveryAddressActivity extends ToolbarActivity<ActivityEditDeliveryAddressBinding> {

    private DeliveryAddress mDeliveryAddress;

    private RegionPickerDialog mRegionPickerDialog;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_edit_delivery_address;
    }

    @Override
    protected void initData() {
        mRegionPickerDialog = new RegionPickerDialog(this);
        mDeliveryAddress = getIntent().getParcelableExtra(Constants.KEY_DELIVERY_ADDRESS);
        if (mDeliveryAddress != null) {
            mDataBinding.cetConsignee.setText(mDeliveryAddress.getReceiver());
            mDataBinding.cetContactNumber.setText(mDeliveryAddress.getPhone());
            mDataBinding.etDetailedAddress.setText(mDeliveryAddress.getStreet());
            Location province = MDGroundApplication.mDaoSession.getLocationDao().load(mDeliveryAddress.getProvinceID());
            Location city = MDGroundApplication.mDaoSession.getLocationDao().load(mDeliveryAddress.getCityID());
            Location county = MDGroundApplication.mDaoSession.getLocationDao().load(mDeliveryAddress.getDistrictID());
            mDataBinding.tvRegion.setText(province.getLocationName() + city.getLocationName() + county.getLocationName());
        } else {
            mDeliveryAddress = new DeliveryAddress();
            mDeliveryAddress.setCountryID(86);
            mDeliveryAddress.setProvinceID(110000);
            mDeliveryAddress.setCityID(110100);
            mDeliveryAddress.setDistrictID(110101);
            mDeliveryAddress.setUserID(MDGroundApplication.mInstance.getLoginUser().getUserID());
            mDataBinding.tvRegion.setText("北京北京市东城区");
            tvTitle.setText(R.string.add_address);
        }
    }

    @Override
    protected void setListener() {
        mRegionPickerDialog.setOnRegionSelectListener(new RegionPickerDialog.OnRegionSelectListener() {
            @Override
            public void onRegionSelect(Location province, Location city, Location county) {
                mDeliveryAddress.setProvinceID(province.getLocationID());
                mDeliveryAddress.setCityID(city.getLocationID());
                mDeliveryAddress.setDistrictID(county.getLocationID());
                mDataBinding.tvRegion.setText(province.getLocationName() + city.getLocationName() + county.getLocationName());
            }
        });
    }

    //region ACTION
    public void chooseRegionAction(View view) {
        mRegionPickerDialog.show();
    }

    public void saveAction(View view) {
        String consignee = mDataBinding.cetConsignee.getText().toString();
        if (StringUtil.isEmpty(consignee)) {
            ViewUtils.toast(R.string.input_consignee);
            return;
        }
        mDeliveryAddress.setReceiver(consignee);

        String phone = mDataBinding.cetContactNumber.getText().toString();
        if (StringUtil.isEmpty(phone)) {
            ViewUtils.toast(R.string.input_phone_number);
            return;
        }
        if (phone.length() != 11) {
            ViewUtils.toast(R.string.input_corrent_phone);
            return;
        }
        mDeliveryAddress.setPhone(phone);

        String detailedAddress = mDataBinding.etDetailedAddress.getText().toString();
        if (StringUtil.isEmpty(detailedAddress)) {
            ViewUtils.toast(R.string.input_detailed_address);
            return;
        }
        mDeliveryAddress.setStreet(detailedAddress);

        saveUserAddressRequest();
    }
    //endregion

    private void saveUserAddressRequest() {
        ViewUtils.loading(this);
        GlobalRestful.getInstance().SaveUserAddress(mDeliveryAddress, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ViewUtils.dismiss();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }
}
