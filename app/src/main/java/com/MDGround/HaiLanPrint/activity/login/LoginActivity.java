package com.MDGround.HaiLanPrint.activity.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.main.MainActivity;
import com.MDGround.HaiLanPrint.application.MDGroundApplication;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityLoginBinding;
import com.MDGround.HaiLanPrint.enumobject.ThirdPartyLoginType;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.models.User;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.DeviceUtil;
import com.MDGround.HaiLanPrint.utils.FileUtils;
import com.MDGround.HaiLanPrint.utils.MD5Util;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding mDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    }

    //region ACTION
    public void loginAction(View view) {
        String phone = mDataBinding.cetAccount.getText().toString();

        if (StringUtil.isEmpty(phone)) {
            Toast.makeText(this, R.string.input_phone_number, Toast.LENGTH_SHORT).show();
            return;

        }
        if (phone.length() != 11) {
            Toast.makeText(this, R.string.input_corrent_phone, Toast.LENGTH_SHORT).show();
            return;
        }

        String password = mDataBinding.cetPassword.getText().toString();

        if (StringUtil.isEmpty(password)) {
            Toast.makeText(this, R.string.input_password, Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6 || password.length() > 16) {
            Toast.makeText(this, R.string.input_corrent_password, Toast.LENGTH_SHORT).show();
            return;
        }

        loginUserRequest(phone, password);
    }

    public void forgetPasswordAction(View view) {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        Bundle extras = new Bundle();
        extras.putString(Constants.KEY_PHONE, mDataBinding.cetAccount.getText().toString());
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void signUpAction(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void autoLoginAction(View view) {

    }

    public void shareAction(View view) {

        OnekeyShare oks = new OnekeyShare();
        oks.setTitle("分享");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }

    public void wechatLoginAction(View view) {
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                String openID = platform.getDb().getUserId();
                String accessToken = platform.getDb().getToken();
                String userName = platform.getDb().getUserName();
                String nickName = platform.getDb().get("nickname");
                String photoUrl = platform.getDb().get("icon");

                loginUserByThirdPartyRequest(ThirdPartyLoginType.Wechat, openID, photoUrl, nickName, userName);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
            }

            @Override
            public void onCancel(Platform platform, int i) {
            }
        });
        wechat.authorize();
    }

    public void qqLoginAction(View view) {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                String openID = platform.getDb().getUserId();
                String userName = platform.getDb().getUserName();
                String nickName = platform.getDb().get("nickname");
                String photoUrl = platform.getDb().get("icon");

                loginUserByThirdPartyRequest(ThirdPartyLoginType.QQ, openID, photoUrl, nickName, userName);

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        qq.authorize();
    }

    public void weiboLoginAction(View view) {
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                String openID = platform.getDb().getUserId();
                String userName = platform.getDb().getUserName();
                String nickName = platform.getDb().get("nickname");
                String photoUrl = platform.getDb().get("icon");

                loginUserByThirdPartyRequest(ThirdPartyLoginType.Weibo, openID, photoUrl, nickName, userName);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        weibo.authorize();
    }
    //endregion

    //region SERVER
    private void loginUserRequest(String phone, String password) {
        ViewUtils.loading(LoginActivity.this);

        GlobalRestful.getInstance()
                .LoginUser(phone, MD5Util.MD5(password), new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        ViewUtils.dismiss();
                        if (ResponseCode.isSuccess(response.body())) {
                            User user = response.body().getContent(User.class);
                            saveUserAndToMainActivity(user);
                        } else {
                            Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                    }
                });
    }

    private void loginUserByThirdPartyRequest(ThirdPartyLoginType loginType,
                                              String openID,
                                              String photoUrl,
                                              String userNickName,
                                              String userName) {
        ViewUtils.loading(LoginActivity.this);
        GlobalRestful.getInstance().LoginUserByThirdParty(loginType, openID, photoUrl, userNickName, userName, new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ViewUtils.dismiss();
                if (ResponseCode.isSuccess(response.body())) {
                    User user = response.body().getContent(User.class);
                    saveUserAndToMainActivity(user);
                } else {
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }
    //endregion

    private void saveUserAndToMainActivity(User user) {
        MDGroundApplication.mLoginUser = user;
        if (mDataBinding.cbAutoLogin.isChecked()) {
            FileUtils.setObject(Constants.KEY_ALREADY_LOGIN_USER, user);
            DeviceUtil.setDeviceId(user.getDeviceID());
        }
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

