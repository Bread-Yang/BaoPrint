package com.MDGround.HaiLanPrint.activity.personalcenter;

import android.text.Editable;
import android.text.TextWatcher;
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
        mDataBinding.tvCanInputCharactor.setText(getString(R.string.can_input_charactor, 200));
    }

    @Override
    protected void setListener() {
        mDataBinding.etContext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int charactorNum = 200 - s.toString().length();
                if (charactorNum < 0) {
                    charactorNum = 0;
                }
                mDataBinding.tvCanInputCharactor.setText(getString(R.string.can_input_charactor, charactorNum));
            }
        });
    }

    //region ACTION
    public void toSumbit(View view) {

        String suggestion = mDataBinding.etContext.getText().toString();
        String phone = MDGroundApplication.sInstance.getLoginUser().getPhone();
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
