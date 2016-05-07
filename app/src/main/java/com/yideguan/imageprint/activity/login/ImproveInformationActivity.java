package com.yideguan.imageprint.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.yideguan.imageprint.R;
import com.yideguan.imageprint.activity.base.ToolbarActivity;
import com.yideguan.imageprint.constants.IntentConstants;
import com.yideguan.imageprint.enumobject.restfuls.ResponseCode;
import com.yideguan.imageprint.models.User;
import com.yideguan.imageprint.restfuls.GlobalRestful;
import com.yideguan.imageprint.restfuls.bean.ResponseData;
import com.yideguan.imageprint.utils.StringUtil;
import com.yideguan.imageprint.views.ClearEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImproveInformationActivity extends ToolbarActivity {

    private ClearEditText cet_name;

    private User mUser;

    @Override
    public int getContentLayout() {
        return R.layout.activity_improve_information;
    }

    @Override
    protected void findViewById() {
        cet_name = (ClearEditText) findViewById(R.id.cet_name);
    }

    @Override
    protected void initData() {
        mUser = getIntent().getParcelableExtra(IntentConstants.NEW_USER);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void finishAction(View view) {
        String name = cet_name.getText().toString();
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

