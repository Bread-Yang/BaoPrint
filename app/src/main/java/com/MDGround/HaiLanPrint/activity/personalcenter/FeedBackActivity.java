package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.view.View;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.databinding.ActivityFeedBackBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.ViewUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PC on 2016-06-15.
 */

public class FeedBackActivity extends ToolbarActivity<ActivityFeedBackBinding> {
    @Override
    protected int getContentLayout() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    //region ACTION
    public void toSumbit(View view) {
        String suggestion = mDataBinding.etContext.getText().toString();
        String phone = MDGroundApplication.mInstance.getLoginUser().getPhone();
        ViewUtils.loading(this);
        GlobalRestful.getInstance().SaveUserSuggestion(phone, suggestion, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (ResponseCode.isSuccess(response.body())) {
                    ViewUtils.dismiss();
                    ViewUtils.toast(getString(R.string.submit_success));
                    finish();
                }

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                ViewUtils.toast(getString(R.string.sumbit_fail));
            }
        });
    }
    //endregion

}
