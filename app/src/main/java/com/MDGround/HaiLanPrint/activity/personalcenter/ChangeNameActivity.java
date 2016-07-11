package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityChangeNameBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.User;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.DateUtils;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.MDGround.HaiLanPrint.views.MaxLengthWatcher;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC on 2016-06-13.
 */
public class ChangeNameActivity extends ToolbarActivity<ActivityChangeNameBinding> {

    private User mLoginUser;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_change_name;
    }

    @Override
    protected void initData() {
        mLoginUser = MDGroundApplication.sInstance.getLoginUser();

        tvRight.setText(R.string.finish);
        tvRight.setVisibility(View.VISIBLE);
        String userNickName = mLoginUser.getUserNickName();
        mDataBinding.etName.setText(userNickName);
        mDataBinding.etName.setSelection(userNickName.length());
        mDataBinding.etName.addTextChangedListener(new MaxLengthWatcher(16, mDataBinding.etName, ChangeNameActivity.this));
    }

    @Override
    protected void setListener() {
        //region ACTION
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newName = mDataBinding.etName.getText().toString();
                if (!"".equals(newName)) {
                    if (newName.equals(mLoginUser.getUserNickName())) {
                        finish();
                    } else {
                        mLoginUser.setUserNickName(newName);
                        Date date = new Date(System.currentTimeMillis());
                        String updateDate = DateUtils.getServerDateStringByDate(date);
                        mLoginUser.setUpdatedTime(updateDate);

                        GlobalRestful.getInstance().SaveUserInfo(mLoginUser, new Callback<ResponseData>() {
                            @Override
                            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                                if (ResponseCode.isSuccess(response.body())) {
                                    MDGroundApplication.sInstance.setLoginUser(mLoginUser);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseData> call, Throwable t) {
                            }
                        });
                    }
                } else {
                    ViewUtils.toast(getString(R.string.nickname_not_null));
                }
            }
        });

    }

    //region  ACTION
    public void deleteInputText(View view) {
        mDataBinding.etName.setText("");
    }
    //endregion
}
