package com.MDGround.HaiLanPrint.activity.login;

import android.os.CountDownTimer;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.MDGround.HaiLanPrint.R;
import com.MDGround.HaiLanPrint.activity.base.ToolbarActivity;
import com.MDGround.HaiLanPrint.constants.Constants;
import com.MDGround.HaiLanPrint.databinding.ActivityForgetPasswordBinding;
import com.MDGround.HaiLanPrint.enumobject.restfuls.ResponseCode;
import com.MDGround.HaiLanPrint.restfuls.GlobalRestful;
import com.MDGround.HaiLanPrint.restfuls.bean.ResponseData;
import com.MDGround.HaiLanPrint.utils.MD5Util;
import com.MDGround.HaiLanPrint.utils.StringUtil;
import com.MDGround.HaiLanPrint.utils.ViewUtils;
import com.socks.library.KLog;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends ToolbarActivity<ActivityForgetPasswordBinding> {

    @Override
    public int getContentLayout() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initData() {
        mDataBinding.cetAccount.append(getIntent().getStringExtra(Constants.KEY_PHONE));

        SMSSDK.initSDK(this, Constants.SMS_APP_KEY, Constants.SMS_APP_SECRECT);
        SMSSDK.registerEventHandler(mEventHandler);
    }

    @Override
    protected void setListener() {
        mDataBinding.cbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDataBinding.cetPassword.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    mDataBinding.cetPassword.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    private EventHandler mEventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    // 提交验证码成功
                    changePswRequest();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    // 获取验证码成功,开始倒计时
                    KLog.e("获取验证码成功,开始倒计时");
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    // 返回支持发送验证码的国家列表
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ViewUtils.toast("验证码出错");
                    }
                });
            }
        }
    };

    private void changePswRequest() {
        String phone = mDataBinding.cetAccount.getText().toString();
        String password = mDataBinding.cetPassword.getText().toString();

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

    public void getCaptchaAction(View view) {
        String phone = mDataBinding.cetAccount.getText().toString();

        if (StringUtil.isEmpty(phone)) {
            ViewUtils.toast(R.string.input_phone_number);
            return;
        }

        if (phone.length() != 11) {
            ViewUtils.toast(R.string.input_corrent_phone);
            return;
        }

        mDataBinding.tvAcquireCaptcha.setClickable(false);

        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mDataBinding.tvAcquireCaptcha.setText(getString(R.string.after_second_acquire_again, millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mDataBinding.tvAcquireCaptcha.setClickable(true);
                mDataBinding.tvAcquireCaptcha.setText(R.string.acquire_again);
            }
        };
        countDownTimer.start();

        SMSSDK.getVerificationCode("86", phone, new OnSendMessageHandler() {
            /**
             * 此方法在发送验证短信前被调用，传入参数为接收者号码返回true表示此号码无须实际接收短信
             */
            @Override
            public boolean onSendMessage(String country, String phone) {
                // 可添加测试白名单
                /*
                 * if (phone.equals("18576627750")) { return true; }
				 */
                return false;
            }
        });
    }

    public void finishAction(View view) {
        String phone = mDataBinding.cetAccount.getText().toString();

        if (StringUtil.isEmpty(phone)) {
            Toast.makeText(this, R.string.input_phone_number, Toast.LENGTH_SHORT).show();
            return;

        }
        if (phone.length() != 11) {
            Toast.makeText(this, R.string.input_corrent_phone, Toast.LENGTH_SHORT).show();
            return;
        }

        String captcha = mDataBinding.cetCaptcha.getText().toString();
        if (StringUtil.isEmpty(captcha)) {
            Toast.makeText(this, R.string.input_captcha, Toast.LENGTH_SHORT).show();
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

        SMSSDK.submitVerificationCode("86", phone, captcha);
    }

}

