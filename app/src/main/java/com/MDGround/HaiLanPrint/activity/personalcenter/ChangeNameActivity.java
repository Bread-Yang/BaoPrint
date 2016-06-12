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
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.socks.library.KLog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC on 2016-06-13.
 */

public class ChangeNameActivity extends ToolbarActivity<ActivityChangeNameBinding> {
    @Override
    protected int getContentLayout() {
        return R.layout.activity_change_name;
    }

    @Override
    protected void initData() {
        tvRight.setText("完成");
        tvRight.setVisibility(View.VISIBLE);
        String userNickName = MDGroundApplication.mLoginUser.getUserNickName();
        mDataBinding.etName.setHint(userNickName);

    }

    @Override
    protected void setListener() {

        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newName = mDataBinding.etName.getText().toString();
                User user=MDGroundApplication.mLoginUser;
                user.setUserNickName(newName);
                if (!"".equals(newName)) {
                    GlobalRestful.getInstance().SaveUserInfo(user, new Callback<ResponseData>() {
                        @Override
                        public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                            KLog.e("kkkkk"+response.body());
                                if(ResponseCode.isSuccess(response.body())){
                                    MDGroundApplication.mLoginUser.setUserNickName(newName);
                                     finish();
                                }
                        }
                        @Override
                        public void onFailure(Call<ResponseData> call, Throwable t) {
                        }
                    });
                } else {
                    ViewUtils.toast("昵称不能为空");
                }

            }
        });
    }
}
