package com.yideguan.imageprint.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.yideguan.imageprint.R;
import com.yideguan.imageprint.enumobject.restfuls.ResponseCode;
import com.yideguan.imageprint.restfuls.GlobalRestful;
import com.yideguan.imageprint.restfuls.bean.Device;
import com.yideguan.imageprint.restfuls.bean.ResponseData;
import com.yideguan.imageprint.utils.DeviceUtil;
import com.yideguan.imageprint.utils.MD5Util;
import com.yideguan.imageprint.utils.StringUtil;
import com.yideguan.imageprint.views.ClearEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ClearEditText cet_account, cet_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById();
    }

    private void findViewById() {
        cet_account = (ClearEditText) findViewById(R.id.cet_account);
        cet_password = (ClearEditText) findViewById(R.id.cet_password);
    }

    public void loginAction(View view) {
        String phone = cet_account.getText().toString();

        if (StringUtil.isEmpty(phone) || phone.length() != 11) {
            Toast.makeText(this, R.string.input_corrent_phone, Toast.LENGTH_SHORT).show();
            return;
        }

        String password = cet_password.getText().toString();
        if (StringUtil.isEmpty(password) || password.length() < 6 || password.length() > 16) {
            Toast.makeText(this, R.string.input_corrent_password, Toast.LENGTH_SHORT).show();
            return;
        }

        Device device = DeviceUtil.getDeviceInfo(getApplicationContext());
        device.setDeviceToken("abc");   // 模拟数据
        device.setDeviceID(16);

        GlobalRestful.getInstance()
                .LoginUser(phone, MD5Util.MD5(password), device, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (ResponseCode.isSuccess(response.body())) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    public void forgetPasswordAction(View view) {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        startActivity(intent);
    }

    public void signUpAction(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}

