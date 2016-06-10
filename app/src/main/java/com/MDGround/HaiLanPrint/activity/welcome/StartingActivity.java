package com.MDGround.HaiLanPrint.activity.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.login.LoginActivity;
import com.MDGround.HaiLanPrint.activity.main.MainActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.User;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.DeviceUtil;
import com.MDGround.HaiLanPrint.utils.FileUtils;
import com.MDGround.HaiLanPrint.utils.NavUtils;
import com.MDGround.HaiLanPrint.utils.ViewUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.activity_starting, null);
        setContentView(view);
        // 渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(2000);
        aa.setFillAfter(true);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                MDGroundApplication.mLoginUser = (User) FileUtils.getObject(Constants.KEY_ALREADY_LOGIN_USER);
                if (MDGroundApplication.mLoginUser != null) {

//                    loginRequest(MDGroundApplication.mLoginUser);
                    NavUtils.toMainActivity(StartingActivity.this);
                } else {
                    toLoginActivity();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void toLoginActivity() {
        Intent intent = new Intent(StartingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginRequest(User user) {
        GlobalRestful.getInstance()
                .LoginUser(user.getPhone(), user.getPassword(), new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        ViewUtils.dismiss();
                        if (ResponseCode.isSuccess(response.body())) {
                            User user = response.body().getContent(User.class);
                            FileUtils.setObject(Constants.KEY_ALREADY_LOGIN_USER, user);
                            DeviceUtil.setDeviceId(user.getDeviceID());

                            Intent intent = new Intent(StartingActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ViewUtils.toast(response.body().getMessage());
                            toLoginActivity();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        toLoginActivity();
                    }
                });
    }
}
