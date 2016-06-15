package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.content.Intent;
import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityEditorAddressBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
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
 * Created by PC on 2016-06-14.
 */
public class EditAddressActivity extends ToolbarActivity<ActivityEditorAddressBinding> {
    DeliveryAddress address;
    private RegionPickerDialog mRegionPickerDialog;
    boolean isUpdate = false;
    @Override
    protected int getContentLayout() {
        return R.layout.activity_editor_address;
    }
    @Override
    protected void initData() {
        Intent intent = getIntent();
        mRegionPickerDialog = new RegionPickerDialog(this);
        int fromWhere = intent.getIntExtra(ManageAddressActivity.FROM_HERE, 0);
        if (fromWhere == (ManageAddressActivity.FOR_UPATE)) {
            isUpdate = true;
            address = intent.getParcelableExtra(ManageAddressActivity.ADDRESS);
            mDataBinding.etContacts.setText(address.getPhone());
            mDataBinding.etReciever.setText(address.getReceiver());
            mDataBinding.etAddress.setText(address.getStreet());
            Location province = MDGroundApplication.mDaoSession.getLocationDao().load((long) address.getProvinceID());
            Location city = MDGroundApplication.mDaoSession.getLocationDao().load((long) address.getCityID());
            Location country = MDGroundApplication.mDaoSession.getLocationDao().load((long) address.getCountryID());
            mDataBinding.etRegio.setText(province.getLocationName() + "" + city.getLocationName() + "" + country.getLocationName());

        } else {
            isUpdate = false;
            tvTitle.setText("新增地址");
            address = new DeliveryAddress();
        }

    }

    @Override
    protected void setListener() {
        mDataBinding.tvAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address.setReceiver(mDataBinding.etReciever.getText().toString());
                address.setUserID(MDGroundApplication.mLoginUser.getUserID());
                if (StringUtil.isEmpty(mDataBinding.etContacts.getText().toString())) {
                    ViewUtils.toast(R.string.input_phone_number);
                    return;
                }
                if (mDataBinding.etContacts.getText().toString().length() != 11) {
                    ViewUtils.toast(R.string.input_corrent_phone);
                    return;
                }
                address.setPhone(mDataBinding.etContacts.getText().toString());
                if (StringUtil.isEmpty(mDataBinding.etAddress.getText().toString())) {
                    ViewUtils.toast(R.string.input_detailed_address);
                    return;
                }
                address.setStreet(mDataBinding.etAddress.getText().toString());
                addOrdeleteAddress(address);
            }
        });
        mRegionPickerDialog.setOnRegionSelectListener(new RegionPickerDialog.OnRegionSelectListener() {
            @Override
            public void onRegionSelect(Location province, final Location city, final Location county) {
                address.setProvinceID(Integer.parseInt(String.valueOf(province.getLocationID())));
                address.setCityID(Integer.parseInt(String.valueOf(city.getLocationID())));
                address.setCountryID(Integer.parseInt(String.valueOf(county.getLocationID())));
                mDataBinding.etRegio.setText(province.getLocationName() + "" + city.getLocationName() + " " + county.getLocationName());
            }

        });

    }

    //region SERVER
    public void addOrdeleteAddress(final DeliveryAddress address) {
        GlobalRestful.getInstance().SaveUserAddress(address, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (ResponseCode.isSuccess(response.body())) {
                    if (isUpdate) {
                        ViewUtils.toast("修改成功");
                    } else {
                        ViewUtils.toast("添加成功");
                    }
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
            }
        });

    }

    //endgion
    //region ACTION
    public void selectRegio(View view) {
        mRegionPickerDialog.show();
    }
    //endregion
}
