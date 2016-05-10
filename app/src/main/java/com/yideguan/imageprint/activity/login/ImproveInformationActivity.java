package com.yideguan.imageprint.activity.login;

import android.view.View;
import android.widget.Toast;

import com.yideguan.imageprint.R;
import com.yideguan.imageprint.activity.base.ToolbarActivity;
import com.yideguan.imageprint.constants.Constants;
import com.yideguan.imageprint.databinding.ActivityImproveInformationBinding;
import com.yideguan.imageprint.enumobject.restfuls.ResponseCode;
import com.yideguan.imageprint.models.User;
import com.yideguan.imageprint.restfuls.GlobalRestful;
import com.yideguan.imageprint.restfuls.bean.ResponseData;
import com.yideguan.imageprint.utils.StringUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImproveInformationActivity extends ToolbarActivity<ActivityImproveInformationBinding> {

    private User mUser;

    @Override
    public int getContentLayout() {
        return R.layout.activity_improve_information;
    }

    @Override
    protected void initData() {
        mUser = getIntent().getParcelableExtra(Constants.KEY_NEW_USER);
    }

    @Override
    protected void setListener() {

    }

    public void finishAction(View view) {
        String name = mDataBinding.cetName.getText().toString();
        if (StringUtil.isEmpty(name)) {
            Toast.makeText(this, R.string.input_name, Toast.LENGTH_SHORT).show();
            return;
        }

        mUser.setUserName(name);

        GlobalRestful.getInstance()
                .RegisterUser(mUser, new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if (response.body().getCode() == ResponseCode.Normal.getValue()) {
                            finish();
                        } else {
                            Toast.makeText(ImproveInformationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {

                    }
                });
    }

}

