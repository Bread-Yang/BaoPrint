package com.yideguan.imageprint.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.socks.library.KLog;
import com.yideguan.imageprint.R;
import com.yideguan.imageprint.restfuls.GlobalRestful;
import com.yideguan.imageprint.restfuls.bean.ResponseData;
import com.yideguan.imageprint.utils.DeviceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginAction(View view) {
        GlobalRestful.INSTANCE.LoginUser("15521377721", "123", DeviceUtils.getDeviceInfo(getApplicationContext()), new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                KLog.e("code : " + response.body().Code);
//                KLog.e("拿到的内容是 : " + response.body().Content);
//                KLog.e("拿到的message是 : " + response.body().Message);
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

