package com.yideguan.imageprint.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.yideguan.imageprint.R;
import com.yideguan.imageprint.activity.base.ToolbarActivity;
import com.yideguan.imageprint.enumobject.restfuls.ResponseCode;
import com.yideguan.imageprint.restfuls.GlobalRestful;
import com.yideguan.imageprint.restfuls.bean.ResponseData;
import com.yideguan.imageprint.utils.MD5Util;
import com.yideguan.imageprint.utils.StringUtil;
import com.yideguan.imageprint.views.ClearEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends ToolbarActivity {

    private ClearEditText cet_account, cet_captcha, cet_password;

    @Override
    public int getContentLayout() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void findViewById() {
        cet_account = (ClearEditText) findViewById(R.id.cet_account);
        cet_captcha = (ClearEditText) findViewById(R.id.cet_captcha);
        cet_password = (ClearEditText) findViewById(R.id.cet_password);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void finishAction(View view) {
        String phone = cet_account.getText().toString();

        if (StringUtil.isEmpty(phone) || phone.length() != 11) {
            Toast.makeText(this, R.string.input_corrent_phone, Toast.LENGTH_SHORT).show();
            return;
        }

        String captcha = cet_captcha.getText().toString();
        if (StringUtil.isEmpty(captcha)) {
            Toast.makeText(this, R.string.input_captcha, Toast.LENGTH_SHORT).show();
            return;
        }

        String password = cet_password.getText().toString();
        if (StringUtil.isEmpty(password) || password.length() < 6 || password.length() > 16) {
            Toast.makeText(this, R.string.input_corrent_password, Toast.LENGTH_SHORT).show();
            return;
        }

        GlobalRestful.getInstance()
                .ChangeUserPassword(phone, MD5Util.MD5(password), new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.body().getCode() == ResponseCode.Normal.getValue()) {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

}

